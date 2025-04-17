@file:OptIn(ExperimentalMaterial3Api::class)
package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun pittsburghFatigabilityScreen(
    onSubmit: (List<FatigabilityResponse>) -> Unit = {}
) {
    val questions = remember { FatigabilityQuestion.values().toList() }
    var responses by rememberSaveable { mutableStateOf(List(questions.size) { FatigabilityResponse() }) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Formulário de Fatigabilidade de Pittsburgh") })
        },
        bottomBar = {
            val enabled = responses.flatMap { listOf(it.physical, it.mental) }.none { it == null }
            Button(
                onClick = { onSubmit(responses) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = enabled
            ) {
                Text("Enviar")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            itemsIndexed(questions) { index, question ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(question.title, style = MaterialTheme.typography.titleMedium)
                        question.subtitle?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(Modifier.height(12.dp))
                        RatingRow(
                            label = "Fadiga Física",
                            selected = responses[index].physical,
                            onSelect = { value ->
                                responses = responses.toMutableList().also {
                                    it[index] = it[index].copy(physical = value)
                                }
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        RatingRow(
                            label = "Fadiga Mental",
                            selected = responses[index].mental,
                            onSelect = { value ->
                                responses = responses.toMutableList().also {
                                    it[index] = it[index].copy(mental = value)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingRow(
    label: String,
    selected: Int?,
    onSelect: (Int) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            (0..5).forEach { value ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    RadioButton(
                        selected = selected == value,
                        onClick = { onSelect(value) }
                    )
                    Text(text = value.toString())
                }
            }
        }
    }
}

// ----- Data layer -----------------------------------------------------------

enum class FatigabilityQuestion(val title: String, val subtitle: String? = null) {
    A(
        "1. Caminhada tranquila por 30 minutos.",
        null
    ),
    C(
        "2. Atividade doméstica leve por 1 hora.",
        "(limpar, cozinhar, tirar o pó, assar, arrumar camas, lavar louça, regar plantas)"
    ),
    H(
        "3. Participar de atividade social por 1 hora.",
        "(festa, jantar, centro de idosos, reunião com família/amigos, jogar cartas)"
    ),
    J(
        "4. Atividade de alta intensidade por 30 minutos.",
        "(corrida, caminhada, ciclismo, natação, esportes com raquete, aparelhos aeróbicos, dança, zumba)"
    );
}

data class FatigabilityResponse(
    val physical: Int? = null,
    val mental: Int? = null
)

// ----- Preview --------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun previewPittsburghFatigability() {
    MaterialTheme {
        pittsburghFatigabilityScreen()
    }
}
