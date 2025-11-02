package com.tecno.aging.ui.screens.tecnico.listaDePacientes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.ui.theme.AppColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteListScreen(
    viewModel: PacienteListViewModel = viewModel(),
    onNavigateToProfile: (pacienteId: Int) -> Unit,
    navController: NavController,
    onNavigateToEditProfile: (pacienteId: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(navController.currentBackStackEntry) {
        val profileUpdated = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>("profile_updated")

        if (profileUpdated == true) {
            viewModel.refreshProfile()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("profile_updated", false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Pacientes",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::refreshProfile,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (uiState.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Erro ao carregar pacientes: ${uiState.errorMessage}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else if (uiState.pacientes.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nenhum paciente encontrado.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.pacientes) { paciente ->
                        PacienteCard(
                            paciente = paciente,
                            onViewClick = { onNavigateToProfile(paciente.id) },
                            onEditClick = { onNavigateToEditProfile(paciente.id) },
                            onHistoryClick = { navController.navigate("historico_avaliacoes/${paciente.id}") }
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
    onEditClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.White
        ),
        border = BorderStroke(1.dp, AppColors.Gray200),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Ícone de Paciente",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = paciente.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CPF: ${paciente.cpf}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Gray700
                )
                Text(
                    text = "Idade: ${paciente.idade} anos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Gray700
                )
            }

            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = AppColors.Gray500)
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier.background(AppColors.White)
                ) {
                    DropdownMenuItem(text = { Text("Ver Perfil") }, onClick = {
                        onViewClick()
                        menuExpanded = false
                    })
                    DropdownMenuItem(text = { Text("Editar Perfil") }, onClick = {
                        onEditClick()
                        menuExpanded = false
                    })
                    DropdownMenuItem(text = { Text("Histórico") }, onClick = {
                        onHistoryClick()
                        menuExpanded = false
                    })
                }
            }
        }
    }
}