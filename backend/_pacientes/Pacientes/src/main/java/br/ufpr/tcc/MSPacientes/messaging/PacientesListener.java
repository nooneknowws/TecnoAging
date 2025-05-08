package br.ufpr.tcc.MSPacientes.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void handlePacienteQuery(String pacienteId) {
        logger.info("Received paciente query for ID: {}", pacienteId);
        try {

            String cleanId = pacienteId.replaceAll("^\"|\"$", "");
            Long id = Long.parseLong(pacienteId);
            logger.debug("Looking up paciente with ID: {}", id);
            
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
                "saga.exchange",
                "query.paciente",
                response,
                message -> {
                    message.getMessageProperties().setContentType("application/json");
                    return message;
                }
            );
        } catch (Exception e) {
            logger.error("Error processing paciente query for ID: {}", pacienteId, e);
            rabbitTemplate.convertAndSend(
            	"saga.exchange",
                "response.paciente.queue.error",
                "Error processing paciente query: " + e.getMessage()
            );
        }
    }
}