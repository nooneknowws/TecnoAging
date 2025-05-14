package com.tecno.aging.ui.screens.cadastro

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import com.tecno.aging.ui.components.buttons.LoadingButton
import com.tecno.aging.ui.components.forms.DatePickerInput
import com.tecno.aging.ui.components.forms.GenderDropdown
import com.tecno.aging.ui.components.forms.MaskedInput
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.components.sections.AddressSection

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CadastroScreenPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        CadastroScreen(
            navController = navController,
            viewModel = object : CadastroViewModel() {
                init {
                    _uiState.value = CadastroState(
                        tecnico = Tecnico(
                            matricula = "12345678",
                            nome = "João Silva",
                            cpf = "123.456.789-09",
                            telefone = "(11) 98765-4321",
                            sexo = "Masculino",
                            dataNasc = "15/05/1985",
                            endereco = Endereco(
                                cep = "01234-567",
                                logradouro = "Rua das Flores",
                                numero = "123",
                                complemento = "Apto 45",
                                bairro = "Centro",
                                municipio = "São Paulo",
                                uf = "SP"
                            )
                        ),
                        senha = "password123",
                        confirmarSenha = "password123"
                    )
                }
            },
            onSuccess = { navController.navigate("login") }
        )
    }
}
@Composable
fun CadastroScreen(
    navController: NavController,
    viewModel: CadastroViewModel = viewModel(),
    onSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text(
            text = "Cadastro de Técnico",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Matrícula/Nome Row
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

            TextFieldWithError(
                value = uiState.tecnico.nome,
                onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(nome = it)) },
                label = "Nome",
                error = uiState.erros["nome"],
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MaskedInput(
                value = uiState.tecnico.cpf,
                onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(cpf = it)) },
                mask = "###.###.###-##",
                label = "CPF",
                error = uiState.erros["cpf"],
                modifier = Modifier.weight(1f)
            )

            MaskedInput(
                value = uiState.tecnico.telefone,
                onValueChange = { viewModel.updateTecnico(uiState.tecnico.copy(telefone = it)) },
                mask = "(##) #####-####",
                label = "Telefone",
                error = uiState.erros["telefone"],
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GenderDropdown(
                selectedGender = uiState.tecnico.sexo,
                onGenderSelected = { viewModel.updateTecnico(uiState.tecnico.copy(sexo = it)) },
                error = uiState.erros["sexo"],
                modifier = Modifier.weight(1f)
            )

            DatePickerInput(
                selectedDate = uiState.tecnico.dataNasc,
                onDateSelected = { viewModel.updateTecnico(uiState.tecnico.copy(dataNasc = it)) },
                error = uiState.erros["dataNasc"],
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            TextFieldWithError(
                value = uiState.senha,
                onValueChange = { viewModel.updateSenha(it) },
                label = "Senha",
                error = uiState.erros["senha"],
                isPassword = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldWithError(
                value = uiState.confirmarSenha,
                onValueChange = { viewModel.updateConfirmarSenha(it) },
                label = "Confirmar Senha",
                error = uiState.erros["confirmarSenha"],
                isPassword = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        LoadingButton(
            text = "Cadastrar",
            loading = uiState.loading,
            onClick = { viewModel.submitForm() },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Já tem uma conta? Faça Login")
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