package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.data.forms.sedentarismo.sedentarismoEtapas
import com.tecno.aging.domain.models.forms.sedentarismo.RespostaSedentarismo
import com.tecno.aging.ui.components.buttons.ArrowLeftComponent
import com.tecno.aging.ui.components.buttons.ButtonComponent


@Composable
fun SedentarismoFormScreen(
    navController: NavController,
    onSubmit: (List<RespostaSedentarismo>) -> Unit = {}
) {
    val etapas = sedentarismoEtapas
    val respostas = remember {
        etapas.flatMap { it.perguntas }.map {
            mutableStateOf(RespostaSedentarismo(it.texto))
        }
    }

    val allValid = respostas.all {
        val r = it.value
        when (r.tipo) {
            "tempo" -> r.respostaTempo.isNotBlank()
            "numero" -> r.respostaNumero?.let { it in 0..7 } == true
            else -> false
        }
    }

    Scaffold(
        bottomBar = {
            ButtonComponent(
                title = "Enviar",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { onSubmit(respostas.map { it.value }) },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            ArrowLeftComponent(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Nível de Atividade Física e Comportamento Sedentário",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            var responseIndex = 0
            etapas.forEach { etapa ->
                Text(etapa.titulo, style = MaterialTheme.typography.titleMedium)
                Text(etapa.descricao, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))

                etapa.perguntas.forEach { pergunta ->
                    val respostaState = respostas[responseIndex]
                    responseIndex++
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(pergunta.texto, style = MaterialTheme.typography.bodyMedium)

                        when (pergunta.tipo) {
                            "tempo" -> TextField(
                                value = respostaState.value.respostaTempo,
                                onValueChange = {
                                    respostaState.value =
                                        respostaState.value.copy(respostaTempo = it)
                                },
                                placeholder = { Text("HH:MM") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            "numero" -> TextField(
                                value = respostaState.value.respostaNumero?.toString() ?: "",
                                onValueChange = {
                                    if (it.length <= 1) { // limita a 1 dígito
                                        val num = it.toIntOrNull()
                                        respostaState.value =
                                            respostaState.value.copy(respostaNumero = num)
                                    }
                                },
                                placeholder = { Text("0 a 7") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}