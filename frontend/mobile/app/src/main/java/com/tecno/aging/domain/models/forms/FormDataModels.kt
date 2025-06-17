package com.tecno.aging.domain.models.forms

import com.google.gson.annotations.SerializedName

data class GenericForm(
    val tipo: String,
    val titulo: String,
    val descricao: String,
    val etapas: List<FormStep>
)

data class FormStep(
    val titulo: String,
    val descricao: String,
    val perguntas: List<FormQuestion>
)

data class FormQuestion(
    val texto: String,
    val tipo: String,
    val opcoes: List<String>?,
    val validacao: Validation
)

data class Validation(
    val min: Int?,
    val max: Int?,
    val required: Boolean
)