package com.tecno.aging.ui.screens.cadastro.tecnicoCadastro

import DatePickerInput
import GenderDropdown
import MaskedInput
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.components.sections.AddressSection
import com.tecno.aging.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    navController: NavController,
    viewModel: CadastroTecnicoViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onSuccess()
            viewModel.resetState()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Técnico") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == 0) {
                            navController.popBackStack()
                        } else {
                            currentStep--
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.icntecno),
                contentDescription = "Logo TecnoAging",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(100.dp)
            )
            Text(
                text = "Progresso: Etapa ${currentStep + 1} de 3",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            when (currentStep) {
                0 -> { // Etapa 1: Dados Pessoais
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextFieldWithError(
                            value = uiState.tecnico.matricula,
                            onValueChange = viewModel::onMatriculaChanged,
                            label = "Matrícula",
                            error = uiState.erros["matricula"],
                            modifier = Modifier.weight(1f)
                        )
                        MaskedInput(
                            value = uiState.tecnico.cpf,
                            onValueChange = viewModel::onCpfChanged,
                            mask = "###.###.###-##",
                            label = "CPF",
                            error = uiState.erros["cpf"],
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldWithError(
                        value = uiState.tecnico.nome,
                        onValueChange = viewModel::onNomeChanged,
                        label = "Nome",
                        error = uiState.erros["nome"],
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MaskedInput(
                            value = uiState.tecnico.telefone,
                            onValueChange = viewModel::onTelefoneChanged,
                            mask = "(##) #####-####",
                            label = "Telefone",
                            error = uiState.erros["telefone"],
                            modifier = Modifier.weight(1f)
                        )
                        GenderDropdown(
                            selectedGender = uiState.tecnico.sexo,
                            onGenderSelected = viewModel::onSexoChanged,
                            error = uiState.erros["sexo"],
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    DatePickerInput(
                        selectedDate = uiState.tecnico.dataNasc,
                        onDateSelected = viewModel::onDataNascChanged,
                        error = uiState.erros["dataNasc"],
                        label = "Data de Nasc",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonComponent(
                        title = "Próximo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = { currentStep++ })
                }

                1 -> { // Etapa 2: Endereço
                    AddressSection(
                        address = uiState.tecnico.endereco,
                        onAddressChange = viewModel::onEnderecoChanged,
                        onCepSearch = viewModel::buscarCep,
                        loadingCep = uiState.loadingCep,
                        erros = uiState.erros,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonComponent(
                        title = "Próximo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = { currentStep++ })
                }

                2 -> { // Etapa 3: Senha e Finalização
                    TextFieldWithError(
                        value = uiState.senha,
                        onValueChange = viewModel::onSenhaChanged,
                        label = "Senha",
                        error = uiState.erros["senha"],
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextFieldWithError(
                        value = uiState.confirmarSenha,
                        onValueChange = viewModel::onConfirmarSenhaChanged,
                        label = "Confirmar Senha",
                        error = uiState.erros["confirmarSenha"],
                        isPassword = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonComponent(
                        title = "Cadastrar",
                        loading = uiState.loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = viewModel::submitForm
                    )
                }
            }
        }
    }
}
