package br.ufpr.tcc.MSPacientes.messaging;

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
                new AuthValidationResponse(false, "Erro interno", "PACIENTE", null, correlationId),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}