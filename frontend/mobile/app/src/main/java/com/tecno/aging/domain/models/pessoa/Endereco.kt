package com.tecno.aging.domain.models.pessoa

data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val complemento: String? = "",
    val bairro: String = "",
    val municipio: String = "",
    val uf: String = ""
) {
    constructor() : this("", "", "", "", "", "", "")
}