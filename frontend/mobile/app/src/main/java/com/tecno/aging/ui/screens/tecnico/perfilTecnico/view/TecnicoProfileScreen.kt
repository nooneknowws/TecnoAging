package com.tecno.aging.ui.screens.tecnico.perfilTecnico.view

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.tecno.aging.ui.theme.AppColors
import androidx.compose.runtime.LaunchedEffect
import coil.compose.AsyncImage
import androidx.compose.material3.pulltorefresh.PullToRefreshBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoProfileScreen(
    navController: NavController,
    viewModel: TecnicoProfileViewModel = viewModel()
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
                title = { Text("Meu Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            uiState.tecnico?.let { profile ->
                FloatingActionButton(
                    onClick = {
                        navController.navigate("tecnico_profile_edit/${profile.id}")
                    },
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
                uiState.error != null && uiState.tecnico == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("", modifier = Modifier.align(Alignment.Center))
                    }
                }

                uiState.tecnico != null -> {
                    val profile = uiState.tecnico!!
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
                            text = profile.nome,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Matrícula: ${profile.matricula}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = AppColors.Gray700
                        )
                        InfoCard(
                            title = "Dados Pessoais",
                            icon = Icons.Default.Person
                        ) {
                            DataRow("CPF", profile.cpf)
                            DataRow("Sexo", profile.sexo)
                            DataRow("Data de Nasc.", profile.dataNasc)
                            DataRow("Telefone", profile.telefone)
                        }
                        InfoCard(
                            title = "Endereço",
                            icon = Icons.Default.Home
                        ) {
                            DataRow("CEP", profile.endereco.cep)
                            DataRow("Logradouro", profile.endereco.logradouro)
                            DataRow("Número", profile.endereco.numero)
                            DataRow("Bairro", profile.endereco.bairro)
                            DataRow("Município", profile.endereco.municipio)
                            DataRow("UF", profile.endereco.uf)
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
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Column(content = content)
        }
    }
}

@Composable
private fun DataRow(label: String, value: String?) {
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
            text = value ?: "Não informado",
            modifier = Modifier.weight(0.6f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.Dark
        )
    }
}