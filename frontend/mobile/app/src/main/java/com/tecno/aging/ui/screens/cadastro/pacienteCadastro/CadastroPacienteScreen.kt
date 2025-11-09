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
import com.tecno.aging.ui.components.dropdowns.DropdownWithError
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.components.sections.AddressSection
import com.tecno.aging.ui.theme.AppColors

val estadoCivilOptions = listOf("Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viúvo(a)", "União Estável")
val escolaridadeOptions = listOf("Nenhum", "Fundamental Incompleto", "Fundamental Completo", "Médio Incompleto", "Médio Completo", "Superior Incompleto", "Superior Completo", "Pós-graduação")
val corRacaOptions = listOf("Branca", "Parda", "Preta", "Amarela", "Indígena")
val estadosBrasileiros = listOf(
    "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
    "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
    "RS", "RO", "RR", "SC", "SP", "SE", "TO"
)
val orgaoEmissorOptions = listOf(
    "SSP",
    "PF",
    "DETRAN",
    "CREA",
    "OAB",
    "CRM",
)
val nivelSocioeconomicoOptions = listOf(
    "Classe A",
    "Classe B",
    "Classe C",
    "Classe D",
    "Classe E"
)
val parentescoOptions = listOf(
    "PAI", "MAE", "FILHO", "FILHA", "CONJUGUE", "COMPANHEIRO",
    "COMPANHEIRA", "IRMAO", "IRMA", "CUIDADOR", "AMIGO", "OUTRO"
)

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

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

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
            label = "Nome Completo",
            error = state.erros["nome"]
        )
        MaskedInput(
            value = state.cpf,
            onValueChange = viewModel::onCpfChange,
            mask = "###.###.###-##",
            label = "CPF",
            error = state.erros["cpf"]
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GenderDropdown(
                selectedGender = state.sexo,
                onGenderSelected = viewModel::onSexoChange,
                error = state.erros["sexo"],
                modifier = Modifier.weight(0.4f)
            )
            DatePickerInput(
                selectedDate = state.dataNascimento,
                onDateSelected = viewModel::onDataNascimentoChange,
                label = "Data de Nasc",
                error = state.erros["dataNascimento"],
                modifier = Modifier.weight(0.6f)
            )
        }
        MaskedInput(
            value = state.telefone,
            onValueChange = viewModel::onTelefoneChange,
            mask = "(##) #####-####",
            label = "Telefone",
            error = state.erros["telefone"]
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
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxHeight()) {

        DropdownWithError(
            label = "Estado Civil",
            options = estadoCivilOptions,
            selectedValue = state.estadoCivil,
            onValueSelected = viewModel::onEstadoCivilChange,
            error = state.erros["estadoCivil"]
        )

        DropdownWithError(
            label = "Escolaridade",
            options = escolaridadeOptions,
            selectedValue = state.escolaridade,
            onValueSelected = viewModel::onEscolaridadeChange,
            error = state.erros["escolaridade"]
        )

        TextFieldWithError(
            value = state.nacionalidade,
            onValueChange = viewModel::onNacionalidadeChange,
            label = "Nacionalidade",
            error = state.erros["nacionalidade"]
        )

        DropdownWithError(
            label = "Cor/Raça",
            options = corRacaOptions,
            selectedValue = state.corRaca,
            onValueSelected = viewModel::onCorRacaChange,
            error = state.erros["corRaca"]
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWithError(
                value = state.municipioNasc,
                onValueChange = viewModel::onMunicipioNascChange,
                label = "Município de Nasc.",
                modifier = Modifier.weight(2f),
                error = state.erros["municipioNasc"]
            )
            DropdownWithError(
                label = "UF",
                options = estadosBrasileiros,
                selectedValue = state.ufNasc,
                modifier = Modifier.weight(1f),
                onValueSelected = viewModel::onUfNascChange,
                error = state.erros["ufNasc"]
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
            error = state.erros["rg"],
            modifier = Modifier.fillMaxWidth()
        )

        DatePickerInput(
            selectedDate = state.dataExpedicao,
            onDateSelected = viewModel::onDataExpedicaoChange,
            label = "Data de Expedição",
            error = state.erros["dataExpedicao"],
            modifier = Modifier.fillMaxWidth()
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownWithError(
                label = "Órgão Emissor",
                options = orgaoEmissorOptions,
                selectedValue = state.orgaoEmissor,
                modifier = Modifier.weight(0.6f),
                onValueSelected = viewModel::onOrgaoEmissorChange,
                error = state.erros["orgaoEmissor"]
            )
            DropdownWithError(
                label = "UF",
                options = estadosBrasileiros,
                selectedValue = state.ufEmissor,
                modifier = Modifier.weight(0.4f),
                onValueSelected = viewModel::onUfEmissorChange,
                error = state.erros["ufEmissor"]
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextFieldWithError(
                value = state.peso,
                onValueChange = viewModel::onPesoChange,
                label = "Peso (kg)",
                error = state.erros["peso"],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            TextFieldWithError(
                value = state.altura,
                onValueChange = viewModel::onAlturaChange,
                label = "Altura (m)",
                error = state.erros["altura"],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        DropdownWithError(
            label = "Nível Socioeconômico",
            options = nivelSocioeconomicoOptions,
            selectedValue = state.socioeconomico,
            modifier = Modifier.fillMaxWidth(),
            onValueSelected = viewModel::onSocioeconomicoChange,
            error = state.erros["socioeconomico"]
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
    Column {
        AddressSection(
            address = state.endereco,
            onAddressChange = viewModel::onEnderecoChange,
            onCepSearch = { viewModel.buscarCep() },
            loadingCep = state.loadingCep,
            erros = state.erros
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
            error = state.erros["senha"],
            isPassword = true
        )
        TextFieldWithError(
            value = state.confirmarSenha,
            onValueChange = viewModel::onConfirmarSenhaChange,
            label = "Confirmar Senha",
            error = state.erros["confirmarSenha"],
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