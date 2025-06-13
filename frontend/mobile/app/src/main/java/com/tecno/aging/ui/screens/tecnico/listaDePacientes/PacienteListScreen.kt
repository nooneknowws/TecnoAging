package com.tecno.aging.ui.screens.tecnico.listaDePacientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecno.aging.domain.models.paciente.Paciente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteListScreen(
    viewModel: PacienteListViewModel = viewModel(),
    onNavigateToProfile: (pacienteId: Int) -> Unit,
    onNavigateToEditProfile: (pacienteId: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Lista de Pacientes") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Text(
                    text = "Erro ao carregar pacientes: ${uiState.errorMessage}",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.pacientes) { paciente ->
                        PacienteCard(
                            paciente = paciente,
                            onViewClick = { onNavigateToProfile(paciente.id) },
                            onEditClick = { /* onNavigateToEditProfile(paciente.id) */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PacienteCard(
    paciente: Paciente,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = paciente.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "CPF: ${paciente.cpf}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Idade: ${paciente.idade} anos",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opções")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(text = { Text("Ver Perfil") }, onClick = {
                        onViewClick()
                        menuExpanded = false
                    })
                    DropdownMenuItem(text = { Text("Editar Perfil") }, onClick = {
                        onEditClick()
                        menuExpanded = false
                    })
                }
            }
        }
    }
}