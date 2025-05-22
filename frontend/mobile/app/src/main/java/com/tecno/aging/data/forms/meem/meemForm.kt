package com.tecno.aging.data.forms.meem

import com.tecno.aging.domain.models.forms.meem.MeemEtapa
import com.tecno.aging.domain.models.forms.meem.MeemPergunta
import com.tecno.aging.domain.models.forms.meem.MeemQuestionType

val meemForm = listOf(
    MeemEtapa(
        titulo = "Orientação Temporal",
        descricao = "Será calculado 1 ponto para cada resposta correta.",
        perguntas = List(5) { index ->
            MeemPergunta(
                texto = when(index) {
                    0 -> "Que dia é hoje?"
                    1 -> "Em que mês estamos?"
                    2 -> "Em que ano estamos?"
                    3 -> "Em que dia da semana estamos?"
                    else -> "Qual a hora atual aproximada (±1h)?"
                },
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        }
    ),
    MeemEtapa(
        titulo = "Orientação Espacial",
        descricao = "Será calculado 1 ponto para cada resposta correta.",
        perguntas = List(5) { index ->
            MeemPergunta(
                texto = when(index) {
                    0 -> "Em que local nós estamos?"
                    1 -> "Que local é este aqui?"
                    2 -> "Em que bairro estamos?"
                    3 -> "Em que cidade estamos?"
                    else -> "Em que estado estamos?"
                },
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        }
    ),
    MeemEtapa(
        titulo = "Memória Imediata",
        descricao = "Repita as três palavras: CARRO, VASO, TIJOLO.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Marque cada palavra repetida corretamente:",
                tipo = MeemQuestionType.CHECKBOX,
                opcoes = listOf("CARRO", "VASO", "TIJOLO")
            )
        )
    ),
    MeemEtapa(
        titulo = "Cálculo",
        descricao = "Subtraia 7 sucessivas vezes (100-7;93-7; ...). Considere autocorreção.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Pontos (0 a 6) para resultados corretos:",
                tipo = MeemQuestionType.RANGE,
                min = 0,
                max = 6
            )
        )
    ),
    MeemEtapa(
        titulo = "Evocação das palavras",
        descricao = "Quais das palavras repetidas anteriormente você lembra?",
        perguntas = listOf(
            MeemPergunta(
                texto = "Marque cada palavra evocada corretamente:",
                tipo = MeemQuestionType.CHECKBOX,
                opcoes = listOf("CARRO", "VASO", "TIJOLO")
            )
        )
    ),
    MeemEtapa(
        titulo = "Nomeação",
        descricao = "Nomeie os objetos mostrados (relógio, caneta).",
        perguntas = listOf(
            MeemPergunta(
                texto = "Marque cada objeto nomeado corretamente:",
                tipo = MeemQuestionType.CHECKBOX,
                opcoes = listOf("Relógio", "Caneta")
            )
        )
    ),
    MeemEtapa(
        titulo = "Repetição",
        descricao = "Repita a frase: “NEM AQUI, NEM ALI, NEM LÁ”.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Resposta à repetição:",
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        )
    ),
    MeemEtapa(
        titulo = "Comando",
        descricao = "Pegue, dobre e coloque o papel sem ajuda.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Avalie cada ação:",
                tipo = MeemQuestionType.CHECKBOX,
                opcoes = listOf(
                    "Pegou o papel",
                    "Dobrou ao meio",
                    "Colocou no chão"
                )
            )
        )
    ),
    MeemEtapa(
        titulo = "Leitura e Obediência",
        descricao = "Faça o que a frase ordena: “FECHE OS OLHOS”.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Resposta ao comando:",
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        )
    ),
    MeemEtapa(
        titulo = "Escrita",
        descricao = "Escreva uma frase com sentido.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Avalie a escrita:",
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        )
    ),
    MeemEtapa(
        titulo = "Cópia do Desenho",
        descricao = "Copie dois pentágonos interseccionados.",
        perguntas = listOf(
            MeemPergunta(
                texto = "Avalie o desenho:",
                tipo = MeemQuestionType.RADIO,
                opcoes = listOf("Resposta correta", "Resposta incorreta")
            )
        )
    )
)

