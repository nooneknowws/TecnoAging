package com.tecno.aging.ui.screens.pacientes.historicoPaciente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import com.tecno.aging.ui.theme.AppColors
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
private fun getIconForFormulario(formularioTitulo: String): ImageVector {
    return when {
        formularioTitulo.contains("IVCF-20", ignoreCase = true) -> Icons.AutoMirrored.Filled.DirectionsRun
        formularioTitulo.contains("Sedentarismo", ignoreCase = true) -> Icons.Default.Chair
        formularioTitulo.contains("MEEM", ignoreCase = true) -> Icons.Default.Psychology
        formularioTitulo.contains("Pittsburgh", ignoreCase = true) -> Icons.Default.Bedtime
        else -> Icons.Default.Description
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    navController: NavController,
    viewModel: HistoricoViewModel = viewModel(factory = HistoricoViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Avaliações") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        containerColor = AppColors.Gray50
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.error != null -> Text(
                    text = "Erro ao carregar o histórico: ${uiState.error}",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                uiState.avaliacoes.isEmpty() -> Text(
                    "Nenhuma avaliação encontrada.",
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.avaliacoes,
                            key = { avaliacao -> avaliacao.id }
                        ) { avaliacao ->
                            HistoricoCard(
                                avaliacao = avaliacao,
                                onClick = {
                                    navController.navigate("avaliacao_detail/${avaliacao.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoricoCard(avaliacao: HistoricoAvaliacao, onClick: () -> Unit) {
    val formattedDate = try {
        avaliacao.dataCriacao?.let {
            val odt = OffsetDateTime.parse(it)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
            odt.format(formatter)
        } ?: "Data não informada"
    } catch (e: Exception) { "Data inválida" }

    val icon = getIconForFormulario(avaliacao.formularioTitulo)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        border = BorderStroke(1.dp, AppColors.Gray200)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Ícone do Formulário",
                modifier = Modifier.size(40.dp),
                tint = AppColors.Primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = avaliacao.formularioTitulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pontuação: ${avaliacao.pontuacaoTotal} / ${avaliacao.pontuacaoMaxima}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Dark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Data: $formattedDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Gray700
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aplicado por: ${avaliacao.tecnicoNome}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.Gray500
                )
            }
        }
    }
}