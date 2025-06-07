package com.tecno.aging.domain.models.pessoa

open class Pessoa(
    open val nome: String = "",
    open val cpf: String = "",
    open val telefone: String = "",
    open val sexo: String = "",
    open val dataNasc: String = "",
    open val senha: String = "",
    open val endereco: Endereco = Endereco(),
    open val idade: Int
)