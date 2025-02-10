package br.ufpr.tcc.MSForms.auth;

public record LoginResponse(boolean success, Object user, String tipo, String token, String message) {}
