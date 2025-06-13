package com.tecno.aging.ui.screens.cadastro.tecnicoCadastro

import DatePickerInput
import GenderDropdown
import MaskedInput
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.components.sections.AddressSection

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

    // Reseta o estado ao entrar na tela para evitar dados de cadastros anteriores
    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4E9FC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cadastro de Técnico",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp),
            color = Color(0xFF3C3C3C)
        )

        when (currentStep) {
            0 -> { // Etapa 1: Dados Pessoais
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextFieldWithError( // Usar TextField normal para matrícula se não precisar de máscara
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    selectedDate = uiState.tecnico.dataNascimento,
                    onDateSelected = viewModel::onDataNascChanged,
                    error = uiState.erros["dataNasc"],
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                ButtonComponent(title = "Próximo", modifier = Modifier.fillMaxWidth(), onClick = { currentStep++ })
            }

            1 -> { // Etapa 2: Endereço
                AddressSection(
                    address = uiState.tecnico.endereco,
                    onAddressChange = viewModel::onEnderecoChanged,
                    onCepSearch = viewModel::buscarCep,
                    loadingCep = uiState.loadingCep,
                    cepError = uiState.cepError,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                ButtonComponent(title = "Próximo", modifier = Modifier.fillMaxWidth(), onClick = { currentStep++ })
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
                Spacer(modifier = Modifier.height(16.dp))
                ButtonComponent(
                    title = "Cadastrar",
                    loading = uiState.loading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = viewModel::submitForm
                )
                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text(text = "Já tem uma conta? Faça Login", color = Color(0xFF594FBF))
                }
                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}