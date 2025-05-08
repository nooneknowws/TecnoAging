package br.ufpr.tcc.MSTecnicos.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void handleTecnicoQuery(String tecnicoId) {
        logger.info("Received tecnico query for ID: {}", tecnicoId);
        try {
        	String cleanId = tecnicoId.replaceAll("^\"|\"$", "");
            Long id = Long.parseLong(tecnicoId);
            logger.debug("Looking up tecnico with ID: {}", id);
            
            Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado com ID: " + id));
            
            TecnicoDTO response = new TecnicoDTO(
                tecnico.getId(),
                tecnico.getNome()
            );
            
            logger.debug("Sending tecnico response: {}", response);
            rabbitTemplate.convertAndSend(
            	"saga-exchange",
                "query.tecnico",
                response,
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    return message;
                }
            );
        } catch (Exception e) {
            logger.error("Error processing tecnico query for ID: {}", tecnicoId, e);
            rabbitTemplate.convertAndSend(
            	"saga-exchange",
            	"response.tecnico.queue.error",
                "Error processing tecnico query: " + e.getMessage()
            );
        }
    }
}