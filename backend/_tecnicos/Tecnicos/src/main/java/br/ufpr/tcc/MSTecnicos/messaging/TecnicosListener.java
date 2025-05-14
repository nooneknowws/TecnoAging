package br.ufpr.tcc.MSTecnicos.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSTecnicos.models.dto.TecnicoDTO;
import br.ufpr.tcc.MSTecnicos.models.Tecnico;
import br.ufpr.tcc.MSTecnicos.repository.TecnicoRepository;

@Component
public class TecnicosListener {
    
    private static final Logger logger = LoggerFactory.getLogger(TecnicosListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @RabbitListener(queues = "query.tecnico.queue")
    public void handleTecnicoQuery(Message message) {

    	logger.info("Message: {}", message.getMessageProperties());
    	
        String correlationId = message.getMessageProperties().getCorrelationId();
        
        logger.info("Received message with correlationId: {}", correlationId);
        
        String tecnicoId = new String(message.getBody());
        logger.info("Processing tecnico query for ID: {}", tecnicoId);

        try {
            Long id = Long.parseLong(tecnicoId.replaceAll("^\"|\"$", ""));
            logger.debug("Looking up tecnico with ID: {}", id);
            
            Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado com ID: " + id));
            
            TecnicoDTO response = new TecnicoDTO(
                tecnico.getId(),
                tecnico.getNome()
            );
            
            logger.info("Sending tecnico response with correlationId: {}", correlationId);
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    m.getMessageProperties().setContentType("application/json");
                    return m;
                }
            );
        } catch (Exception e) {
            logger.error("Error processing tecnico query for ID: {}", tecnicoId, e);
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "response.tecnico.queue.error",
                "Error processing tecnico query: " + e.getMessage(),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}