package br.ufpr.tcc.MSAuth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
    
    @Bean
    public IndexResolver mongoIndexResolver(MongoMappingContext mongoMappingContext) {
        return new MongoPersistentEntityIndexResolver(mongoMappingContext);
    }
}