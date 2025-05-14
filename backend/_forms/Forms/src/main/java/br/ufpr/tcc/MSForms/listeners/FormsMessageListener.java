package br.ufpr.tcc.MSForms.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSForms.service.FormsService;
import br.ufpr.tcc.MSForms.models.dto.PacienteDTO;
import br.ufpr.tcc.MSForms.models.dto.TecnicoDTO;

@Component
public class FormsMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(FormsMessageListener.class);

    @Autowired
    private FormsService messageSender;

    @RabbitListener(queues = "response.paciente.queue")
    public void handlePacienteResponse(PacienteDTO pacienteDTO, 
                                     org.springframework.amqp.core.Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId == null) {
            logger.error("Missing correlation ID in paciente response. Message consumed but not processed. PacienteDTO: {}", pacienteDTO);
            return;
        }
        logger.info("Received paciente response for correlationId: {}", correlationId);
        logger.info("PacienteDTO: {}", pacienteDTO);
        messageSender.completeRequest(correlationId, pacienteDTO);
    }

    @RabbitListener(queues = "response.tecnico.queue")
    public void handleTecnicoResponse(TecnicoDTO tecnicoDTO, 
                                    org.springframework.amqp.core.Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        if (correlationId == null) {
            logger.error("Missing correlation ID in tecnico response. Message consumed but not processed. TecnicoDTO: {}", tecnicoDTO);
            return;
        }
        logger.info("Received tecnico response for correlationId: {}", correlationId);
        logger.info("TecnicoDTO: {}", tecnicoDTO);
        messageSender.completeRequest(correlationId, tecnicoDTO);
    }

    @RabbitListener(queues = "response.paciente.queue.error")
    public void handlePacienteError(String error) {
        logger.error("Error in paciente processing: {}", error);
    }

    @RabbitListener(queues = "response.tecnico.queue.error")
    public void handleTecnicoError(String error) {
        logger.error("Error in tecnico processing: {}", error);
    }
}