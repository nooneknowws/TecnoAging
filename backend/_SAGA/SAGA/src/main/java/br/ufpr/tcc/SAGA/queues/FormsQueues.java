package br.ufpr.tcc.SAGA.queues;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FormsQueues {
	
    private static final long QUEUE_TTL = 60 * 1000;
    // FILAS
    
    @Bean
    Queue avaliacaoCriadaRequestQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", QUEUE_TTL);
        return new Queue("avaliacao.criacao.request", true, false, false, args);
    }

    @Bean
    Queue avaliacaoCriadaResponseQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", QUEUE_TTL);
        return new Queue("avaliacao.criacao.response", true, false, false, args);
    }


}
