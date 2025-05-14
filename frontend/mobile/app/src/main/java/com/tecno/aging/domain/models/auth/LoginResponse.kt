package com.tecno.aging.domain.models.auth

data class LoginResponse(
    val Perfil: String? = null,
    val ID: String? = null,
    val Nome: String? = null,
    val message: String? = null,
    val token: String? = null,
    val success: Boolean
)