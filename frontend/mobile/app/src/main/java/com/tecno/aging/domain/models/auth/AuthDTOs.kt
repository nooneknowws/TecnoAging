package com.tecno.aging.domain.models.auth

import com.google.gson.annotations.SerializedName

data class EnviarCodigoRequest(
    val cpf: String
)

data class EnviarCodigoResponse(
    val message: String,
    val codigo: String,
    val telefone: String
)

data class ResetPasswordDTO(
    @SerializedName("cpf") val cpf: String,
    @SerializedName("codigo") val codigo: String,
    @SerializedName("novaSenha") val novaSenha: String,
    @SerializedName("confirmarSenha") val confirmarSenha: String
)