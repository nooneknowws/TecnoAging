package br.ufpr.tcc.MSForms.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FormsMessageListener {
	 private static final Logger logger = LoggerFactory.getLogger(FormsMessageListener.class);

	    @Autowired
	    private RabbitTemplate rabbitTemplate;

	    @Autowired
	    private ObjectMapper objectMapper;
	    
}
