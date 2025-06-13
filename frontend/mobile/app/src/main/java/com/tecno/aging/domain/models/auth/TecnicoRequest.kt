package com.tecno.aging.domain.models.auth

import com.tecno.aging.domain.models.pessoa.Endereco

data class TecnicoRequest(
    val matricula: String,
    val senha: String,
    val ativo: Boolean = true,
    val nome: String,
    val sexo: String,
    val endereco: Endereco?,
    val dataNasc: String,
    val cpf: String,
    val telefone: String
)