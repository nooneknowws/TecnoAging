package com.tecno.aging.ui.screens.pacientes.avaliacoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import com.tecno.aging.domain.models.historico.PerguntaValor
import com.tecno.aging.ui.theme.AppColors
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliacaoDetailScreen(
    navController: NavController,
    viewModel: AvaliacaoDetailViewModel = viewModel(factory = AvaliacaoDetailViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Avaliação") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        containerColor = AppColors.Gray50
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.error != null -> Text(
                    "Erro: ${uiState.error}",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    textAlign = TextAlign.Center
                )
                uiState.avaliacao != null -> {
                    val avaliacao = uiState.avaliacao!!
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        item {
                            HeaderCard(avaliacao)
                        }

                        item {
                            Text(
                                text = "Perguntas e Respostas",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        itemsIndexed(avaliacao.perguntasValores) { index, resposta ->
                            RespostaCard(
                                numeroPergunta = index + 1,
                                resposta = resposta
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun HeaderCard(avaliacao: HistoricoAvaliacao) {
    val formattedDate = remember(avaliacao.dataCriacao) {
        try {
            val odt = OffsetDateTime.parse(avaliacao.dataCriacao)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
            odt.format(formatter)
        } catch (e: Exception) { avaliacao.dataCriacao ?: "Não informada" }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        border = BorderStroke(1.dp, AppColors.Gray200)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = avaliacao.formularioTitulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary
            )
            Divider()
            InfoRow("Paciente:", avaliacao.pacienteNome)
            InfoRow("Aplicado por:", avaliacao.tecnicoNome)
            InfoRow("Data:", formattedDate)
            InfoRow("Pontuação:", "${avaliacao.pontuacaoTotal} / ${avaliacao.pontuacaoMaxima}")
        }
    }
}

@Composable
private fun RespostaCard(numeroPergunta: Int, resposta: PerguntaValor) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, AppColors.Gray200)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "$numeroPergunta.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = resposta.pergunta,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Dark
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = resposta.valor.replace("\"", ""),
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.Gray700
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.Gray700,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = AppColors.Dark
        )
    }
}