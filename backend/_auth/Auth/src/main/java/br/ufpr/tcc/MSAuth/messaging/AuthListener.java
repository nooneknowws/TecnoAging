package br.ufpr.tcc.MSAuth.messaging;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSAuth.dto.AuthValidationResponse;
import br.ufpr.tcc.MSAuth.services.CorrelationService;


@Component
public class AuthListener {

	private static final Logger logger = LoggerFactory.getLogger(AuthListener.class);
	
	@Autowired
	private CorrelationService correlationService;

	@RabbitListener(queues = "auth.response.paciente.queue")
	public void handlePacienteResponse(AuthValidationResponse response, Message message) {
	    String correlationId = message.getMessageProperties().getCorrelationId();
	    logger.info("Resposta PACIENTE recebida - CorrelationId: {}, Success: {}, ErrorMessage: {}", 
	               correlationId, response.isSuccess(), response.getErrorMessage());
	    
	    CompletableFuture<AuthValidationResponse> future = 
	        correlationService.removePendingRequest(correlationId);
	    if (future != null) {
	        logger.info("Future encontrado, completando...");
	        future.complete(response);
	    } else {
	        logger.warn("Future não encontrado para correlationId: {}", correlationId);
	    }
	}

	@RabbitListener(queues = "auth.response.tecnico.queue")
	public void handleTecnicoResponse(AuthValidationResponse response, Message message) {
	    String correlationId = message.getMessageProperties().getCorrelationId();
	    logger.info("Resposta TECNICO recebida - CorrelationId: {}, Success: {}, ErrorMessage: {}", 
	               correlationId, response.isSuccess(), response.getErrorMessage());
	    
	    CompletableFuture<AuthValidationResponse> future = 
	        correlationService.removePendingRequest(correlationId);
	    if (future != null) {
	        logger.info("Future encontrado, completando...");
	        future.complete(response);
	    } else {
	        logger.warn("Future não encontrado para correlationId: {}", correlationId);
	    }
	}


}