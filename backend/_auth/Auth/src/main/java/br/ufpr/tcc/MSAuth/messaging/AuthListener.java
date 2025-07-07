package br.ufpr.tcc.MSAuth.messaging;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSAuth.dto.AuthValidationResponse;
import br.ufpr.tcc.MSAuth.services.CorrelationService;


@Component
public class AuthListener {

	@Autowired
	private CorrelationService correlationService;

	@RabbitListener(queues = "auth.response.paciente.queue")
	public void handlePacienteResponse(AuthValidationResponse response, Message message) {
	    String correlationId = message.getMessageProperties().getCorrelationId();
	    CompletableFuture<AuthValidationResponse> future = 
	        correlationService.removePendingRequest(correlationId);
	    if (future != null) future.complete(response);
	}

	@RabbitListener(queues = "auth.response.tecnico.queue")
	public void handleTecnicoResponse(AuthValidationResponse response, Message message) {
	    String correlationId = message.getMessageProperties().getCorrelationId();
	    CompletableFuture<AuthValidationResponse> future = 
	        correlationService.removePendingRequest(correlationId);
	    if (future != null) future.complete(response);
	}


}