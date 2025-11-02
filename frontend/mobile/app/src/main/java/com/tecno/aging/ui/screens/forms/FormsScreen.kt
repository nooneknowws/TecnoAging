package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.ui.components.cards.FormularioCard
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
        },
        containerColor = AppColors.Gray50
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = viewModel::refresh,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.error != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            } else if (uiState.forms.isEmpty() && !uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Nenhum formulário encontrado.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Selecione um formulário abaixo para iniciar a avaliação.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(uiState.forms) { form ->
                        FormularioCard(
                            form = form,
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
                .heightIn(max = 500.dp),
            colors = CardDefaults.cardColors(containerColor = AppColors.White)
        ) {
            Column {
                Text(
                    text = "Selecione um Paciente",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                if (patients.isEmpty()) {
                    Box(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Nenhum paciente encontrado.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(patients) { patient ->
                            PatientSelectorCard(
                                patient = patient,
                                onClick = { onPatientSelected(patient.id.toLong()) }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
fun PatientSelectorCard(
    patient: Paciente,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, AppColors.Gray200)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Ícone de Paciente",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.Gray100)
                    .padding(8.dp),
                tint = AppColors.Gray700
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = patient.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "CPF: ${patient.cpf}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.Gray500
                )
            }
        }
    }
}