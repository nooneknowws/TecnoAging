package com.tecno.aging.ui.screens.forms

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
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
import com.tecno.aging.domain.models.forms.FormQuestion
import com.tecno.aging.domain.models.forms.FormStep
import com.tecno.aging.domain.models.forms.GenericForm
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavController,
    formViewModel: FormViewModel = viewModel(factory = FormViewModel.Factory)
) {
    val uiState by formViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.submissionSuccess) {
        if (uiState.submissionSuccess) {
            Toast.makeText(context, "Avaliação enviada com sucesso!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

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
                viewModel.submeterAvaliacao()
            }
        )
    }
}

@Composable
fun EtapaPage(
    etapa: FormStep,
    respostas: Map<Long, String>,
    onRespostaChanged: (perguntaId: Long, resposta: String) -> Unit
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
            val respostaAtual = respostas[pergunta.id] ?: ""

            when (pergunta.tipo) {
                "range" -> RangeQuestion(pergunta, respostaAtual) { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
                "numero" -> NumeroQuestion(pergunta, respostaAtual) { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
                "tempo" -> TempoQuestion(pergunta, respostaAtual) { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
                "radio" -> RadioQuestion(pergunta, respostaAtual) { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
                "checkbox" -> CheckboxQuestion(pergunta, respostaAtual) { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RangeQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
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
                    onValueChange = { onRespostaChanged(it.toString()) },
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
fun NumeroQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    OutlinedTextField(
        value = respostaAtual,
        onValueChange = { novoValor ->
            if (novoValor.isEmpty() || novoValor.all { it.isDigit() }) {
                val valorNumerico = novoValor.toLongOrNull()
                val max = pergunta.validacao.max?.toLong()
                if (valorNumerico == null || max == null || valorNumerico <= max) {
                    onRespostaChanged(novoValor)
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
fun TempoQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val calendar = Calendar.getInstance()
        val initialHour = if (respostaAtual.isNotBlank() && ":" in respostaAtual) respostaAtual.split(":")[0].toInt() else calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = if (respostaAtual.isNotBlank() && ":" in respostaAtual) respostaAtual.split(":")[1].toInt() else calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val tempoFormatado = String.format("%02d:%02d", hour, minute)
                onRespostaChanged(tempoFormatado)
                showDialog = false
            },
            initialHour,
            initialMinute,
            true
        )
        timePickerDialog.setOnDismissListener { showDialog = false }
        timePickerDialog.show()
    }
    Box(modifier = Modifier.clickable { showDialog = true }) {
        OutlinedTextField(
            value = respostaAtual,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text(pergunta.texto) },
            readOnly = true,
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            trailingIcon = { Icon(Icons.Default.Schedule, contentDescription = "Selecionar Tempo") }
        )
    }
}

@Composable
fun RadioQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pergunta.texto, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            pergunta.opcoes?.forEach { opcao ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (respostaAtual == opcao.texto),
                            onClick = { onRespostaChanged(opcao.texto) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (respostaAtual == opcao.texto),
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = opcao.texto)
                }
            }
        }
    }
}

@Composable
fun CheckboxQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
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
                            selected = (opcao.texto in respostasSelecionadas),
                            onClick = {
                                val novasRespostas = respostasSelecionadas.toMutableSet()
                                if (opcao.texto in novasRespostas) {
                                    novasRespostas.remove(opcao.texto)
                                } else {
                                    novasRespostas.add(opcao.texto)
                                }
                                onRespostaChanged(novasRespostas.joinToString(","))
                            }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = (opcao.texto in respostasSelecionadas),
                        onCheckedChange = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = opcao.texto)
                }
            }
        }
    }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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