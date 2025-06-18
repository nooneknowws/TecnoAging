package com.tecno.aging.ui.screens.forms

import android.app.TimePickerDialog
import android.icu.util.Calendar
import com.tecno.aging.domain.models.forms.GenericForm
import com.tecno.aging.domain.models.forms.FormQuestion
import com.tecno.aging.domain.models.forms.FormStep

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    formFileName: String,
    navController: NavController,
    formViewModel: FormViewModel = viewModel()
) {
    LaunchedEffect(formFileName) {
        formViewModel.carregarFormulario(formFileName)
    }

    val uiState by formViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.form?.titulo ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.form != null -> {
                FormContent(
                    modifier = Modifier.padding(paddingValues),
                    form = uiState.form!!,
                    uiState = uiState,
                    viewModel = formViewModel
                )
            }
        }
    }
}

@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    form: GenericForm,
    uiState: FormUiState,
    viewModel: FormViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = uiState.etapaAtual,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            }, label = "EtapaAnimation"
        ) { targetEtapaIndex ->
            val etapa = form.etapas[targetEtapaIndex]
            EtapaPage(
                etapa = etapa,
                etapaIndex = targetEtapaIndex,
                respostas = uiState.respostas,
                onRespostaChanged = viewModel::onRespostaChanged
            )
        }

        BottomNavigationBar(
            etapaAtualIndex = uiState.etapaAtual,
            totalEtapas = form.etapas.size,
            onAnteriorClick = { viewModel.etapaAnterior() },
            onProximoClick = { viewModel.proximaEtapa() },
            onFinalizarClick = {
                println("Respostas Finais: ${uiState.respostas}")
                // TODO: Adicionar lógica para salvar e navegar para a próxima tela
                // Ex: navController.navigate("tela_de_resultados") { popUpTo("home") }
            }
        )
    }
}

@Composable
fun EtapaPage(
    etapa: FormStep,
    etapaIndex: Int,
    respostas: Map<String, String>,
    onRespostaChanged: (String, Int, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = etapa.titulo, style = MaterialTheme.typography.headlineSmall)
        if (etapa.descricao.isNotBlank()) {
            Text(text = etapa.descricao, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        etapa.perguntas.forEach { pergunta ->
            val chaveResposta = "etapa_${etapaIndex}_pergunta_${pergunta.texto}"
            val respostaAtual = respostas[chaveResposta] ?: ""

            when (pergunta.tipo) {
                "range" -> RangeQuestion(
                    pergunta = pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { texto, novaResposta ->
                        onRespostaChanged(texto, etapaIndex, novaResposta)
                    }
                )
                "numero" -> NumeroQuestion(
                    pergunta = pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { texto, novaResposta ->
                        onRespostaChanged(texto, etapaIndex, novaResposta)
                    }
                )
                "tempo" -> TempoQuestion(
                    pergunta = pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { texto, novaResposta ->
                        onRespostaChanged(texto, etapaIndex, novaResposta)
                    }
                )
                "radio" -> RadioQuestion(
                    pergunta = pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { texto, novaResposta ->
                        onRespostaChanged(texto, etapaIndex, novaResposta)
                    }
                )
                "checkbox" -> CheckboxQuestion(
                    pergunta = pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { texto, novaResposta ->
                        onRespostaChanged(texto, etapaIndex, novaResposta)
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RangeQuestion(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String, String) -> Unit
) {
    val respostaFloat = respostaAtual.toFloatOrNull() ?: (pergunta.validacao.min?.toFloat() ?: 0f)
    val min = pergunta.validacao.min?.toFloat() ?: 0f
    val max = pergunta.validacao.max?.toFloat() ?: 5f
    val steps = (max - min - 1).coerceAtLeast(0F).toInt()

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = pergunta.texto, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = respostaFloat,
                    onValueChange = { onRespostaChanged(pergunta.texto, it.toString()) },
                    valueRange = min..max,
                    steps = steps,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = respostaFloat.roundToInt().toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun NumeroQuestion(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String, String) -> Unit
) {
    OutlinedTextField(
        value = respostaAtual,
        onValueChange = { novoValor ->
            if (novoValor.isEmpty() || novoValor.all { it.isDigit() }) {
                val valorNumerico = novoValor.toLongOrNull()
                val max = pergunta.validacao.max?.toLong()
                if (valorNumerico == null || max == null || valorNumerico <= max) {
                    onRespostaChanged(pergunta.texto, novoValor)
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(pergunta.texto) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun TempoQuestion(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String, String) -> Unit
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        val calendar = Calendar.getInstance()
        val initialHour = if (respostaAtual.isNotBlank() && respostaAtual.contains(":")) respostaAtual.split(":")[0].toInt() else calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = if (respostaAtual.isNotBlank() && respostaAtual.contains(":")) respostaAtual.split(":")[1].toInt() else calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val tempoFormatado = String.format("%02d:%02d", hour, minute)
                onRespostaChanged(pergunta.texto, tempoFormatado)
            },
            initialHour,
            initialMinute,
            true
        )
        timePickerDialog.setOnDismissListener {
            showDialog.value = false
        }
        timePickerDialog.show()
    }

    OutlinedTextField(
        value = respostaAtual,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth().clickable { showDialog.value = true },
        label = { Text(pergunta.texto) },
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = "Selecionar Tempo") }
    )
}

@Composable
fun BottomNavigationBar(
    etapaAtualIndex: Int,
    totalEtapas: Int,
    onAnteriorClick: () -> Unit,
    onProximoClick: () -> Unit,
    onFinalizarClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onAnteriorClick,
            enabled = etapaAtualIndex > 0
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Etapa Anterior")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Anterior")
        }

        Text(
            text = "Etapa ${etapaAtualIndex + 1} de $totalEtapas",
            style = MaterialTheme.typography.bodyMedium
        )

        val isUltimaEtapa = etapaAtualIndex == totalEtapas - 1
        Button(
            onClick = if (isUltimaEtapa) onFinalizarClick else onProximoClick
        ) {
            Text(if (isUltimaEtapa) "Finalizar" else "Próximo")
            if (!isUltimaEtapa) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Próxima Etapa")
            }
        }
    }
}

@Composable
fun RadioQuestion(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String, String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pergunta.texto, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            pergunta.opcoes?.forEach { opcao ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (respostaAtual == opcao),
                            onClick = { onRespostaChanged(pergunta.texto, opcao) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (respostaAtual == opcao),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = opcao)
                }
            }
        }
    }
}

@Composable
fun CheckboxQuestion(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String, String) -> Unit
) {
    val respostasSelecionadas = remember(respostaAtual) {
        if (respostaAtual.isBlank()) emptySet() else respostaAtual.split(',').toSet()
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pergunta.texto, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            pergunta.opcoes?.forEach { opcao ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (opcao in respostasSelecionadas),
                            onClick = {
                                val novasRespostas = respostasSelecionadas.toMutableSet()
                                if (opcao in novasRespostas) {
                                    novasRespostas.remove(opcao)
                                } else {
                                    novasRespostas.add(opcao)
                                }
                                onRespostaChanged(pergunta.texto, novasRespostas.joinToString(","))
                            }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = (opcao in respostasSelecionadas),
                        onCheckedChange = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = opcao)
                }
            }
        }
    }
}