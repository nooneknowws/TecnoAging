package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.data.forms.meem.MeemResponse
import com.tecno.aging.data.forms.meem.meemForm
import com.tecno.aging.domain.models.forms.meem.MeemQuestionType
import com.tecno.aging.ui.components.buttons.ArrowLeftComponent
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.buttons.ButtonVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeemFormScreen(
    navController: NavController,
    onSubmit: (List<MeemResponse>) -> Unit = {}
) {
    val etapas = remember { meemForm }

    val respostas = remember {
        etapas.flatMap { etapa ->
            etapa.perguntas.map { pergunta ->
                MeemResponse(pergunta).apply {
                    if (pergunta.tipo == MeemQuestionType.CHECKBOX) {
                        repeat(pergunta.opcoes.size) { selectedCheckboxes.add(false) }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mini Exame do Estado Mental (MEEM)") },
                navigationIcon = { ArrowLeftComponent(navController) }
            )
        },
        bottomBar = {
            ButtonComponent(
                title = "Enviar",
                variant = ButtonVariant.Primary,
                onClick = { onSubmit(respostas) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            etapas.forEachIndexed { etapaIndex, etapa ->
                item {
                    Column {
                        Text(etapa.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(etapa.descricao, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                itemsIndexed(etapa.perguntas) { perguntaIndex, pergunta ->
                    val globalIndex = etapas
                        .take(etapaIndex)
                        .sumOf { it.perguntas.size } + perguntaIndex
                    val resp = respostas[globalIndex]

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(pergunta.texto, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))

                            when (pergunta.tipo) {
                                MeemQuestionType.RADIO -> pergunta.opcoes.forEachIndexed { idx, opt ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { resp.selectedRadio = idx }
                                            .padding(vertical = 4.dp)
                                    ) {
                                        RadioButton(
                                            selected = resp.selectedRadio == idx,
                                            onClick = { resp.selectedRadio = idx }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(opt)
                                    }
                                }
                                MeemQuestionType.CHECKBOX -> pergunta.opcoes.forEachIndexed { idx, opt ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                resp.selectedCheckboxes[idx] = !resp.selectedCheckboxes[idx]
                                            }
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Checkbox(
                                            checked = resp.selectedCheckboxes[idx],
                                            onCheckedChange = { resp.selectedCheckboxes[idx] = it }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(opt)
                                    }
                                }
                                MeemQuestionType.RANGE -> {
                                    var sliderPos by remember {
                                        mutableStateOf((resp.selectedRange ?: pergunta.min).toFloat())
                                    }
                                    Text(
                                        text = "${resp.selectedRange ?: pergunta.min}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Slider(
                                        value = sliderPos,
                                        onValueChange = {
                                            sliderPos = it
                                            resp.selectedRange = it.toInt()
                                        },
                                        valueRange = pergunta.min.toFloat()..pergunta.max.toFloat(),
                                        steps = pergunta.max - pergunta.min - 1,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
