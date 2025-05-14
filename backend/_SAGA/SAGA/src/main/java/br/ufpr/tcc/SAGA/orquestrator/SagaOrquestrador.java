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

}