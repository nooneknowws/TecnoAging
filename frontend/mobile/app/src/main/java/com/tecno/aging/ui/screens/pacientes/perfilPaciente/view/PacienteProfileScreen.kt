package com.tecno.aging.ui.screens.pacientes.perfilPaciente.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.theme.AppColors
import androidx.compose.runtime.LaunchedEffect
import coil.compose.AsyncImage
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteProfileScreen(
    navController: NavController,
    viewModel: PacienteProfileViewModel = viewModel(factory = PacienteProfileViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val loggedInUserId = SessionManager.getUserId()
    val paciente = uiState.paciente
    val isMyProfile = loggedInUserId == paciente?.id.toString()
    val screenTitle = if (isMyProfile) "Meu Perfil" else "Perfil de ${paciente?.nome ?: "..."}"

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
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            if (paciente != null) {
                FloatingActionButton(
                    onClick = { navController.navigate("patient_profile_edit/${paciente.id}") },
                    containerColor = AppColors.Primary
                ) {
                    Icon(Icons.Default.Edit, "Editar Perfil", tint = Color.White)
                }
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::refreshProfile,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState()

            when {
                uiState.errorMessage != null && uiState.paciente == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Erro ao carregar perfil",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = uiState.errorMessage ?: "Erro desconhecido",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColors.Gray700
                            )
                            Button(onClick = viewModel::refreshProfile) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
                paciente != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Exibe a foto de perfil
                        val fotoBitmap = uiState.fotoBitmap
                        if (fotoBitmap != null) {
                            Image(
                                bitmap = fotoBitmap,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(AppColors.Gray200, CircleShape)
                            )
                        } else {
                            // Fallback para ícone padrão
                            Image(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "Foto de perfil padrão",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(AppColors.Gray200, CircleShape)
                            )
                        }
                        Text(
                            text = paciente.nome,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "CPF: ${paciente.cpf}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppColors.Gray700
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            Button(onClick = { navController.navigate("historico_avaliacoes/${paciente.id}") }) {
                                Icon(Icons.Default.EventNote, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Ver Histórico")
                            }
                        }

                        InfoCard(
                            title = "Dados Pessoais",
                            icon = Icons.Default.Person
                        ) {
                            DataRow("Idade", "${paciente.idade} anos")
                            DataRow("Data de Nasc.", paciente.dataNasc ?: "N/A")
                            DataRow("Sexo", paciente.sexo ?: "N/A")
                            DataRow("Telefone", paciente.telefone ?: "N/A")
                        }

                        InfoCard(
                            title = "Dados Sócio-demográficos",
                            icon = Icons.Default.Info
                        ) {
                            DataRow("RG", paciente.rg ?: "N/A")
                            DataRow("Cor/Raça", paciente.corRaca ?: "N/A")
                            DataRow("Estado Civil", paciente.estadoCivil ?: "N/A")
                            DataRow("Escolaridade", paciente.escolaridade ?: "N/A")
                        }
                    }
                }

                uiState.isLoading -> {}

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Perfil não carregado.", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, AppColors.Gray200)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = title, tint = AppColors.Primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Column(content = content)
        }
    }
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(0.4f),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.Gray700
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.6f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.Dark
        )
    }
}