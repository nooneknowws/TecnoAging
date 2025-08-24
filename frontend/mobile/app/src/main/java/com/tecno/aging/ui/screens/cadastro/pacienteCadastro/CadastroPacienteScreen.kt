package com.tecno.aging.ui.screens.cadastro.pacienteCadastro

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import DatePickerInput
import GenderDropdown
import MaskedInput
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.components.sections.AddressSection
import com.tecno.aging.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroPacienteScreen(
    navController: NavController,
    viewModel: CadastroPacienteViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.success) {
        if (uiState.success) onSuccess()
    }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Paciente") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.currentStep == 0) {
                            navController.popBackStack()
                        } else {
                            viewModel.onPreviousStepClick()
                        }
                    }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Progresso: Etapa ${uiState.currentStep + 1} de 5")

            when (uiState.currentStep) {
                0 -> Step1(uiState, viewModel, onNext = viewModel::onNextStepClick)
                1 -> Step2(uiState, viewModel, onNext = viewModel::onNextStepClick)
                2 -> Step3(uiState, viewModel, onNext = viewModel::onNextStepClick)
                3 -> Step4(uiState, viewModel, onNext = viewModel::onNextStepClick)
                4 -> Step5(uiState, viewModel, navController)
            }
        }
    }
}

@Composable
private fun Step1(
    state: CadastroPacienteState,
    viewModel: CadastroPacienteViewModel,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextFieldWithError(
            value = state.nome,
            onValueChange = viewModel::onNomeChange,
            label = "Nome Completo"
        )
        MaskedInput(
            value = state.cpf,
            onValueChange = viewModel::onCpfChange,
            mask = "###.###.###-##",
            label = "CPF"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GenderDropdown(
                selectedGender = state.sexo,
                onGenderSelected = viewModel::onSexoChange,
                modifier = Modifier.weight(0.4f)
            )
            DatePickerInput(
                selectedDate = state.dataNascimento,
                onDateSelected = viewModel::onDataNascimentoChange,
                label = "Data de Nasc",
                modifier = Modifier.weight(0.6f)
            )
        }
        MaskedInput(
            value = state.telefone,
            onValueChange = viewModel::onTelefoneChange,
            mask = "(##) #####-####",
            label = "Telefone"
        )
        Spacer(Modifier.weight(1f))
        ButtonComponent(title = "Próximo", onClick = onNext, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun Step2(
    state: CadastroPacienteState,
    viewModel: CadastroPacienteViewModel,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextFieldWithError(
            value = state.estadoCivil,
            onValueChange = viewModel::onEstadoCivilChange,
            label = "Estado Civil"
        )
        TextFieldWithError(
            value = state.escolaridade,
            onValueChange = viewModel::onEscolaridadeChange,
            label = "Escolaridade"
        )
        TextFieldWithError(
            value = state.nacionalidade,
            onValueChange = viewModel::onNacionalidadeChange,
            label = "Nacionalidade"
        )
        TextFieldWithError(
            value = state.corRaca,
            onValueChange = viewModel::onCorRacaChange,
            label = "Cor/Raça"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWithError(
                value = state.municipioNasc,
                onValueChange = viewModel::onMunicipioNascChange,
                label = "Município de Nasc.",
                modifier = Modifier.weight(2f)
            )
            TextFieldWithError(
                value = state.ufNasc,
                onValueChange = viewModel::onUfNascChange,
                label = "UF",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.weight(1f))
        ButtonComponent(title = "Próximo", onClick = onNext, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun Step3(
    state: CadastroPacienteState,
    viewModel: CadastroPacienteViewModel,
    onNext: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextFieldWithError(
            value = state.rg,
            onValueChange = viewModel::onRgChange,
            label = "RG",
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerInput(
            selectedDate = state.dataExpedicao,
            onDateSelected = viewModel::onDataExpedicaoChange,
            label = "Data de Expedição",
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWithError(
                value = state.orgaoEmissor,
                onValueChange = viewModel::onOrgaoEmissorChange,
                label = "Órgão Emissor",
                modifier = Modifier.weight(0.6f)
            )
            TextFieldWithError(
                value = state.ufEmissor,
                onValueChange = viewModel::onUfEmissorChange,
                label = "UF",
                modifier = Modifier.weight(0.4f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWithError(
                value = state.peso,
                onValueChange = viewModel::onPesoChange,
                label = "Peso (kg)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            TextFieldWithError(
                value = state.altura,
                onValueChange = viewModel::onAlturaChange,
                label = "Altura (m)",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        TextFieldWithError(
            value = state.socioeconomico,
            onValueChange = viewModel::onSocioeconomicoChange,
            label = "Nível Socioeconômico",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))
        ButtonComponent(title = "Próximo", onClick = onNext, modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Step4(
    state: CadastroPacienteState,
    viewModel: CadastroPacienteViewModel,
    onNext: () -> Unit
) {
    val parentescoOptions = listOf(
        "PAI", "MAE", "FILHO", "FILHA", "CONJUGUE", "COMPANHEIRO",
        "COMPANHEIRA", "IRMAO", "IRMA", "CUIDADOR", "AMIGO", "OUTRO"
    )

    Column {
        AddressSection(
            address = state.endereco,
            onAddressChange = viewModel::onEnderecoChange,
            onCepSearch = { viewModel.buscarCep() },
            loadingCep = state.loadingCep,
            cepError = state.cepError
        )
        Spacer(Modifier.height(24.dp))

        Text("Contatos de Emergência", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        state.contatos.forEachIndexed { index, contato ->
            OutlinedCard(
                modifier = Modifier.padding(vertical = 8.dp),
                border = BorderStroke(1.dp, AppColors.Gray200)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Contato ${index + 1}", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        if (state.contatos.size > 1) {
                            IconButton(onClick = { viewModel.removeContato(index) }) {
                                Icon(Icons.Default.Delete, "Remover Contato")
                            }
                        }
                    }

                    TextFieldWithError(
                        value = contato.nome,
                        onValueChange = { novoNome ->
                            viewModel.onContatoChange(index, contato.copy(nome = novoNome))
                        },
                        label = "Nome do Contato"
                    )
                    MaskedInput(
                        value = contato.telefone,
                        onValueChange = { novoTelefone ->
                            viewModel.onContatoChange(index, contato.copy(telefone = novoTelefone))
                        },
                        mask = "(##) #####-####",
                        label = "Telefone do Contato"
                    )

                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = contato.parentesco,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Parentesco") },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            parentescoOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onContatoChange(index, contato.copy(parentesco = option))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        TextButton(onClick = viewModel::addContato) { Text("+ Adicionar Outro Contato") }

        Spacer(Modifier.weight(1f, fill = true))
        ButtonComponent(title = "Próximo", onClick = onNext, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun Step5(
    state: CadastroPacienteState,
    viewModel: CadastroPacienteViewModel,
    navController: NavController
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextFieldWithError(
            value = state.senha,
            onValueChange = viewModel::onSenhaChange,
            label = "Senha",
            isPassword = true
        )
        TextFieldWithError(
            value = state.confirmarSenha,
            onValueChange = viewModel::onConfirmarSenhaChange,
            label = "Confirmar Senha",
            isPassword = true
        )
        Spacer(Modifier.weight(1f))
        ButtonComponent(
            title = "Finalizar Cadastro",
            loading = state.loading,
            onClick = viewModel::submitForm,
            modifier = Modifier.fillMaxWidth()
        )
    }
}