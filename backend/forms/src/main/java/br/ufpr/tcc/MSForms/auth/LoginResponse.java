package br.ufpr.tcc.MSForms.auth;

import br.ufpr.tcc.MSForms.models.Pessoa;

public record LoginResponse(boolean success, Long id, Pessoa user, String tipo, String token, String message) {}
