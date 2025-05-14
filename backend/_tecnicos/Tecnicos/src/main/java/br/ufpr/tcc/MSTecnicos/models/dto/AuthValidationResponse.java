package br.ufpr.tcc.MSTecnicos.models.dto; // Shared package

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthValidationResponse {
    private final boolean success;
    private final String errorMessage;
    private final String microservice;
    private final Long userId;
    private final String userName;

    @JsonCreator
    public AuthValidationResponse(
        @JsonProperty("success") boolean success,
        @JsonProperty("errorMessage") String errorMessage,
        @JsonProperty("microservice") String microservice,
        @JsonProperty("userId") Long userId,
        @JsonProperty("userName") String userName
    ) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.microservice = microservice;
        this.userId = userId;
        this.userName = userName;
    }

    // Helper factory methods
    public static AuthValidationResponse success(String microservice, Long userId, String userName) {
        return new AuthValidationResponse(true, null, microservice, userId, userName);
    }

    public static AuthValidationResponse error(String errorMessage, String microservice) {
        return new AuthValidationResponse(false, errorMessage, microservice, null, null);
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public String getMicroservice() { return microservice; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
}