package com.tecno.aging.domain.models.forms.meem

enum class MeemQuestionType { RADIO, CHECKBOX, RANGE }

data class MeemPergunta(
    val texto: String,
    val tipo: MeemQuestionType,
    val opcoes: List<String> = emptyList(),
    val min: Int = 0,
    val max: Int = 0
)

data class MeemEtapa(
    val titulo: String,
    val descricao: String,
    val perguntas: List<MeemPergunta>
)