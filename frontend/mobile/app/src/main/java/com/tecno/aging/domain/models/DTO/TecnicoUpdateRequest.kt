package com.tecno.aging.domain.models.DTO

import com.tecno.aging.domain.models.pessoa.Endereco

data class TecnicoUpdateRequest(
    val nome: String,
    val sexo: String,
    val dataNasc: String,
    val telefone: String,
    val endereco: Endereco,
    val matricula: String,
    val cpf: String,
    val idade: Int,
    val ativo: Boolean
)