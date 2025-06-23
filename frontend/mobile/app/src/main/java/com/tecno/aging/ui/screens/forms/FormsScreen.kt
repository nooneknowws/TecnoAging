package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.domain.models.paciente.Paciente
import com.tecno.aging.ui.components.cards.FormCard
import com.tecno.aging.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsScreen(
    navController: NavController,
    viewModel: FormsListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showPatientDialog by remember { mutableStateOf(false) }
    var selectedFormId by remember { mutableLongStateOf(0L) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Formulários de Avaliação",
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
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Selecione um formulário abaixo para iniciar a avaliação. Cada teste auxilia na identificação de diferentes aspectos da saúde e bem-estar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.forms) { form ->
                            FormCard(
                                title = form.titulo,
                                icon = Icons.Default.MedicalServices, // Ícone genérico
                                onClick = {
                                    val perfil = SessionManager.getUserProfile()
                                    if (perfil.equals("TECNICO", ignoreCase = true)) {
                                        selectedFormId = form.id
                                        showPatientDialog = true
                                    } else {
                                        val pacienteId = SessionManager.getUserId()?.toLong()
                                        if (pacienteId != null) {
                                            navController.navigate("form/${form.id}/$pacienteId")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPatientDialog) {
        PatientSelectionDialog(
            patients = uiState.patients,
            onDismiss = { showPatientDialog = false },
            onPatientSelected = { pacienteId ->
                showPatientDialog = false
                navController.navigate("form/$selectedFormId/$pacienteId")
            }
        )
    }
}

@Composable
fun PatientSelectionDialog(
    patients: List<Paciente>,
    onDismiss: () -> Unit,
    onPatientSelected: (Long) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            Column {
                Text(
                    text = "Selecione um Paciente",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                if (patients.isEmpty()) {
                    Box(Modifier.padding(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Nenhum paciente encontrado.")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(patients) { patient ->
                            ListItem(
                                headlineContent = { Text(patient.nome) },
                                supportingContent = { Text("CPF: ${patient.cpf}") },
                                modifier = Modifier.clickable { onPatientSelected(patient.id.toLong()) }
                            )
                        }
                    }
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    Text("CANCELAR")
                }
            }
        }
    }
}