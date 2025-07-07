package br.ufpr.tcc.shared.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento de resposta de autenticação.
 * Este evento seria publicado pelo serviço de autenticação (e.g., MS Auth)
 * após processar uma AuthRequestEvent. Ele contém o resultado da autenticação.
 */
@Data // Gera getters, setters, toString(), equals() e hashCode()
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os argumentos
public class AuthResponseEvent {
    private boolean success;
    private String errorMessage;
    private String microservice; // O microsserviço que gerou a resposta (e.g., "MS_AUTH", "MS_PACIENTES")
    private Long userId;
    private String userName;
    private String perfil; // Perfil do usuário (e.g., "PACIENTE", "TECNICO")
    private String token; // Token de autenticação (se a autenticação for bem-sucedida)
    private String correlationId; // Para correlacionar requisições e respostas assíncronas
}