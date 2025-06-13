package com.tecno.aging.ui.screens.cadastro.tecnicoCadastro

import DatePickerInput
import GenderDropdown
import MaskedInput
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    viewModel: CadastroViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentStep by remember { mutableStateOf(0) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onSuccess()
            viewModel.resetState()
        }
    }

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
            0 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MaskedInput(
                        value = uiState.tecnico.matricula,
                        onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(matricula = it)) },
                        mask = "########",
                        label = "Matrícula",
                        error = uiState.erros["matricula"],
                        modifier = Modifier.weight(1f)
                    )

                    MaskedInput(
                        value = uiState.tecnico.cpf,
                        onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(cpf = it)) },
                        mask = "###.###.###-##",
                        label = "CPF",
                        error = uiState.erros["cpf"],
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldWithError(
                    value = uiState.tecnico.nome,
                    onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(nome = it)) },
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
                        onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(telefone = it)) },
                        mask = "(##) #####-####",
                        label = "Telefone",
                        error = uiState.erros["telefone"],
                        modifier = Modifier.weight(1f)
                    )

                    GenderDropdown(
                        selectedGender = uiState.tecnico.sexo,
                        onGenderSelected = { viewModel.updateTecnico(uiState.tecnico.copy(sexo = it)) },
                        error = uiState.erros["sexo"],
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                DatePickerInput(
                    selectedDate = uiState.tecnico.dataNasc,
                    onDateSelected = { viewModel.updateTecnico(uiState.tecnico.copy(dataNasc = it)) },
                    error = uiState.erros["dataNasc"],
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    title = "Próximo",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { currentStep++ },
                )
            }

            1 -> {
                AddressSection(
                    address = uiState.tecnico.endereco,
                    onAddressChange = { viewModel.updateEndereco(it) },
                    onCepSearch = {
                        if (uiState.tecnico.endereco.cep.filter { it.isDigit() }.length == 8) {
                            viewModel.buscarCep()
                        }
                    },
                    loadingCep = uiState.loadingCep,
                    cepError = uiState.cepError,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    title = "Próximo",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { currentStep++ },
                )
            }

            2 -> {
                TextFieldWithError(
                    value = uiState.senha,
                    onValueChange = { viewModel.updateSenha(it) },
                    label = "Senha",
                    error = uiState.erros["senha"],
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextFieldWithError(
                    value = uiState.confirmarSenha,
                    onValueChange = { viewModel.updateConfirmarSenha(it) },
                    label = "Confirmar Senha",
                    error = uiState.erros["confirmarSenha"],
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ButtonComponent(
                    title = "Cadastrar",
                    loading = uiState.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = { viewModel.submitForm() },

                    )

                TextButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Já tem uma conta? Faça Login",
                        color = Color(0xFF594FBF)
                    )
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
