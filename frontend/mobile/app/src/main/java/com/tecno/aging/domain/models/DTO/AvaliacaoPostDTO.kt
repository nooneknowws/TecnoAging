package com.tecno.aging.domain.models.DTO

import com.google.gson.annotations.SerializedName

data class AvaliacaoPostDTO(
    @SerializedName("pacienteId") val pacienteId: Long,
    @SerializedName("tecnicoId") val tecnicoId: Long,
    @SerializedName("formularioId") val formularioId: Long,
    @SerializedName("pontuacaoTotal") val pontuacaoTotal: Int = 0,
    @SerializedName("respostas") val respostas: List<RespostaPostDTO>
)

data class RespostaPostDTO(
    @SerializedName("perguntaId") val perguntaId: Long,
    @SerializedName("valor") val valor: String
)