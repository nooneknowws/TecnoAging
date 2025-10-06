package br.ufpr.tcc.MSAuth.dto; // Shared package

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthValidationResponse {
    private final boolean success;
    private final String errorMessage;
    private final String microservice;
    private final Long userId;
    private final String userName;
    private final String telefone;

    @JsonCreator
    public AuthValidationResponse(
        @JsonProperty("success") boolean success,
        @JsonProperty("errorMessage") String errorMessage,
        @JsonProperty("microservice") String microservice,
        @JsonProperty("userId") Long userId,
        @JsonProperty("userName") String userName,
        @JsonProperty("telefone") String telefone
    ) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.microservice = microservice;
        this.userId = userId;
        this.userName = userName;
        this.telefone = telefone;
    }

    public static AuthValidationResponse success(String microservice, Long userId, String userName) {
        return new AuthValidationResponse(true, null, microservice, userId, userName, null);
    }
    
    public static AuthValidationResponse success(String microservice, Long userId, String userName, String telefone) {
        return new AuthValidationResponse(true, null, microservice, userId, userName, telefone);
    }

    public static AuthValidationResponse error(String errorMessage, String microservice) {
        return new AuthValidationResponse(false, errorMessage, microservice, null, null, null);
    }

    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public String getMicroservice() { return microservice; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getTelefone() { return telefone; }
}