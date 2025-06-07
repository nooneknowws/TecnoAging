package com.tecno.aging.domain.models.pessoa.tecnico

import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.Pessoa

data class Tecnico(

    override var idade: Int,

    var ativo: Boolean,
    var id: String = "",
    var matricula: String = "",
    override var nome: String = "",
    override var cpf: String = "",
    override var telefone: String = "",
    override var sexo: String = "",
    override var dataNasc: String = "",
    override var endereco: Endereco = Endereco(),
    override var senha: String = "",
) : Pessoa(nome, cpf, telefone, sexo, dataNasc, senha, endereco, idade)