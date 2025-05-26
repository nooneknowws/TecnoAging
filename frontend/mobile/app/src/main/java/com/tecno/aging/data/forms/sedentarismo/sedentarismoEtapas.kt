package com.tecno.aging.data.forms.sedentarismo

import com.tecno.aging.domain.models.forms.sedentarismo.EtapaSedentarismo
import com.tecno.aging.domain.models.forms.sedentarismo.PerguntaSedentarismo

val sedentarismoEtapas = listOf(
    EtapaSedentarismo(
        titulo = "Atividade Moderada",
        descricao = "Você consegue realizá-la conversando com dificuldade enquanto se movimenta e não vai conseguir cantar.",
        perguntas = listOf(
            PerguntaSedentarismo("Quanto tempo por dia você realiza atividades moderadas? (HH:MM)", "tempo"),
            PerguntaSedentarismo("Quantos dias por semana?", "numero")
        )
    ),
    EtapaSedentarismo(
        titulo = "Atividade Vigorosa",
        descricao = "Você não vai conseguir nem conversar. A sua respiração vai ser muito mais rápida que o normal e os batimentos do seu coração vão aumentar muito.",
        perguntas = listOf(
            PerguntaSedentarismo("Quanto tempo por dia você realiza atividades vigorosas? (HH:MM)", "tempo"),
            PerguntaSedentarismo("Quantos dias por semana?", "numero")
        )
    ),
    EtapaSedentarismo(
        titulo = "Comportamento Sedentário",
        descricao = "Quanto tempo do seu dia, enquanto você está acordado, você gasta sentado, reclinado ou deitado...",
        perguntas = listOf(
            PerguntaSedentarismo("Quanto tempo por dia? (HH:MM)", "tempo"),
            PerguntaSedentarismo("Quantos dias por semana?", "numero")
        )
    )
)