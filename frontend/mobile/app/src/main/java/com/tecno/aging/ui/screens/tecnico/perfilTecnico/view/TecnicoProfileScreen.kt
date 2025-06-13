package com.tecno.aging.ui.screens.tecnico.perfilTecnico.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun TecnicoProfileScreen(
    navController: NavController,
    viewModel: TecnicoProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Meu Perfil") })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = "Erro: ${uiState.error}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                uiState.tecnico != null -> {
                    val profile = uiState.tecnico!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_person), // Placeholder
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("tecnico_profile_edit") },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Editar Perfil")
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Dados Pessoais", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                DataRow(label = "Matrícula", value = profile.matricula)
                                DataRow(label = "Nome", value = profile.nome)
                                DataRow(label = "CPF", value = profile.cpf)
                                DataRow(label = "Telefone", value = profile.telefone)
                                DataRow(label = "Sexo", value = profile.sexo)
                                DataRow(label = "Data de Nascimento", value = profile.dataNascimento)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Endereço", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                DataRow(label = "CEP", value = profile.endereco?.cep)
                                DataRow(label = "Logradouro", value = profile.endereco?.logradouro)
                                DataRow(label = "Número", value = profile.endereco?.numero?.toString())
                                DataRow(label = "Complemento", value = profile.endereco?.complemento)
                                DataRow(label = "Bairro", value = profile.endereco?.bairro)
                                DataRow(label = "Município", value = profile.endereco?.municipio)
                                DataRow(label = "UF", value = profile.endereco?.uf)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DataRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.width(140.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value ?: "Não informado",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}