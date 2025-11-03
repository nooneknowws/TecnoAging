package com.tecno.aging.ui.screens.tecnico.editAvaliacao

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import com.tecno.aging.domain.models.historico.PerguntaHistorico
import com.tecno.aging.ui.screens.forms.CheckboxQuestion
import com.tecno.aging.ui.screens.forms.NumeroQuestion
import com.tecno.aging.ui.screens.forms.RadioQuestion
import com.tecno.aging.ui.screens.forms.RangeQuestion
import com.tecno.aging.ui.screens.forms.TempoQuestion
import com.tecno.aging.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliacaoEditScreen(
    navController: NavController,
    viewModel: AvaliacaoEditViewModel = viewModel(factory = AvaliacaoEditViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.submissionSuccess) {
        if (uiState.submissionSuccess) {
            Toast.makeText(context, "Avaliação atualizada com sucesso!", Toast.LENGTH_LONG).show()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("avaliacao_updated", true)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.avaliacao?.formularioTitulo ?: "Editar Avaliação") },
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
                Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Erro: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }
            uiState.avaliacao != null -> {
                EditFormContent(
                    modifier = Modifier.padding(paddingValues),
                    avaliacao = uiState.avaliacao!!,
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun EditFormContent(
    modifier: Modifier = Modifier,
    avaliacao: HistoricoAvaliacao,
    uiState: AvaliacaoEditUiState,
    viewModel: AvaliacaoEditViewModel
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).background(AppColors.Gray50),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column {
                    Text(text = avaliacao.formularioTitulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Editando respostas:", style = MaterialTheme.typography.bodyLarge, color = AppColors.Gray700)
                }
                Divider()
            }

            items(
                items = avaliacao.respostas,
                key = { it.pergunta.id }
            ) { respostaItem ->
                val respostaAtual = uiState.respostas[respostaItem.pergunta.id] ?: ""
                EditQuestionRenderer(
                    pergunta = respostaItem.pergunta,
                    respostaAtual = respostaAtual,
                    onRespostaChanged = { novaResposta ->
                        viewModel.onRespostaChanged(respostaItem.pergunta.id, novaResposta)
                    }
                )
            }
        }

        Surface(shadowElevation = 8.dp) {
            Button(
                onClick = viewModel::submitUpdate,
                enabled = !uiState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp)
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Salvar Alterações")
                }
            }
        }
    }
}

@Composable
fun EditQuestionRenderer(
    pergunta: PerguntaHistorico,
    respostaAtual: String,
    onRespostaChanged: (String) -> Unit
) {
    val useCard = pergunta.tipo != "numero" && pergunta.tipo != "tempo"

    val content = @Composable {
        when (pergunta.tipo) {
            "range" -> RangeQuestion(pergunta.toFormQuestion(), respostaAtual, onRespostaChanged)
            "numero" -> NumeroQuestion(pergunta.toFormQuestion(), respostaAtual, onRespostaChanged)
            "tempo" -> TempoQuestion(pergunta.toFormQuestion(), respostaAtual, onRespostaChanged)
            "radio" -> RadioQuestion(pergunta.toFormQuestion(), respostaAtual, onRespostaChanged)
            "checkbox" -> CheckboxQuestion(pergunta.toFormQuestion(), respostaAtual, onRespostaChanged)
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

private fun PerguntaHistorico.toFormQuestion(): com.tecno.aging.domain.models.forms.Pergunta {
    return com.tecno.aging.domain.models.forms.Pergunta(
        id = this.id,
        texto = this.texto,
        tipo = this.tipo,
        opcoes = this.opcoes,
        validacao = this.validacao?.let {
            com.tecno.aging.domain.models.forms.Validacao(
                min = it.min,
                max = it.max,
                required = false
            )
        }
    )
}