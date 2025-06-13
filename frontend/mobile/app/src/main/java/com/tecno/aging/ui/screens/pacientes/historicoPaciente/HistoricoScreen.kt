package com.tecno.aging.ui.screens.pacientes.historicoPaciente

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    navController: NavController,
    // Usa a Factory que criamos no ViewModel para instanciá-lo corretamente
    viewModel: HistoricoViewModel = viewModel(factory = HistoricoViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Histórico de Avaliações") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                // 1. Estado de Carregamento
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                // 2. Estado de Erro
                Text(
                    text = "Erro ao carregar o histórico: ${uiState.error}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            } else if (uiState.avaliacoes.isEmpty()) {
                // 3. Estado de Sucesso, mas com lista vazia
                Text(
                    text = "Nenhuma avaliação encontrada para este paciente.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // 4. Estado de Sucesso com dados
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.avaliacoes) { avaliacao ->
                        HistoricoCard(
                            avaliacao = avaliacao,
                            onClick = {
                                // Futuramente, navegar para os detalhes da avaliação
                                // navController.navigate("avaliacao_detail/${avaliacao.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoricoCard(avaliacao: HistoricoAvaliacao, onClick: () -> Unit) {
    // Função para formatar a data para o padrão brasileiro (dd/MM/yyyy às HH:mm)
    val formattedDate = try {
        val odt = OffsetDateTime.parse(avaliacao.dataCriacao)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
        odt.format(formatter)
    } catch (e: Exception) {
        "Data inválida" // Fallback caso a data venha em formato inesperado
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            // 1. Nome do formulário
            Text(
                text = avaliacao.formularioTitulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 2. Pontuação
            Text(
                text = "Pontuação: ${avaliacao.pontuacaoTotal} / ${avaliacao.pontuacaoMaxima}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 3. Data de realização
            Text(
                text = "Data: $formattedDate",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            // 4. Nome do técnico
            Text(
                text = "Aplicado por: ${avaliacao.tecnicoNome}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}