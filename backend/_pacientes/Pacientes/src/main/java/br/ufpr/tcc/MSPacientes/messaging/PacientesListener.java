package br.ufpr.tcc.MSPacientes.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSPacientes.models.dto.PacienteDTO;
import br.ufpr.tcc.MSPacientes.models.Paciente;
import br.ufpr.tcc.MSPacientes.repository.PacienteRepository;

@Component
public class PacientesListener {
	
	private static final Logger logger = LoggerFactory.getLogger(PacientesListener.class);
	
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @RabbitListener(queues = "query.paciente.queue")
    public void handlePacienteQuery(Message message) {
    	logger.info("Message: {}", message.getMessageProperties());

        String correlationId = message.getMessageProperties().getCorrelationId();
        
        logger.info("Received message with correlationId: {}", correlationId);
        
        String pacienteId = new String(message.getBody());
        logger.info("Processing tecnico query for ID: {}", pacienteId);
        
        try {
        	Long id = Long.parseLong(pacienteId.replaceAll("^\"|\"$", ""));
        	
            Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));
            
            PacienteDTO response = new PacienteDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getIdade(),
                paciente.getImc()
            );
            
            logger.debug("Sending paciente response: {}", response);
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "response.paciente",
                response,
                m -> {
                	m.getMessageProperties().setCorrelationId(correlationId);
                    m.getMessageProperties().setContentType("application/json");

                    logger.info("JSON: {}", m);
                    return m;
                }
            );
        } catch (Exception e) {
            logger.error("Error processing paciente query for ID: {}", pacienteId, e);
            rabbitTemplate.convertAndSend(
            	"saga-exchange",
                "response.paciente.queue.error",
                "Error processing paciente query: " + e.getMessage(),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}