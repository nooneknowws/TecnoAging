package com.tecno.aging.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecno.aging.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.cepErrorMessage) {
        uiState.cepErrorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Editar Perfil") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = viewModel::onSaveProfile,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(50.dp),
                    enabled = !uiState.isSaving
                ) {
                    Text("SALVAR ALTERAÇÕES")
                }
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- SEÇÃO FOTO DE PERFIL ---
                Spacer(Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_person), // Placeholder
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { /* TODO: Lógica para abrir galeria */ }) {
                    Text("Escolher arquivo")
                }
                Spacer(Modifier.height(16.dp))

                // --- DADOS PESSOAIS ---
                OutlinedTextField(
                    value = uiState.matricula, onValueChange = {},
                    label = { Text("Matrícula") },
                    modifier = Modifier.fillMaxWidth(), readOnly = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.nome, onValueChange = viewModel::onNomeChange,
                    label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.cpf, onValueChange = {},
                        label = { Text("CPF") },
                        modifier = Modifier.weight(1f), readOnly = true
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = uiState.telefone, onValueChange = viewModel::onTelefoneChange,
                        label = { Text("Telefone") }, modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    // TODO: Substituir por um DropdownMenu real se necessário
                    OutlinedTextField(
                        value = uiState.sexo, onValueChange = viewModel::onSexoChange,
                        label = { Text("Sexo") }, modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    // TODO: Substituir por um DatePickerDialog real se necessário
                    OutlinedTextField(
                        value = uiState.dataNasc, onValueChange = viewModel::onDataNascChange,
                        label = { Text("Data de Nascimento") }, modifier = Modifier.weight(1f),
                        trailingIcon = { Icon(Icons.Default.DateRange, null) }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                // --- SEÇÃO ENDEREÇO ---
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = uiState.cep, onValueChange = viewModel::onCepChanged,
                        label = { Text("CEP") }, modifier = Modifier.weight(1f),
                        isError = uiState.cepErrorMessage != null
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { viewModel.onCepChanged(uiState.cep) },
                        enabled = !uiState.isSearchingCep
                    ) {
                        if (uiState.isSearchingCep) {
                            CircularProgressIndicator(Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else { Text("Buscar") }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.logradouro, onValueChange = {},
                        label = { Text("Logradouro") }, modifier = Modifier.weight(2f),
                        readOnly = true
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = uiState.numero, onValueChange = viewModel::onNumeroChange,
                        label = { Text("Número") }, modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.complemento, onValueChange = viewModel::onComplementoChange,
                    label = { Text("Complemento") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.bairro, onValueChange = {},
                    label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = uiState.municipio, onValueChange = {},
                        label = { Text("Município") }, modifier = Modifier.weight(2f),
                        readOnly = true
                    )
                    Spacer(Modifier.width(8.dp))
                    // TODO: Substituir por um DropdownMenu real
                    OutlinedTextField(
                        value = uiState.uf, onValueChange = viewModel::onUfChange,
                        label = { Text("UF") }, modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.height(16.dp))
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}