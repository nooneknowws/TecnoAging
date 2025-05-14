package com.tecno.aging.domain.models.pessoa

data class Pessoa (
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val sexo: String = "",
    val dataNasc: String = "",
    val senha: String = "",
    val endereco: Endereco = Endereco(),
    val idade: String = ""
)
