package com.tecno.aging.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    profileType: String,
    onEditClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil do Técnico") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    if (profileType == "tecnico") {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is ProfileViewModel.ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProfileViewModel.ProfileUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.refreshData() },
                    modifier = Modifier.padding(padding)
                )
            }

            is ProfileViewModel.ProfileUiState.Success -> {
                ProfileContent(
                    padding = padding,
                    profile = state.tecnico,
                    profileType = profileType
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    padding: PaddingValues,
    profile: Tecnico,
    profileType: String
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {

        }

        Spacer(modifier = Modifier.height(24.dp))

        // Personal Data Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dados Pessoais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileDataItem(label = "Matrícula", value = profile.matricula)
                ProfileDataItem(label = "Nome", value = profile.nome)
                ProfileDataItem(label = "CPF", value = formatCpf(profile.cpf))
                ProfileDataItem(label = "Telefone", value = formatPhone(profile.telefone))
                ProfileDataItem(label = "Sexo", value = profile.sexo)
                ProfileDataItem(label = "Data de Nascimento", value = formatDate(profile.dataNasc))

                if (profileType == "tecnico") {
                    ProfileDataItem(label = "Status", value = if (profile.ativo) "Ativo" else "Inativo")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Endereço",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileDataItem(label = "CEP", value = formatCep(profile.endereco.cep))
                ProfileDataItem(label = "Logradouro", value = profile.endereco.logradouro)
                ProfileDataItem(label = "Número", value = profile.endereco.numero.toString())
                ProfileDataItem(
                    label = "Complemento",
                    value = profile.endereco.complemento.ifEmpty { "Nenhum" }
                )
                ProfileDataItem(label = "Bairro", value = profile.endereco.bairro)
                ProfileDataItem(label = "Município", value = profile.endereco.municipio)
                ProfileDataItem(label = "UF", value = profile.endereco.uf)
            }
        }
    }
}

@Composable
private fun ProfileDataItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Tentar novamente")
        }
    }
}

private fun formatCpf(cpf: String): String {
    return if (cpf.length == 11) {
        "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${cpf.substring(6, 9)}-${cpf.substring(9)}"
    } else {
        cpf
    }
}

private fun formatPhone(phone: String): String {
    return if (phone.length == 11) {
        "(${phone.substring(0, 2)}) ${phone.substring(2, 7)}-${phone.substring(7)}"
    } else {
        phone
    }
}

private fun formatDate(date: String): String {
    return try {
        val parts = date.split("-")
        if (parts.size == 3) "${parts[2]}/${parts[1]}/${parts[0]}" else date
    } catch (e: Exception) {
        date
    }
}

private fun formatCep(cep: String): String {
    return if (cep.length == 8) {
        "${cep.substring(0, 5)}-${cep.substring(5)}"
    } else {
        cep
    }
}