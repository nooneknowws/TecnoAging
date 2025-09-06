package br.ufpr.tcc.MSTecnicos.messaging;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.ufpr.tcc.MSTecnicos.models.Tecnico;
import br.ufpr.tcc.MSTecnicos.models.dto.AuthDTO;
import br.ufpr.tcc.MSTecnicos.models.dto.AuthValidationResponse;
import br.ufpr.tcc.MSTecnicos.repository.TecnicoRepository;

@Component
public class AuthTecnicoListener {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthTecnicoListener.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @RabbitListener(queues = "auth.query.tecnico.queue")
    public void handleAuthRequest(AuthDTO authDTO, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        logger.info("Auth request for CPF: {}", authDTO.getCpf());

        try {
            // Remove formatting characters from CPF before searching
            String cleanCpf = authDTO.getCpf().replaceAll("[^0-9]", "");
            logger.info("Original CPF: {}, Clean CPF: {}", authDTO.getCpf(), cleanCpf);
            Optional<Tecnico> tecnicoOpt = tecnicoRepository.findByCpfForAuth(cleanCpf);
            
            AuthValidationResponse response;
            
            if (tecnicoOpt.isPresent()) {
                Tecnico tecnico = tecnicoOpt.get();
                if (tecnico.verificarSenha(authDTO.getSenha())) {
                    response = AuthValidationResponse.success(
                        "TECNICO",
                        tecnico.getId(),
                        tecnico.getNome()
                    );
                } else {
                    response = AuthValidationResponse.error(
                        "Senha inválida", 
                        "TECNICO"
                    );
                }
            } else {
                response = AuthValidationResponse.error(
                    "CPF não encontrado", 
                    "TECNICO"
                );
            }
            

            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico",
                response,
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );

        } catch (Exception e) {
            logger.error("Auth error: {}", e.getMessage());
            rabbitTemplate.convertAndSend(
                "saga-exchange",
                "auth.response.tecnico.error",
                new AuthValidationResponse(false, "Erro interno", "TECNICO", null, correlationId),
                m -> {
                    m.getMessageProperties().setCorrelationId(correlationId);
                    return m;
                }
            );
        }
    }
}