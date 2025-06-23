package com.tecno.aging.ui.screens.pacientes.perfilPaciente.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacienteProfileScreen(
    navController: NavController,
    viewModel: PacienteProfileViewModel = viewModel(factory = PacienteProfileViewModel.Companion.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meu Perfil",
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
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = "Erro: ${uiState.errorMessage}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.paciente != null -> {
                    val paciente = uiState.paciente!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(100.dp).clip(CircleShape).align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_person), // Placeholder
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("patient_profile_edit/${paciente.id}") },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Editar Perfil")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Dados Pessoais", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                DataRow("Nome", paciente.nome)
                                DataRow("CPF", paciente.cpf)
                                DataRow("Idade", "${paciente.idade} anos")
                                DataRow("Data de Nasc.", paciente.dataNasc ?: "N/A")
                                DataRow("Sexo", paciente.sexo ?: "N/A")
                                DataRow("Telefone", paciente.telefone ?: "N/A")

                                Spacer(modifier = Modifier.height(16.dp))

                                Text("Dados Sócio-demográficos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                DataRow("RG", paciente.rg ?: "N/A")
                                DataRow("Cor/Raça", paciente.corRaca ?: "N/A")
                                DataRow("Estado Civil", paciente.estadoCivil ?: "N/A")
                                DataRow("Escolaridade", paciente.escolaridade ?: "N/A")
                                DataRow("Nacionalidade", paciente.nacionalidade ?: "N/A")
                                DataRow("Município Nasc.", paciente.municipioNasc ?: "N/A")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$label:",
            modifier = Modifier.width(140.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}