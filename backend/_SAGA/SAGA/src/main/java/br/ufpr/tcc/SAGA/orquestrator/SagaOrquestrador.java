package br.ufpr.tcc.SAGA.orquestrator;

import org.slf4j.Logger;
import java.io.IOException;
import java.util.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import org.slf4j.LoggerFactory;

@Service
public class SagaOrquestrador {

    private static final Logger logger = LoggerFactory.getLogger(SagaOrquestrador.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;
    
}