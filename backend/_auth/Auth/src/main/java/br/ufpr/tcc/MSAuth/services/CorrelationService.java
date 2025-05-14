package br.ufpr.tcc.MSAuth.services;

import org.springframework.stereotype.Service;

import br.ufpr.tcc.MSAuth.dto.AuthValidationResponse;

import java.util.concurrent.*;

@Service
public class CorrelationService {
    private final ConcurrentMap<String, CompletableFuture<AuthValidationResponse>> pendingRequests = 
        new ConcurrentHashMap<>();

    public void addPendingRequest(String correlationId, CompletableFuture<AuthValidationResponse> future) {
        pendingRequests.put(correlationId, future);
    }

    public CompletableFuture<AuthValidationResponse> removePendingRequest(String correlationId) {
        return pendingRequests.remove(correlationId);
    }
}