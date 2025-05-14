package br.ufpr.tcc.MSForms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpr.tcc.MSForms.models.dto.PacienteDTO;
import br.ufpr.tcc.MSForms.models.dto.TecnicoDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FormsService {

    private static final Logger logger = LoggerFactory.getLogger(FormsService.class);
    
   
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private final ConcurrentHashMap<String, CompletableFuture<?>> pendingRequests = new ConcurrentHashMap<>();
    
    public CompletableFuture<PacienteDTO> requestPacienteData(Long pacienteId) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<PacienteDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        
        logger.info("Sending paciente query for ID: {} with correlationId: {}", pacienteId, correlationId);
        
        rabbitTemplate.convertAndSend(
        		"saga-exchange",
        		"query.paciente.queue",
            pacienteId.toString(),
            message -> {
                message.getMessageProperties().setCorrelationId(correlationId);
                logger.info("mensagem {}, ID: {}", message, correlationId);
                return message;
            });
        
        return future;
    }

    public CompletableFuture<TecnicoDTO> requestTecnicoData(Long tecnicoId) {
        String correlationId = UUID.randomUUID().toString();
        CompletableFuture<TecnicoDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        
        logger.info("Sending tecnico query for ID: {} with correlationId: {}", tecnicoId, correlationId);
        
        rabbitTemplate.convertAndSend(
            "saga-exchange",
            "query.tecnico.queue",
            tecnicoId.toString(),
            message -> {
                message.getMessageProperties().setCorrelationId(correlationId);
                return message;
            });
        
        return future;
    }
    
    @SuppressWarnings("unchecked")
    public <T> void completeRequest(String correlationId, T result) {
        logger.info("Completing request with correlationId: {}", correlationId);
        CompletableFuture<T> future = (CompletableFuture<T>) pendingRequests.remove(correlationId);
        if (future != null) {
            future.complete(result);
            logger.debug("Successfully completed future for correlationId: {}", correlationId);
        } else {
            logger.warn("No pending request found for correlationId: {}", correlationId);
        }
    }
}