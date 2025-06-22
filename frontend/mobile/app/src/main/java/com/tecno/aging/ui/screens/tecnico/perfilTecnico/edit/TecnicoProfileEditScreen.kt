package com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit

import android.content.Context
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tecno.aging.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.Calendar

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageDir = File(context.cacheDir, "images")
    if (!imageDir.exists()) imageDir.mkdirs()
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", imageDir)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileEditScreen(
    viewModel: ProfileEditViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showImageSourceSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)


    // 3. --- LAUNCHER PARA A GALERIA (EXISTENTE) ---
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onFotoChange(uri)
            }
        }
    )

    // 4. --- LAUNCHER PARA A CÂMERA (NOVO) ---
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri?.let { viewModel.onFotoChange(it) }
            }
        }
    )

    var showDatePicker by remember { mutableStateOf(false) }

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

                Spacer(Modifier.height(24.dp))

                AsyncImage(
                    model = uiState.fotoUri ?: R.drawable.ic_person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                TextButton(onClick = { showImageSourceSheet = true }) {
                    Text("Alterar foto")
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
                    OutlinedTextField(
                        value = uiState.sexo, onValueChange = viewModel::onSexoChange,
                        label = { Text("Sexo") }, modifier = Modifier.weight(0.5f)
                    )
                    Spacer(Modifier.width(8.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = uiState.dataNasc,
                            onValueChange = {},
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
                            CircularProgressIndicator(
                                Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Buscar")
                        }
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
                    OutlinedTextField(
                        value = uiState.uf, onValueChange = viewModel::onUfChange,
                        label = { Text("UF") }, modifier = Modifier.weight(1f), readOnly = true,
                    )
                    Spacer(Modifier.height(16.dp))
                }
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