package br.ufpr.tcc.MSTecnicos.messaging;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSTecnicos.models.Tecnico;
import br.ufpr.tcc.MSTecnicos.models.dto.AuthDTO;
import br.ufpr.tcc.MSTecnicos.models.dto.AuthValidationResponse;
import br.ufpr.tcc.MSTecnicos.repository.TecnicoRepository;

@Component
public class AuthTecnicoListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthTecnicoListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @RabbitListener(queues = "auth.query.tecnico.queue")
    public void handleAuthRequest(AuthDTO authDTO, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        logger.info("Auth request for CPF: {}", authDTO.getCpf());

        try {
            // Remove formatting characters from CPF before searching
            String cleanCpf = authDTO.getCpf().replaceAll("[^0-9]", "");
            logger.info("Original CPF: {}, Clean CPF: {}", authDTO.getCpf(), cleanCpf);
            Optional<Tecnico> tecnicoOpt = tecnicoRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (tecnicoOpt.isPresent()) {
                Tecnico tecnico = tecnicoOpt.get();
                if (tecnico.verificarSenha(authDTO.getSenha())) {
                    response = AuthValidationResponse.success(
                        "TECNICO",
                        tecnico.getId(),
                        tecnico.getNome()
                    );
                } else {
                    response = AuthValidationResponse.error(
                        "Senha inválida", 
                        "TECNICO"
                    );
                }
            } else {
                response = AuthValidationResponse.error(
                    "CPF não encontrado", 
                    "TECNICO"
                );
            }
            

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );

        } catch (Exception e) {
            logger.error("Auth error: {}", e.getMessage());
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico.error",
                new AuthValidationResponse(false, "Erro interno", "TECNICO", null, correlationId),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.check.tecnico.queue")
    public void handleCheckCpfRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        logger.info("Check CPF request for: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Tecnico> tecnicoOpt = tecnicoRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (tecnicoOpt.isPresent()) {
                response = AuthValidationResponse.success("TECNICO", null, null);
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "TECNICO");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );

        } catch (Exception e) {
            logger.error("Check CPF error: {}", e.getMessage());
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico.error",
                AuthValidationResponse.error("Erro interno", "TECNICO"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.telefone.tecnico.queue")
    public void handleGetTelefoneRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        logger.info("Get telefone request for CPF: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Tecnico> tecnicoOpt = tecnicoRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (tecnicoOpt.isPresent()) {
                Tecnico tecnico = tecnicoOpt.get();
                response = AuthValidationResponse.success("TECNICO", null, tecnico.getTelefone());
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "TECNICO");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );

        } catch (Exception e) {
            logger.error("Get telefone error: {}", e.getMessage());
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico.error",
                AuthValidationResponse.error("Erro interno", "TECNICO"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.reset.tecnico.queue")
    public void handleResetPasswordRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        String novaSenha = request.get("novaSenha");
        logger.info("Reset password request for CPF: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Tecnico> tecnicoOpt = tecnicoRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (tecnicoOpt.isPresent()) {
                Tecnico tecnico = tecnicoOpt.get();
                tecnico.redefinirSenha(novaSenha);
                tecnicoRepository.save(tecnico);
                
                response = AuthValidationResponse.success("TECNICO", null, null);
                logger.info("Senha resetada com sucesso para técnico ID: {}", tecnico.getId());
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "TECNICO");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );

        } catch (Exception e) {
            logger.error("Reset password error: {}", e.getMessage());
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico.error",
                AuthValidationResponse.error("Erro interno", "TECNICO"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}