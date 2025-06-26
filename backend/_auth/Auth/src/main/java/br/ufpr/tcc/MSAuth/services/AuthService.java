package br.ufpr.tcc.MSAuth.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufpr.tcc.MSAuth.dto.AuthDTO;
import br.ufpr.tcc.MSAuth.dto.AuthValidationResponse;
import br.ufpr.tcc.MSAuth.models.AuthenticatedUser;
import br.ufpr.tcc.MSAuth.repositories.AuthenticatedUserRepository;
import br.ufpr.tcc.MSAuth.security.JwtTokenProvider;

import br.ufpr.tcc.MSAuth.exceptions.InvalidCredentialsException;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AuthenticatedUserRepository authUserRepository;

    @Autowired
    private CorrelationService correlationService;

    public boolean logout(String token) {
        return authUserRepository.deleteByToken(token) > 0;
    }

    public AuthenticatedUser login(AuthDTO authDTO) {
        String correlationIdPaciente = UUID.randomUUID().toString();
        String correlationIdTecnico = UUID.randomUUID().toString();

        CompletableFuture<AuthValidationResponse> futurePaciente = new CompletableFuture<>();
        CompletableFuture<AuthValidationResponse> futureTecnico = new CompletableFuture<>();

        correlationService.addPendingRequest(correlationIdPaciente, futurePaciente);
        correlationService.addPendingRequest(correlationIdTecnico, futureTecnico);

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.query.paciente",
            authDTO,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdPaciente);
                return m;
            }
        );

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.query.tecnico",
            authDTO,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdTecnico);
                return m;
            }
        );

        CompletableFuture<Object> combinedFuture = CompletableFuture.anyOf(
            futurePaciente,
            futureTecnico
        );

        try {
            AuthValidationResponse response = (AuthValidationResponse) combinedFuture.get(10, TimeUnit.SECONDS);

            if (response.isSuccess()) {
                String token = jwtTokenProvider.createToken(
                    authDTO.getCpf(),
                    List.of("ROLE_USER", "ROLE_" + response.getMicroservice()),
                    response.getUserId(),
                    response.getUserName(),
                    response.getMicroservice()
                );

                AuthenticatedUser authUser = new AuthenticatedUser(
                    response.getUserId().toString(),
                    response.getUserName(),
                    response.getMicroservice(),
                    token
                );

                authUserRepository.save(authUser);

                return authUser;
            } else {
                throw new InvalidCredentialsException("CPF ou senha estão incorretos");
            }

        } catch (TimeoutException e) {
            throw new RuntimeException("Timeout ao autenticar");
        } catch (Exception e) {
            throw new RuntimeException("Falha na autenticação: " + e.getMessage());
        } finally {
            correlationService.removePendingRequest(correlationIdPaciente);
            correlationService.removePendingRequest(correlationIdTecnico);
        }
    }

    public boolean verifyJwt(String token) {
        try {
            AuthenticatedUser authUser = authUserRepository.findByToken(token);

            if (authUser == null) {
                return false;
            }

            jwtTokenProvider.validateToken(token);

            return true;

        } catch (Exception e) {
            logger.error("JWT verification failed: " + e.getMessage());
            return false;
        }
    }
}