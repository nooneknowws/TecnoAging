package com.tecno.aging.domain.models.auth

data class LoginRequest(
    val cpf: String,
    val senha: String
)