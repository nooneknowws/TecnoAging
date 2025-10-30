package com.tecno.aging.domain.models.historico

data class PerguntaValor(
    val pergunta: PerguntaDetalhe,
    val valor: String = ""
)

data class PerguntaDetalhe(
    val id: Long = 0L,
    val texto: String = ""
)