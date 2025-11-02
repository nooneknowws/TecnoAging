package com.tecno.aging.domain.models.pessoa.tecnico

import com.tecno.aging.domain.models.pessoa.Endereco

data class Tecnico(
    val id: Long = 0L,
    val cpf: String = "",
    val nome: String = "",
    val sexo: String = "",
    val dataNasc: String = "",
    val telefone: String = "",
    val matricula: String = "",
    val ativo: Boolean = false,
    val idade: Int = 0,
    val fotoPerfil: String? = null,
    val endereco: Endereco = Endereco(),
)