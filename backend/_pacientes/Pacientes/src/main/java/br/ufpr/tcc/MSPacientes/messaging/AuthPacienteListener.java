package br.ufpr.tcc.MSPacientes.messaging;

import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSPacientes.models.Paciente;
import br.ufpr.tcc.MSPacientes.models.dto.AuthDTO;
import br.ufpr.tcc.MSPacientes.models.dto.AuthValidationResponse;
import br.ufpr.tcc.MSPacientes.repository.PacienteRepository;
import ch.qos.logback.classic.Logger;

@Component
public class AuthPacienteListener {
    
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AuthPacienteListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @RabbitListener(queues = "auth.query.paciente.queue")
    public void handleAuthRequest(AuthDTO authDTO, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        logger.info("Auth request for CPF: {}", authDTO.getCpf());

        try {
            // Remove formatting characters from CPF before searching
            String cleanCpf = authDTO.getCpf().replaceAll("[^0-9]", "");
            logger.info("Original CPF: {}, Clean CPF: {}", authDTO.getCpf(), cleanCpf);
            Optional<Paciente> pacienteOpt = pacienteRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (pacienteOpt.isPresent()) {
                Paciente paciente = pacienteOpt.get();
                logger.info("Paciente encontrado: ID={}, Nome={}", paciente.getId(), paciente.getNome());
                logger.info("Validando senha para paciente ID: {}", paciente.getId());
                
                if (paciente.verificarSenha(authDTO.getSenha())) {
                    logger.info("Senha válida para paciente ID: {}", paciente.getId());
                    response = AuthValidationResponse.success(
                        "PACIENTE",
                        paciente.getId(),
                        paciente.getNome()
                    );
                } else {
                    logger.warn("Senha inválida para paciente ID: {}", paciente.getId());
                    response = AuthValidationResponse.error(
                        "Senha inválida", 
                        "PACIENTE"
                    );
                }
            } else {
                logger.warn("CPF não encontrado: {}", cleanCpf);
                response = AuthValidationResponse.error(
                    "CPF não encontrado", 
                    "PACIENTE"
                );
            }
            

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.paciente",
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
                "auth.response.paciente.error",
                AuthValidationResponse.error("Erro interno", "PACIENTE"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.check.paciente.queue")
    public void handleCheckCpfRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        logger.info("Check CPF request for: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Paciente> pacienteOpt = pacienteRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (pacienteOpt.isPresent()) {
                response = AuthValidationResponse.success("PACIENTE", null, null);
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "PACIENTE");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.paciente",
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
                "auth.response.paciente.error",
                AuthValidationResponse.error("Erro interno", "PACIENTE"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.telefone.paciente.queue")
    public void handleGetTelefoneRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        logger.info("Get telefone request for CPF: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Paciente> pacienteOpt = pacienteRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (pacienteOpt.isPresent()) {
                Paciente paciente = pacienteOpt.get();
                response = new AuthValidationResponse(true, null, "PACIENTE", null, null, paciente.getTelefone());
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "PACIENTE");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.paciente",
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
                "auth.response.paciente.error",
                AuthValidationResponse.error("Erro interno", "PACIENTE"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
    
    @RabbitListener(queues = "auth.reset.paciente.queue")
    public void handleResetPasswordRequest(Map<String, String> request, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String cpf = request.get("cpf");
        String novaSenha = request.get("novaSenha");
        logger.info("Reset password request for CPF: {}", cpf);

        try {
            String cleanCpf = cpf.replaceAll("[^0-9]", "");
            Optional<Paciente> pacienteOpt = pacienteRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (pacienteOpt.isPresent()) {
                Paciente paciente = pacienteOpt.get();
                paciente.redefinirSenha(novaSenha);
                pacienteRepository.save(paciente);
                
                response = AuthValidationResponse.success("PACIENTE", null, null);
                logger.info("Senha resetada com sucesso para paciente ID: {}", paciente.getId());
            } else {
                response = AuthValidationResponse.error("CPF não encontrado", "PACIENTE");
            }

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.paciente",
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
                "auth.response.paciente.error",
                AuthValidationResponse.error("Erro interno", "PACIENTE"),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}