package com.tecno.aging.domain.models.DTO

data class CepResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)