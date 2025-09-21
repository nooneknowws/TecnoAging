package com.tecno.aging.domain.models.forms

import com.google.gson.annotations.SerializedName

data class GenericForm(
    @SerializedName("id") val id: Long,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("etapas") val etapas: List<Etapa>
)

data class Etapa(
    @SerializedName("id") val id: Long,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descricao") val descricao: String,
    @SerializedName("perguntas") val perguntas: List<Pergunta>
)

data class Pergunta(
    @SerializedName("id") val id: Long,
    @SerializedName("texto") val texto: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("opcoes") val opcoes: List<String>?,
    @SerializedName("validacao") val validacao: Validacao?
)

data class Validacao(
    @SerializedName("min") val min: Int?,
    @SerializedName("max") val max: Int?,
    @SerializedName("required") val required: Boolean?
)