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
            Optional<Paciente> pacienteOpt = pacienteRepository.findByCpf(authDTO.getCpf());
            
            AuthValidationResponse response;
            
                Paciente paciente = pacienteOpt.get();
                if (paciente.verificarSenha(authDTO.getSenha())) {
                    response = AuthValidationResponse.success(
                        "PACIENTE",
                        paciente.getId(),
                        paciente.getNome()
                    );
                } else {
                    response = AuthValidationResponse.error(
                        "Senha inválida", 
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