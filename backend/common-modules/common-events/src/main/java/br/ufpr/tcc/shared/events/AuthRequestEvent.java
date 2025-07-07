package br.ufpr.tcc.shared.events; 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento de requisição de autenticação.
 * Este evento seria publicado por um serviço que precisa autenticar um usuário
 * (e.g., o API Gateway ou um serviço que precisa validar credenciais internamente).
 * Ele contém as informações necessárias para a tentativa de login.
 */
@Data // Gera getters, setters, toString(), equals() e hashCode()
@NoArgsConstructor // Gera construtor sem argumentos
@AllArgsConstructor // Gera construtor com todos os argumentos
public class AuthRequestEvent {
    private String cpf;
    private String senha;
    private String correlationId; // Para correlacionar requisições e respostas assíncronas
    private String requestSource; // Para identificar qual serviço fez a requisição (e.g., "MS_FORMS", "API_GATEWAY")
    private String userType; // Para indicar o tipo de usuário (e.g., "PACIENTE", "TECNICO")
}