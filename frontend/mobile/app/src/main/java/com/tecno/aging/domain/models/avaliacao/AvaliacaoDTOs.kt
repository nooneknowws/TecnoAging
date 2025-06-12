package com.tecno.aging.domain.models.historico

import com.google.gson.annotations.SerializedName

data class PerguntaValor(
    @SerializedName("pergunta")
    val pergunta: String,

    @SerializedName("valor")
    val valor: String
)

data class HistoricoAvaliacao(
    @SerializedName("avaliacaoId")
    val id: Long,

    @SerializedName("pacienteId")
    val pacienteId: Long,

    @SerializedName("paciente")
    val pacienteNome: String,

    @SerializedName("pacienteIMC")
    val pacienteImc: Double?,

    @SerializedName("pacienteIDADE")
    val pacienteIdade: Int,

    @SerializedName("tecnicoId")
    val tecnicoId: Long,

    @SerializedName("tecnico")
    val tecnicoNome: String,

    @SerializedName("formulario")
    val formularioTitulo: String,

    @SerializedName("formularioDesc")
    val formularioDesc: String,

    @SerializedName("pontuacaoTotal")
    val pontuacaoTotal: Int,

    @SerializedName("pontuacaoMaxima")
    val pontuacaoMaxima: Int,

    @SerializedName("dataCriacao")
    val dataCriacao: String,

    @SerializedName("dataAtualizacao")
    val dataAtualizacao: String,

    @SerializedName("perguntasValores")
    val perguntasValores: List<PerguntaValor>
)