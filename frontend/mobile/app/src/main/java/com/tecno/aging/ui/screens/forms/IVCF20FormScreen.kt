@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Modelo de dados --------------------------------------------------------

enum class QuestionType { RADIO, CHECKBOX }

data class Pergunta(
    val texto: String,
    val tipo: QuestionType,
    val opcoes: List<String>
)

data class Etapa(
    val titulo: String,
    val descricao: String,
    val perguntas: List<Pergunta>
)

private val ivcf20Form = listOf(
    Etapa(
        titulo = "Auto-percepção da Saúde",
        descricao = "Avaliação geral da saúde comparada a outras pessoas da mesma idade.",
        perguntas = listOf(
            Pergunta(
                texto = "Em geral, comparado com outras pessoas da sua idade, como você diria que está a sua saúde?",
                tipo = QuestionType.RADIO,
                opcoes = listOf(
                    "Excelente, boa ou muito boa",
                    "Regular ou ruim"
                )
            )
        )
    ),
    Etapa(
        titulo = "Percepção da saúde comparada",
        descricao = "Avaliação geral da saúde comparada historicamente.",
        perguntas = listOf(
            Pergunta(
                texto = "Comparada há um ano atrás, como você se classificaria sua saúde em geral, agora?",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Melhor", "Pior", "Igual")
            )
        )
    ),
    Etapa(
        titulo = "Atividades de Vida Diária",
        descricao = "Avaliação de atividades como compras, finanças, e pequenos trabalhos domésticos.",
        perguntas = listOf(
            Pergunta(
                texto = "Você deixou de fazer compras por causa da sua saúde ou condição física?",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não ou não faz compras por outros motivos que não a saúde")
            ),
            Pergunta(
                texto = "Você deixou de controlar seu dinheiro ou os gastos da casa por causa da sua saúde ou condição física?",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não ou não controla o dinheiro por outros motivos que não a saúde")
            ),
            Pergunta(
                texto = "Você deixou de realizar pequenos trabalhos domésticos (limpeza leve, arrumar a casa, lavar louças) por causa da sua saúde ou condição física?",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não ou não faz trabalhos domésticos por outros motivos que não a saúde")
            ),
            Pergunta(
                texto = "Você deixou de tomar banho sozinho por causa da sua saúde ou condição física?",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não")
            )
        )
    ),
    Etapa(
        titulo = "Cognição",
        descricao = "Avaliação de possíveis sinais de problemas de memória.",
        perguntas = listOf(
            Pergunta("Algum familiar ou amigo falou que você está ficando esquecido?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta("Este esquecimento está piorando nos últimos meses?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta("Este esquecimento está impedindo a realização de alguma atividade do cotidiano?", QuestionType.RADIO, listOf("Sim", "Não"))
        )
    ),
    Etapa(
        titulo = "Humor",
        descricao = "Avaliação de sintomas relacionados ao humor.",
        perguntas = listOf(
            Pergunta("No último mês, você ficou com desânimo, tristeza ou desesperança?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta("No último mês, você perdeu o interesse ou prazer em atividades anteriormente prazerosas?", QuestionType.RADIO, listOf("Sim", "Não"))
        )
    ),
    Etapa(
        titulo = "Mobilidade",
        descricao = "Avaliação de mobilidade e capacidade física.",
        perguntas = listOf(
            Pergunta("Você é incapaz de elevar os braços acima do nível do ombro?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta("Você é incapaz de segurar pequenos objetos?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta(
                texto = "Você tem alguma das quatro condições abaixo relacionadas?",
                tipo = QuestionType.CHECKBOX,
                opcoes = listOf(
                    "Perda de peso não intencional de 4,5 kg ou 5% do peso corporal no último ano ou 6 kg nos últimos 6 meses ou 3 kg no último mês",
                    "Índice de Massa Corporal (IMC) menor que 22 kg/m²",
                    "Circunferência da panturrilha < 31 cm",
                    "Tempo gasto no teste de velocidade de marcha (4 m) > 5 segundos"
                )
            ),
            Pergunta("Você tem dificuldade para caminhar capaz de impedir a realização de alguma atividade do cotidiano?", QuestionType.RADIO, listOf("Sim", "Não")),
            Pergunta("Você caiu no último ano? Quantas vezes?", QuestionType.RADIO, listOf("Não", "1 vez", "2 vezes", "3 vezes ou mais")),
            Pergunta("Você perde urina ou fezes, sem querer, em algum momento?", QuestionType.RADIO, listOf("Sim", "Não"))
        )
    ),
    Etapa(
        titulo = "Comunicação",
        descricao = "Avaliação de problemas de comunicação relacionados à saúde.",
        perguntas = listOf(
            Pergunta(
                texto = "Você tem problemas de visão capazes de impedir a realização de alguma atividade do cotidiano? É permitido o uso de óculos ou lentes de contato.",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não")
            ),
            Pergunta(
                texto = "Você tem problemas de audição que impedem a realização de atividades do cotidiano? É permitido o uso de aparelhos de audição.",
                tipo = QuestionType.RADIO,
                opcoes = listOf("Sim", "Não")
            ),
            Pergunta(
                texto = "Você tem alguma das três condições abaixo relacionadas?",
                tipo = QuestionType.CHECKBOX,
                opcoes = listOf(
                    "Cinco ou mais doenças crônicas",
                    "Uso regular de cinco ou mais medicamentos diferentes",
                    "Internação recente, nos últimos 6 meses"
                )
            )
        )
    )
)

class ResponseState(
    val pergunta: Pergunta,
    initialRadio: Int? = null,
    initialCheckboxes: List<Boolean> = emptyList()
) {
    var selectedRadio by mutableStateOf(initialRadio)
    val selectedCheckboxes = mutableStateListOf<Boolean>().apply {
        if (initialCheckboxes.isNotEmpty()) addAll(initialCheckboxes)
        else repeat(pergunta.opcoes.size) { add(false) }
    }
}

// --- Composable ------------------------------------------------------------

@Composable
fun IVCF20FormScreen(
    onSubmit: (List<ResponseState>) -> Unit = {}
) {
    val etapas = remember { ivcf20Form }
    val respostas = remember { etapas.flatMap { it.perguntas }.map { ResponseState(it) } }
    var currentStep by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Índice de Vulnerabilidade Clínico‑Funcional (IVCF‑20)") }
            )
        },
        bottomBar = {
            val etapa = etapas[currentStep]
            val startIndex = etapas.take(currentStep).sumOf { it.perguntas.size }
            val stepResponses = respostas.subList(startIndex, startIndex + etapa.perguntas.size)
            val allAnswered = stepResponses.all { resp ->
                if (resp.pergunta.tipo == QuestionType.RADIO) resp.selectedRadio != null
                else resp.selectedCheckboxes.any { it }
            }
            Button(
                onClick = {
                    if (currentStep < etapas.lastIndex) currentStep++
                    else onSubmit(respostas)
                },
                enabled = allAnswered,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (currentStep < etapas.lastIndex) "Próxima" else "Enviar")
            }
        }
    ) { padding ->
        val etapa = etapas[currentStep]
        val startIndex = etapas.take(currentStep).sumOf { it.perguntas.size }
        val stepResponses = respostas.subList(startIndex, startIndex + etapa.perguntas.size)

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(etapa.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(etapa.descricao, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                }
            }
            itemsIndexed(etapa.perguntas) { idx, pergunta ->
                val respState = stepResponses[idx]
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("${idx + 1}. ${pergunta.texto}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    if (pergunta.tipo == QuestionType.RADIO) {
                        Column {
                            pergunta.opcoes.forEachIndexed { optIdx, opt ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = respState.selectedRadio == optIdx,
                                        onClick = { respState.selectedRadio = optIdx }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(opt)
                                }
                            }
                        }
                    } else {
                        Column {
                            pergunta.opcoes.forEachIndexed { optIdx, opt ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Checkbox(
                                        checked = respState.selectedCheckboxes[optIdx],
                                        onCheckedChange = { checked ->
                                            respState.selectedCheckboxes[optIdx] = checked
                                        }
                                    )
                                    Text(opt)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIVCF20Form() {
    MaterialTheme {
        IVCF20FormScreen()
    }
}
