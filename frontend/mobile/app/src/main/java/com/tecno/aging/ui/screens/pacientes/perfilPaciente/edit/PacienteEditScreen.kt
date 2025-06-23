package com.tecno.aging.ui.screens.pacientes.perfilPaciente.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.tecno.aging.R
import com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit.createImageFile
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PacienteEditScreen(
    navController: NavController,
    viewModel: PacienteEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showImageSourceSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val singlePhotoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) viewModel.onFotoChange(uri)
        }
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) imageUri?.let { viewModel.onFotoChange(it) }
        }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message -> snackbarHostState.showSnackbar(message) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Meu Perfil",
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
                Spacer(Modifier.height(24.dp))
                AsyncImage(
                    model = uiState.fotoUri ?: R.drawable.ic_person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { showImageSourceSheet = true }) { Text("Alterar foto") }
                Spacer(Modifier.height(16.dp))

                // --- DADOS PESSOAIS DO PACIENTE ---
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
                    OutlinedTextField(
                        value = uiState.sexo, onValueChange = viewModel::onSexoChange,
                        label = { Text("Sexo") }, modifier = Modifier.weight(0.5f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = uiState.dataNasc, onValueChange = {},
                            label = { Text("Data de Nascimento") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = { Icon(Icons.Default.DateRange, "Calendário") }
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(onClick = { showDatePicker = true })
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Divider()
                Spacer(Modifier.height(16.dp))

                // --- NOVA SEÇÃO: DADOS SÓCIO-DEMOGRÁFICOS ---
                Text("Dados Sócio-demográficos", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.rg, onValueChange = viewModel::onRgChange,
                    label = { Text("RG") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.corRaca, onValueChange = viewModel::onCorRacaChange,
                    label = { Text("Cor/Raça") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.estadoCivil, onValueChange = viewModel::onEstadoCivilChange,
                    label = { Text("Estado Civil") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.escolaridade, onValueChange = viewModel::onEscolaridadeChange,
                    label = { Text("Escolaridade") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.nacionalidade, onValueChange = viewModel::onNacionalidadeChange,
                    label = { Text("Nacionalidade") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.municipioNasc, onValueChange = viewModel::onMunicipioNascChange,
                    label = { Text("Município de Nascimento") }, modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    if (showImageSourceSheet) {
        ModalBottomSheet(onDismissRequest = { showImageSourceSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Escolher fonte da imagem", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))

                ListItem(
                    headlineContent = { Text("Tirar foto") },
                    leadingContent = { Icon(Icons.Default.CameraAlt, null) },
                    modifier = Modifier.clickable {
                        showImageSourceSheet = false
                        if (cameraPermissionState.status.isGranted) {
                            val file = createImageFile(context)
                            imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            takePictureLauncher.launch(imageUri!!)
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                )

                ListItem(
                    headlineContent = { Text("Escolher uma da Galeria") },
                    leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                    modifier = Modifier.clickable {
                        showImageSourceSheet = false
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()

        calendar.set(1900, 0, 1)
        val minDateMillis = calendar.timeInMillis

        val maxDateMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        val datePickerState = rememberDatePickerState(
            yearRange = 1900..Calendar.getInstance().get(Calendar.YEAR),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= minDateMillis && utcTimeMillis <= maxDateMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false

                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val brasilTimeZone = TimeZone.getTimeZone("America/Sao_Paulo")

                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                                timeZone = brasilTimeZone
                            }
                            val formattedDate = sdf.format(Date(selectedDateMillis))
                            viewModel.onDataNascChange(formattedDate)
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = null,
                headline = {
                    Text(
                        text = "Selecione uma data",
                        modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 12.dp)
                    )
                }
            )
        }
    }
}