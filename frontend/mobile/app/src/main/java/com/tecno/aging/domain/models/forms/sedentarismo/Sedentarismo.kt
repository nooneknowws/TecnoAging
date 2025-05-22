package com.tecno.aging.domain.models.forms.sedentarismo

data class PerguntaSedentarismo(
    val texto: String,
    val tipo: String
)

data class EtapaSedentarismo(
    val titulo: String,
    val descricao: String,
    val perguntas: List<PerguntaSedentarismo>
)

data class RespostaSedentarismo(
    val pergunta: String,
    val tipo: String = "",
    val respostaTempo: String = "",
    val respostaNumero: Int? = null
)
