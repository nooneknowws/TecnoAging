package com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit

import DatePickerInput
import GenderDropdown
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.theme.AppColors
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageDir = File(context.cacheDir, "images")
    if (!imageDir.exists()) imageDir.mkdirs()
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", imageDir)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileEditScreen(
    navController: NavController,
    viewModel: ProfileEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var currentStep by remember { mutableIntStateOf(0) }
    var showImageSourceSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) viewModel.onFotoChange(uri) }
    )
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success -> if (success) imageUri?.let { viewModel.onFotoChange(it) } }
    )

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.previousBackStackEntry?.savedStateHandle?.set("profile_updated", true)
            navController.popBackStack()
        }
    }
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.userMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == 0) {
                            navController.popBackStack()
                        } else {
                            currentStep--
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AppColors.Gray50
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.userMessage != null && uiState.matricula.isEmpty() -> {}
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Etapa ${currentStep + 1} de 2",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        when (currentStep) {
                            0 -> StepPersonalData(
                                uiState = uiState,
                                viewModel = viewModel,
                                onNextClick = { currentStep++ },
                                onChangePictureClick = { showImageSourceSheet = true },
                            )
                            1 -> StepAddress(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }

    if (showImageSourceSheet) {
        ModalBottomSheet(onDismissRequest = { showImageSourceSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
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
                    headlineContent = { Text("Escolher da Galeria") },
                    leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                    modifier = Modifier.clickable {
                        showImageSourceSheet = false
                        singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        })
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        viewModel.onDataNascChange(sdf.format(Date(millis)))
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun StepPersonalData(
    uiState: ProfileEditUiState,
    viewModel: ProfileEditViewModel,
    onNextClick: () -> Unit,
    onChangePictureClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EditInfoCard(title = "Foto de Perfil", icon = Icons.Default.AccountCircle) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Exibe a foto - prioriza URI nova, depois bitmap do banco, depois ícone padrão
                    when {
                        uiState.fotoUri != null -> {
                            AsyncImage(
                                model = uiState.fotoUri,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(120.dp).clip(CircleShape)
                            )
                        }
                        uiState.fotoBitmap != null -> {
                            Image(
                                bitmap = uiState.fotoBitmap,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(120.dp).clip(CircleShape)
                            )
                        }
                        else -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "Foto de perfil padrão",
                                modifier = Modifier.size(120.dp).clip(CircleShape)
                            )
                        }
                    }
                    TextButton(onClick = onChangePictureClick) { Text("Alterar foto") }
                }
            }
        }

        // --- CARD DE DADOS PESSOAIS ---
        EditInfoCard(title = "Dados Pessoais", icon = Icons.Default.Badge) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = uiState.matricula, onValueChange = {}, label = { Text("Matrícula") }, modifier = Modifier.fillMaxWidth(), readOnly = true)
                OutlinedTextField(value = uiState.nome, onValueChange = viewModel::onNomeChange, label = { Text("Nome Completo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = uiState.cpf, onValueChange = {}, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth(), readOnly = true)
                OutlinedTextField(value = uiState.telefone, onValueChange = viewModel::onTelefoneChange, label = { Text("Telefone") }, modifier = Modifier.fillMaxWidth())

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenderDropdown(
                        selectedGender = uiState.sexo,
                        onGenderSelected = viewModel::onSexoChange,
                        modifier = Modifier.weight(0.4f)
                    )
                    DatePickerInput(
                        selectedDate = uiState.dataNasc,
                        onDateSelected = viewModel::onDataNascChange,
                        label = "Data de Nasc",
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
        }

        ButtonComponent(
            title = "Próximo: Endereço",
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun StepAddress(uiState: ProfileEditUiState, viewModel: ProfileEditViewModel) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EditInfoCard(title = "Endereço", icon = Icons.Default.Home) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = uiState.endereco.cep, onValueChange = viewModel::onCepChanged, label = { Text("CEP") }, modifier = Modifier.weight(1f), isError = uiState.cepErrorMessage != null)
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.buscarCep(uiState.endereco.cep) }, enabled = !uiState.isSearchingCep) {
                        if (uiState.isSearchingCep) {
                            CircularProgressIndicator(Modifier.size(24.dp))
                        } else { Text("Buscar") }
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = uiState.endereco.logradouro, onValueChange = {}, label = { Text("Logradouro") }, modifier = Modifier.weight(2f), readOnly = true)
                    OutlinedTextField(value = uiState.endereco.numero, onValueChange = viewModel::onNumeroChange, label = { Text("Número") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = uiState.endereco.complemento ?: "", onValueChange = viewModel::onComplementoChange, label = { Text("Complemento") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = uiState.endereco.bairro, onValueChange = {}, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth(), readOnly = true)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = uiState.endereco.municipio, onValueChange = {}, label = { Text("Município") }, modifier = Modifier.weight(2f), readOnly = true)
                    OutlinedTextField(value = uiState.endereco.uf, onValueChange = {}, label = { Text("UF") }, modifier = Modifier.weight(1f), readOnly = true)
                }
            }
        }

        ButtonComponent(
            title = "Salvar Alterações",
            loading = uiState.isSaving,
            onClick = { viewModel.onSaveProfile(contentResolver) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
    }
}

@Composable
private fun EditInfoCard(
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