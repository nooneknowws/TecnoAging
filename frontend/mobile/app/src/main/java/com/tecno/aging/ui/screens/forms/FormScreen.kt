package com.tecno.aging.ui.screens.forms

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.tecno.aging.domain.models.forms.Pergunta as FormQuestion
import com.tecno.aging.domain.models.forms.Etapa as FormStep
import com.tecno.aging.domain.models.forms.GenericForm
import com.tecno.aging.ui.theme.AppColors
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
            val message = if (uiState.pageTitle.startsWith("Editar")) {
                "Avaliação atualizada com sucesso!"
            } else {
                "Avaliação enviada com sucesso!"
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("avaliacao_updated", true)

            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.form?.titulo ?: "Carregando...", style = MaterialTheme.typography.titleMedium) },
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
            onFinalizarClick = { viewModel.submitForm() },
            isSubmitting = uiState.isSubmitting
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
            .background(AppColors.Gray50)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(text = etapa.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (etapa.descricao.isNotBlank()) {
                Text(text = etapa.descricao, style = MaterialTheme.typography.bodyLarge, color = AppColors.Gray700)
            }
        }
        Divider()

        etapa.perguntas.forEach { pergunta ->
            val respostaAtual = respostas[pergunta.id] ?: ""
            QuestionRenderer(
                pergunta = pergunta,
                respostaAtual = respostaAtual,
                onRespostaChanged = { novaResposta -> onRespostaChanged(pergunta.id, novaResposta) }
            )
        }
    }
}

@Composable
fun QuestionRenderer(
    pergunta: FormQuestion,
    respostaAtual: String,
    onRespostaChanged: (String) -> Unit
) {
    val useCard = pergunta.tipo != "numero" && pergunta.tipo != "tempo"

    val content = @Composable {
        when (pergunta.tipo) {
            "range" -> RangeQuestion(pergunta, respostaAtual, onRespostaChanged)
            "numero" -> NumeroQuestion(pergunta, respostaAtual, onRespostaChanged)
            "tempo" -> TempoQuestion(pergunta, respostaAtual, onRespostaChanged)
            "radio" -> RadioQuestion(pergunta, respostaAtual, onRespostaChanged)
            "checkbox" -> CheckboxQuestion(pergunta, respostaAtual, onRespostaChanged)
            else -> Text("Tipo de pergunta '${pergunta.tipo}' não suportado.")
        }
    }

    if (useCard) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppColors.White),
            border = BorderStroke(1.dp, AppColors.Gray200)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = pergunta.texto, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }
        }
    } else {
        Column {
            Text(text = pergunta.texto, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun RangeQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    val min = pergunta.validacao?.min?.toFloat() ?: 0f
    val max = pergunta.validacao?.max?.toFloat() ?: 5f
    val respostaFloat = respostaAtual.toFloatOrNull() ?: min
    val steps = (max - min - 1).coerceAtLeast(0F).toInt()

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

@Composable
fun NumeroQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    OutlinedTextField(
        value = respostaAtual,
        onValueChange = { novoValor ->
            if (novoValor.isEmpty() || novoValor.all { it.isDigit() }) {
                val valorNumerico = novoValor.toLongOrNull()
                val max = pergunta.validacao?.max?.toLong()
                if (max == null || valorNumerico == null || valorNumerico <= max) {
                    onRespostaChanged(novoValor)
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Sua resposta") },
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
        val (initialHour, initialMinute) = if (respostaAtual.isNotBlank() && ":" in respostaAtual) {
            val parts = respostaAtual.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
        }

        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onRespostaChanged(String.format("%02d:%02d", hour, minute))
            },
            initialHour,
            initialMinute,
            true
        ).apply {
            setOnDismissListener { showDialog = false }
            show()
        }
    }

    Box(modifier = Modifier.clickable { showDialog = true }) {
        OutlinedTextField(
            value = respostaAtual,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("HH:MM") },
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
    val opcoes = pergunta.opcoes ?: emptyList()

    Column {
        opcoes.forEach { opcao ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (respostaAtual == opcao),
                        onClick = { onRespostaChanged(opcao) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (respostaAtual == opcao),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                // CORRIGIDO: Usa 'opcao' diretamente, pois é uma String.
                Text(text = opcao)
            }
        }
    }
}

@Composable
fun CheckboxQuestion(pergunta: FormQuestion, respostaAtual: String, onRespostaChanged: (String) -> Unit) {
    val respostasSelecionadas = remember(respostaAtual) {
        if (respostaAtual.isBlank()) emptySet() else respostaAtual.split(',').toSet()
    }
    val opcoes = pergunta.opcoes ?: emptyList()

    Column {
        opcoes.forEach { opcao ->
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
                            onRespostaChanged(novasRespostas.joinToString(","))
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
                // CORRIGIDO: Usa 'opcao' diretamente, pois é uma String.
                Text(text = opcao)
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
    onFinalizarClick: () -> Unit,
    isSubmitting: Boolean
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
            enabled = etapaAtualIndex > 0 && !isSubmitting
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
            onClick = if (isUltimaEtapa) onFinalizarClick else onProximoClick,
            enabled = !isSubmitting
        ) {
            if (isSubmitting && isUltimaEtapa) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
            } else {
                Text(if (isUltimaEtapa) "Finalizar" else "Próximo")
                if (!isUltimaEtapa) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Próxima Etapa")
                }
            }
        }
    }
}