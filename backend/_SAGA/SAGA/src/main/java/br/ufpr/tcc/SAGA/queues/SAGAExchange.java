package br.ufpr.tcc.SAGA.queues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class SAGAExchange {
    

    private static final Logger logger = LoggerFactory.getLogger(SAGAExchange.class);

    // Exchange
    public static final String SAGA_EXCHANGE = "saga-exchange";
    //Conection Queue
    public static final String CONECTION_QUEUE = "connection.queue";
    
    // Avaliacao Query Queues
    public static final String PACIENTE_QUERY_QUEUE = "query.paciente.queue";
    public static final String TECNICO_QUERY_QUEUE = "query.tecnico.queue";
    
    // Avaliacao Response Queues
    public static final String PACIENTE_RESPONSE_QUEUE = "response.paciente.queue";
    public static final String TECNICO_RESPONSE_QUEUE = "response.tecnico.queue";
    
    // Auth Query Queues 
    public static final String AUTH_PACIENTE_QUERY_QUEUE = "auth.query.paciente.queue";
    public static final String AUTH_TECNICO_QUERY_QUEUE = "auth.query.tecnico.queue";

    // Auth Response Queues
    public static final String AUTH_PACIENTE_RESPONSE_QUEUE = "auth.response.paciente.queue";
    public static final String AUTH_TECNICO_RESPONSE_QUEUE = "auth.response.tecnico.queue";
    public static final String AUTH_PACIENTE_ERROR_QUEUE = "auth.response.paciente.error";
    public static final String AUTH_TECNICO_ERROR_QUEUE = "auth.response.tecnico.error";

    public static final String PACIENTE_RESPONSE_QUEUE_ERROR = "response.paciente.queue.error";
    public static final String TECNICO_RESPONSE_QUEUE_ERROR = "response.tecnico.queue.error";
    
    // Routing Keys - CHANGED TO MATCH QUEUE NAMES
    public static final String PACIENTE_QUERY_ROUTING_KEY = "query.paciente.queue";
    public static final String TECNICO_QUERY_ROUTING_KEY = "query.tecnico.queue";  
    public static final String PACIENTE_RESPONSE_ROUTING_KEY = "response.paciente";
    public static final String TECNICO_RESPONSE_ROUTING_KEY = "response.tecnico";
    public static final String AUTH_PACIENTE_QUERY_ROUTING_KEY = "auth.query.paciente";
    public static final String AUTH_TECNICO_QUERY_ROUTING_KEY = "auth.query.tecnico";
    public static final String AUTH_PACIENTE_RESPONSE_ROUTING_KEY = "auth.response.paciente";
    public static final String AUTH_TECNICO_RESPONSE_ROUTING_KEY = "auth.response.tecnico";
    public static final String AUTH_PACIENTE_ERROR_ROUTING_KEY = "auth.response.paciente.error";
    public static final String AUTH_TECNICO_ERROR_ROUTING_KEY = "auth.response.tecnico.error";
    
    @Bean
    public DirectExchange sagaExchange() {
        return new DirectExchange(SAGA_EXCHANGE, true, false);
    }
    
    @Bean
    public Queue conectionQueue() {
        return new Queue(CONECTION_QUEUE, true);
    }

    @Bean
    public Queue authPacienteQueryQueue() {
        return new Queue(AUTH_PACIENTE_QUERY_QUEUE, true);
    }

    @Bean
    public Queue authTecnicoQueryQueue() {
        return new Queue(AUTH_TECNICO_QUERY_QUEUE, true);
    }

    @Bean
    public Queue authPacienteResponseQueue() {
        return new Queue(AUTH_PACIENTE_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue authTecnicoResponseQueue() {
        return new Queue(AUTH_TECNICO_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue authPacienteErrorQueue() {
        return new Queue(AUTH_PACIENTE_ERROR_QUEUE, true);
    }

    @Bean
    public Queue authTecnicoErrorQueue() {
        return new Queue(AUTH_TECNICO_ERROR_QUEUE, true);
    }
    @Bean
    public Queue pacienteQueryQueue() {
        logger.info("Declaring Paciente Query Queue: {}", PACIENTE_QUERY_QUEUE);
        return new Queue(PACIENTE_QUERY_QUEUE, true);
    }

    @Bean
    public Queue tecnicoQueryQueue() {
        logger.info("Declaring Tecnico Query Queue: {}", TECNICO_QUERY_QUEUE);
        return new Queue(TECNICO_QUERY_QUEUE, true);
    }

   

    @Bean
    public Queue pacienteResponseQueue() {
        return new Queue(PACIENTE_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue tecnicoResponseQueue() {
        return new Queue(TECNICO_RESPONSE_QUEUE, true);
    }
    @Bean
    public Queue pacienteErrorQueue() {
        return new Queue(PACIENTE_RESPONSE_QUEUE_ERROR, true);
    }

    @Bean
    public Queue tecnicoErrorQueue() {
        return new Queue(TECNICO_RESPONSE_QUEUE_ERROR, true);
    }
    @Bean
    public Binding conectionBinding() {
        logger.info("Binding {} to {} with routing key {}", 
          CONECTION_QUEUE, SAGA_EXCHANGE);
        return BindingBuilder.bind(pacienteQueryQueue())
                .to(sagaExchange())
                .with(SAGA_EXCHANGE);
    }
    
    @Bean
    public Binding pacienteQueryBinding() {
        logger.info("Binding {} to {} with routing key {}", 
            PACIENTE_QUERY_QUEUE, SAGA_EXCHANGE, PACIENTE_QUERY_ROUTING_KEY);
        return BindingBuilder.bind(pacienteQueryQueue())
                .to(sagaExchange())
                .with(PACIENTE_QUERY_ROUTING_KEY);
    }

    @Bean
    public Binding tecnicoQueryBinding() {
        logger.info("Binding {} to {} with routing key {}", 
            TECNICO_QUERY_QUEUE, SAGA_EXCHANGE, TECNICO_QUERY_ROUTING_KEY);
        return BindingBuilder.bind(tecnicoQueryQueue())
                .to(sagaExchange())
                .with(TECNICO_QUERY_ROUTING_KEY);
    }
    @Bean
    public Binding pacienteResponseBinding() {
        return BindingBuilder.bind(pacienteResponseQueue())
                .to(sagaExchange())
                .with(PACIENTE_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding tecnicoResponseBinding() {
        return BindingBuilder.bind(tecnicoResponseQueue())
                .to(sagaExchange())
                .with(TECNICO_RESPONSE_ROUTING_KEY);
    }
   

    @Bean
    public Binding pacienteErrorBinding() {
        return BindingBuilder.bind(pacienteErrorQueue())
                .to(sagaExchange())
                .with(PACIENTE_RESPONSE_QUEUE_ERROR);
    }

    @Bean
    public Binding tecnicoErrorBinding() {
        return BindingBuilder.bind(tecnicoErrorQueue())
                .to(sagaExchange())
                .with(TECNICO_RESPONSE_QUEUE_ERROR);
    }
    @Bean
    public Binding authPacienteQueryBinding() {
        return BindingBuilder.bind(authPacienteQueryQueue())
                .to(sagaExchange())
                .with(AUTH_PACIENTE_QUERY_ROUTING_KEY);
    }

    @Bean
    public Binding authTecnicoQueryBinding() {
        return BindingBuilder.bind(authTecnicoQueryQueue())
                .to(sagaExchange())
                .with(AUTH_TECNICO_QUERY_ROUTING_KEY);
    }

    @Bean
    public Binding authPacienteResponseBinding() {
        return BindingBuilder.bind(authPacienteResponseQueue())
                .to(sagaExchange())
                .with(AUTH_PACIENTE_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding authTecnicoResponseBinding() {
        return BindingBuilder.bind(authTecnicoResponseQueue())
                .to(sagaExchange())
                .with(AUTH_TECNICO_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding authPacienteErrorBinding() {
        return BindingBuilder.bind(authPacienteErrorQueue())
                .to(sagaExchange())
                .with(AUTH_PACIENTE_ERROR_ROUTING_KEY);
    }

    @Bean
    public Binding authTecnicoErrorBinding() {
        return BindingBuilder.bind(authTecnicoErrorQueue())
                .to(sagaExchange())
                .with(AUTH_TECNICO_ERROR_ROUTING_KEY);
    }
    @PostConstruct
    public void logConfiguration() {
        logger.info("SAGA Exchange Configuration:");
        logger.info("Exchange: {}", SAGA_EXCHANGE);
        logger.info("Paciente Query: Queue={}, RoutingKey={}", 
            PACIENTE_QUERY_QUEUE, PACIENTE_QUERY_ROUTING_KEY);
        logger.info("Tecnico Query: Queue={}, RoutingKey={}", 
            TECNICO_QUERY_QUEUE, TECNICO_QUERY_ROUTING_KEY);
    }
    
}