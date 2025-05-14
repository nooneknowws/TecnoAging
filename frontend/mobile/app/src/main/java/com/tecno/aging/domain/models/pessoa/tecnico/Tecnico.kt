package com.tecno.aging.domain.models.pessoa.tecnico

import com.tecno.aging.domain.models.pessoa.Endereco

data class Tecnico(
    var matricula: String = "",
    var nome: String = "",
    var cpf: String = "",
    var telefone: String = "",
    var sexo: String = "",
    var dataNasc: String = "",
    var endereco: Endereco = Endereco()
)