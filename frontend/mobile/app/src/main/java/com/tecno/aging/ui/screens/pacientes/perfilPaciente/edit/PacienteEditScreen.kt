package com.tecno.aging.ui.screens.pacientes.perfilPaciente.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit.createImageFile
import com.tecno.aging.ui.theme.AppColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val sexoOptions = listOf("Masculino", "Feminino")
private val corRacaOptions = listOf("Branca", "Preta", "Parda", "Amarela", "Indígena", "Não declarada")
private val estadoCivilOptions = listOf("Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "União Estável")
private val escolaridadeOptions = listOf(
    "Analfabeto",
    "Ensino Fundamental Incompleto",
    "Ensino Fundamental Completo",
    "Ensino Médio Incompleto",
    "Ensino Médio Completo",
    "Ensino Superior Incompleto",
    "Ensino Superior Completo",
    "Pós-graduação",
    "Mestrado",
    "Doutorado"
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PacienteEditScreen(
    navController: NavController,
    viewModel: PacienteEditViewModel = viewModel(factory = PacienteEditViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var currentStep by remember { mutableIntStateOf(0) }
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

    LaunchedEffect(uiState.userMessage, uiState.errorMessage) {
        val message = uiState.userMessage ?: uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.userMessageShown()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("profile_updated", true)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil do Paciente") },
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
                uiState.errorMessage != null && uiState.cpf.isEmpty() -> {
                    Text(
                        text = "Erro ao carregar perfil: ${uiState.errorMessage}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
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
                                onShowDatePicker = { showDatePicker = true }
                            )
                            1 -> StepSocioData(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }

    // --- BottomSheet (Fonte da Imagem) ---
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
                    headlineContent = { Text("Escolher da Galeria") },
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
            onDismissRequest = { showDatePicker = false },
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
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
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

@Composable
private fun StepPersonalData(
    uiState: PacienteEditUiState,
    viewModel: PacienteEditViewModel,
    onNextClick: () -> Unit,
    onChangePictureClick: () -> Unit,
    onShowDatePicker: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- CARD FOTO DE PERFIL ---
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

        EditInfoCard(title = "Dados Pessoais", icon = Icons.Default.Badge) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = viewModel::onNomeChange,
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.cpf,
                    onValueChange = {},
                    label = { Text("CPF") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                OutlinedTextField(
                    value = uiState.telefone,
                    onValueChange = viewModel::onTelefoneChange,
                    label = { Text("Telefone") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DropdownInput(
                        label = "Sexo",
                        options = sexoOptions,
                        selectedOption = uiState.sexo,
                        onOptionSelected = viewModel::onSexoChange,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePickerInput(
                        selectedDate = uiState.dataNasc,
                        onShowDatePicker = onShowDatePicker,
                        label = "Data de Nasc.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp)
        ) {
            Text("PRÓXIMO: DADOS SÓCIO-DEMOGRÁFICOS")
        }
    }
}

@Composable
private fun StepSocioData(
    uiState: PacienteEditUiState,
    viewModel: PacienteEditViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val context = LocalContext.current
        val contentResolver = context.contentResolver
        EditInfoCard(title = "Dados Sócio-demográficos", icon = Icons.Default.Info) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.rg,
                    onValueChange = viewModel::onRgChange,
                    label = { Text("RG") },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownInput(
                    label = "Cor/Raça",
                    options = corRacaOptions,
                    selectedOption = uiState.corRaca,
                    onOptionSelected = viewModel::onCorRacaChange,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownInput(
                    label = "Estado Civil",
                    options = estadoCivilOptions,
                    selectedOption = uiState.estadoCivil,
                    onOptionSelected = viewModel::onEstadoCivilChange,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownInput(
                    label = "Escolaridade",
                    options = escolaridadeOptions,
                    selectedOption = uiState.escolaridade,
                    onOptionSelected = viewModel::onEscolaridadeChange,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.nacionalidade,
                    onValueChange = viewModel::onNacionalidadeChange,
                    label = { Text("Nacionalidade") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.municipioNasc,
                    onValueChange = viewModel::onMunicipioNascChange,
                    label = { Text("Município de Nascimento") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = { viewModel.onSaveProfile(contentResolver) },
            enabled = !uiState.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp)
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Salvar Alterações")
            }
        }
    }
}

/////// COMPONENTES

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

@Composable
fun DatePickerInput(
    selectedDate: String,
    onShowDatePicker: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.DateRange, "Calendário") }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onShowDatePicker)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownInput(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}