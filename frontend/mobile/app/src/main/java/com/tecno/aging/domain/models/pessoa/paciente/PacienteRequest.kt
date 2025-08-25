package com.tecno.aging.domain.models.pessoa.paciente

import com.tecno.aging.domain.models.pessoa.Endereco
import com.google.gson.annotations.SerializedName

data class Contato(
    val nome: String,
    val telefone: String,
    val parentesco: String
)

data class PacienteRequest(
    val nome: String,
    val cpf: String,
    val sexo: String,
    @SerializedName("dataNasc")
    val dataNascimento: String,
    val telefone: String,
    val senha: String,
    val endereco: Endereco,
    val peso: Double,
    val altura: Double,
    val socioeconomico: String,
    val escolaridade: String,
    val estadoCivil: String,
    val nacionalidade: String,
    val municipioNasc: String,
    val ufNasc: String,
    val corRaca: String,
    val rg: String,
    val dataExpedicao: String,
    val orgaoEmissor: String,
    val ufEmissor: String,
    val contatos: List<Contato>
)