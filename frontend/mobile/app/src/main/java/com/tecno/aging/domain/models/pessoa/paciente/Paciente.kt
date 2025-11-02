package com.tecno.aging.domain.models.pessoa.paciente

data class PacienteEndereco(
    val logradouro: String?,
    val numero: Int?,
    val bairro: String?,
    val municipio: String?,
    val uf: String?,
    val cep: String?,
    val complemento: String?
)

data class Paciente(
    val id: Int,
    val nome: String,
    val cpf: String,
    val idade: Int,
    val peso: Double?,
    val altura: Double?,
    val imc: Double?,
    val socioeconomico: String?,
    val escolaridade: String?,
    val estadoCivil: String?,
    val nacionalidade: String?,
    val municipioNasc: String?,
    val ufNasc: String?,
    val corRaca: String?,
    val rg: String?,
    val dataExpedicao: String?,
    val orgaoEmissor: String?,
    val ufEmissor: String?,
    val dataNasc: String?,
    val telefone: String?,
    val sexo: String?,
    val fotoPerfil: String? = null,
    val endereco: PacienteEndereco?
)