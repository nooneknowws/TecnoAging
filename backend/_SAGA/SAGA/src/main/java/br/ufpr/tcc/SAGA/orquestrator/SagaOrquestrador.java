package br.ufpr.tcc.SAGA.orquestrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpr.tcc.SAGA.queues.SAGAExchange;

@Service
public class SagaOrquestrador {
    private static final Logger logger = LoggerFactory.getLogger(SagaOrquestrador.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @RabbitListener(queues = "query.paciente.queue")
    public void forwardPacienteQuery(String pacienteId) {
        try {
            logger.info("Received paciente query for ID: {}", pacienteId);
            rabbitTemplate.convertAndSend(
            	"saga-exchange",
                "query.paciente.queue",
                pacienteId,
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setReplyTo("response.paciente.queue");
                    logger.debug("Set replyTo: {}", "response.paciente.queue");
                    return message;
                }
            );
            logger.info("Forwarded paciente query to routing key: {}", "query.paciente");
        } catch (Exception e) {
            logger.error("Error forwarding paciente query for ID: {}", pacienteId, e);
        }
    }

    @RabbitListener(queues = "query.tecnico.queue")
    public void forwardTecnicoQuery(String tecnicoId) {
        try {
            logger.info("Received tecnico query for ID: {}", tecnicoId);
            rabbitTemplate.convertAndSend(
            	"saga-exchange",
            	"query.tecnico.queue",
                tecnicoId,
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    message.getMessageProperties().setReplyTo("response.tecnico.queue");
                    logger.debug("Set replyTo: {}", "response.tecnico.queue");
                    return message;
                }
            );
            logger.info("Forwarded tecnico query to routing key: {}", "query.tecnico");
        } catch (Exception e) {
            logger.error("Error forwarding tecnico query for ID: {}", tecnicoId, e);
        }
    }
}