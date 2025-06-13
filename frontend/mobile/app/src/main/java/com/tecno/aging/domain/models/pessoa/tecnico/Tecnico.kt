package com.tecno.aging.domain.models.pessoa.tecnico

import com.google.gson.annotations.SerializedName
import com.tecno.aging.domain.models.pessoa.Endereco

data class Tecnico(
    val id: Long = 0L,
    val cpf: String = "",
    val nome: String = "",
    val sexo: String = "",
    val dataNascimento: String = "",
    val telefone: String = "",
    val matricula: String = "",
    val ativo: Boolean = false,
    val endereco: Endereco = Endereco()
)