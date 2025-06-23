package com.tecno.aging.domain.models.forms

import com.google.gson.annotations.SerializedName

data class GenericForm(
    val id: Long,
    val titulo: String,
    val descricao: String,
    val tipo: String,
    val etapas: List<FormStep>
)

data class FormStep(
    val titulo: String,
    val descricao: String,
    val perguntas: List<FormQuestion>
)

data class FormQuestion(
    val id: Long,
    val texto: String,
    val tipo: String,
    val validacao: FormValidation,
    val opcoes: List<FormOption>?
)

data class FormOption(
    val texto: String,
    val valor: Int
)

data class FormValidation(
    val min: Int?,
    val max: Int?
)