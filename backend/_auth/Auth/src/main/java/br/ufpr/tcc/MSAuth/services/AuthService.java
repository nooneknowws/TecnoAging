package br.ufpr.tcc.MSAuth.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        try {
            logger.info("Aguardando resposta dos microsserviços...");
            
            // Aguarda ambas as respostas com timeout
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futurePaciente, futureTecnico);
            
            try {
                // Tenta aguardar ambas as respostas por no máximo 10 segundos
                allFutures.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                logger.warn("Timeout esperando todas as respostas, processando as que chegaram...");
            }
            
            // Verifica se alguma das respostas foi bem-sucedida
            AuthValidationResponse response = null;
            
            if (futurePaciente.isDone()) {
                try {
                    AuthValidationResponse pacienteResponse = futurePaciente.get();
                    logger.info("Resposta PACIENTE - Success: {}, Message: {}", 
                               pacienteResponse.isSuccess(), pacienteResponse.getErrorMessage());
                    if (pacienteResponse.isSuccess()) {
                        response = pacienteResponse;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao obter resposta do paciente: {}", e.getMessage());
                }
            }
            
            if (response == null && futureTecnico.isDone()) {
                try {
                    AuthValidationResponse tecnicoResponse = futureTecnico.get();
                    logger.info("Resposta TECNICO - Success: {}, Message: {}", 
                               tecnicoResponse.isSuccess(), tecnicoResponse.getErrorMessage());
                    if (tecnicoResponse.isSuccess()) {
                        response = tecnicoResponse;
                    } else if (response == null) {
                        // Se nenhuma resposta foi bem-sucedida, usa a resposta de erro do técnico
                        response = tecnicoResponse;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao obter resposta do técnico: {}", e.getMessage());
                }
            }
            
            // Se ainda não tem resposta, significa que nenhuma chegou
            if (response == null) {
                throw new RuntimeException("Nenhuma resposta recebida dos microsserviços");
            }

            if (response.isSuccess()) {
                logger.info("Autenticação bem-sucedida, criando token...");
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

                logger.info("Token criado com sucesso para usuário: {}", response.getUserName());
                return authUser;
            } else {
                logger.warn("Autenticação falhada - Microservice: {}, Message: {}", 
                           response.getMicroservice(), response.getErrorMessage());
                throw new InvalidCredentialsException("CPF ou senha estão incorretos");
            }

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

    public Map<String, Object> verifyJwtAndExtractData(String token) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            AuthenticatedUser authUser = authUserRepository.findByToken(token);

            if (authUser == null) {
                result.put("valid", false);
                return result;
            }

            jwtTokenProvider.validateToken(token);
            
            // Extract data from token
            String username = jwtTokenProvider.getUsername(token);
            
            result.put("valid", true);
            result.put("userId", authUser.getUserId());
            result.put("username", username);
            result.put("microservice", authUser.getPerfil());
            
            return result;

        } catch (Exception e) {
            logger.error("JWT verification failed: " + e.getMessage());
            result.put("valid", false);
            return result;
        }
    }
    
    public boolean checkUserByCpf(String cpf) {
        String correlationIdPaciente = UUID.randomUUID().toString();
        String correlationIdTecnico = UUID.randomUUID().toString();

        CompletableFuture<AuthValidationResponse> futurePaciente = new CompletableFuture<>();
        CompletableFuture<AuthValidationResponse> futureTecnico = new CompletableFuture<>();

        correlationService.addPendingRequest(correlationIdPaciente, futurePaciente);
        correlationService.addPendingRequest(correlationIdTecnico, futureTecnico);

        Map<String, String> cpfRequest = new HashMap<>();
        cpfRequest.put("cpf", cpf);

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.check.paciente",
            cpfRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdPaciente);
                return m;
            }
        );

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.check.tecnico",
            cpfRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdTecnico);
                return m;
            }
        );

        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futurePaciente, futureTecnico);
            
            try {
                allFutures.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                logger.warn("Timeout verificando CPF, processando respostas recebidas...");
            }
            
            if (futurePaciente.isDone()) {
                try {
                    AuthValidationResponse pacienteResponse = futurePaciente.get();
                    if (pacienteResponse.isSuccess()) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao verificar CPF no serviço de pacientes: {}", e.getMessage());
                }
            }
            
            if (futureTecnico.isDone()) {
                try {
                    AuthValidationResponse tecnicoResponse = futureTecnico.get();
                    if (tecnicoResponse.isSuccess()) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao verificar CPF no serviço de técnicos: {}", e.getMessage());
                }
            }
            
            return false;

        } catch (Exception e) {
            logger.error("Erro ao verificar CPF: {}", e.getMessage());
            return false;
        } finally {
            correlationService.removePendingRequest(correlationIdPaciente);
            correlationService.removePendingRequest(correlationIdTecnico);
        }
    }
    
    public String getTelefoneByMaskedCpf(String cpf) {
        String correlationIdPaciente = UUID.randomUUID().toString();
        String correlationIdTecnico = UUID.randomUUID().toString();

        CompletableFuture<AuthValidationResponse> futurePaciente = new CompletableFuture<>();
        CompletableFuture<AuthValidationResponse> futureTecnico = new CompletableFuture<>();

        correlationService.addPendingRequest(correlationIdPaciente, futurePaciente);
        correlationService.addPendingRequest(correlationIdTecnico, futureTecnico);

        Map<String, String> cpfRequest = new HashMap<>();
        cpfRequest.put("cpf", cpf);

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.telefone.paciente",
            cpfRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdPaciente);
                return m;
            }
        );

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.telefone.tecnico",
            cpfRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdTecnico);
                return m;
            }
        );

        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futurePaciente, futureTecnico);
            
            try {
                allFutures.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                logger.warn("Timeout buscando telefone, processando respostas recebidas...");
            }
            
            if (futurePaciente.isDone()) {
                try {
                    AuthValidationResponse pacienteResponse = futurePaciente.get();
                    if (pacienteResponse.isSuccess() && pacienteResponse.getTelefone() != null) {
                        return pacienteResponse.getTelefone();
                    }
                } catch (Exception e) {
                    logger.error("Erro ao buscar telefone no serviço de pacientes: {}", e.getMessage());
                }
            }
            
            if (futureTecnico.isDone()) {
                try {
                    AuthValidationResponse tecnicoResponse = futureTecnico.get();
                    if (tecnicoResponse.isSuccess() && tecnicoResponse.getTelefone() != null) {
                        return tecnicoResponse.getTelefone();
                    }
                } catch (Exception e) {
                    logger.error("Erro ao buscar telefone no serviço de técnicos: {}", e.getMessage());
                }
            }
            
            return null;

        } catch (Exception e) {
            logger.error("Erro ao buscar telefone: {}", e.getMessage());
            return null;
        } finally {
            correlationService.removePendingRequest(correlationIdPaciente);
            correlationService.removePendingRequest(correlationIdTecnico);
        }
    }
    
    public boolean resetPassword(String cpf, String novaSenha) {
        String correlationIdPaciente = UUID.randomUUID().toString();
        String correlationIdTecnico = UUID.randomUUID().toString();

        CompletableFuture<AuthValidationResponse> futurePaciente = new CompletableFuture<>();
        CompletableFuture<AuthValidationResponse> futureTecnico = new CompletableFuture<>();

        correlationService.addPendingRequest(correlationIdPaciente, futurePaciente);
        correlationService.addPendingRequest(correlationIdTecnico, futureTecnico);

        Map<String, String> resetRequest = new HashMap<>();
        resetRequest.put("cpf", cpf);
        resetRequest.put("novaSenha", novaSenha);

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.reset.paciente.queue",
            resetRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdPaciente);
                return m;
            }
        );

        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "auth.reset.tecnico.queue",
            resetRequest,
            m -> {
                m.getMessageProperties().setCorrelationId(correlationIdTecnico);
                return m;
            }
        );

        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futurePaciente, futureTecnico);
            
            try {
                allFutures.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                logger.warn("Timeout resetando senha, processando respostas recebidas...");
            }
            
            if (futurePaciente.isDone()) {
                try {
                    AuthValidationResponse pacienteResponse = futurePaciente.get();
                    if (pacienteResponse.isSuccess()) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao resetar senha no serviço de pacientes: {}", e.getMessage());
                }
            }
            
            if (futureTecnico.isDone()) {
                try {
                    AuthValidationResponse tecnicoResponse = futureTecnico.get();
                    if (tecnicoResponse.isSuccess()) {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("Erro ao resetar senha no serviço de técnicos: {}", e.getMessage());
                }
            }
            
            return false;

        } catch (Exception e) {
            logger.error("Erro ao resetar senha: {}", e.getMessage());
            return false;
        } finally {
            correlationService.removePendingRequest(correlationIdPaciente);
            correlationService.removePendingRequest(correlationIdTecnico);
        }
    }
}