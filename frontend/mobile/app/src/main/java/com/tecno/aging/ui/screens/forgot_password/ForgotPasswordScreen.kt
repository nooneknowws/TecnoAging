package com.tecno.aging.ui.screens.forgot_password

import MaskedInput
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.fields.PasswordTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.resetSuccess) {
        if (uiState.resetSuccess) {
            Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Senha") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.currentStep,
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
            }, label = "StepAnimation"
        ) { step ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                    1 -> Step1_EnterCpf(uiState, viewModel)
                    2 -> Step2_EnterOtp(uiState, viewModel)
                    3 -> Step3_ResetPassword(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
private fun Step1_EnterCpf(uiState: ForgotPasswordUiState, viewModel: ForgotPasswordViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Insira seu CPF", style = MaterialTheme.typography.headlineSmall)
        Text("Enviaremos um código de verificação para o seu celular.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))

        MaskedInput(
            value = uiState.cpf,
            onValueChange = viewModel::onCpfChange,
            mask = "###.###.###-##",
            label = "CPF",
            error = uiState.errorMessage,
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorMessage != null && uiState.cpf.length != 11) {
            Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.weight(1f))

        ButtonComponent(
            title = "Enviar Código",
            onClick = {
                viewModel.clearError()
                viewModel.sendOtp()
            },
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun Step2_EnterOtp(uiState: ForgotPasswordUiState, viewModel: ForgotPasswordViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Verifique seu Celular", style = MaterialTheme.typography.headlineSmall)
        Text(
            text = "Insira o código de 6 dígitos que enviamos para ${uiState.telefoneMascarado}.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        OtpInput(
            otpValues = uiState.otp,
            onOtpChanged = viewModel::onOtpChange
        )

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.weight(1f))

        ButtonComponent(
            title = "Verificar Código",
            onClick = {
                viewModel.clearError()
                viewModel.verifyOtp()
            },
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun Step3_ResetPassword(uiState: ForgotPasswordUiState, viewModel: ForgotPasswordViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crie uma Nova Senha", style = MaterialTheme.typography.headlineSmall)
        Text("Sua nova senha deve ser diferente da anterior.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))

        PasswordTextField(
            value = uiState.newPassword,
            onValueChange = viewModel::onNewPasswordChange,
            label = "Nova Senha",
            error = null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        PasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = "Confirmar Nova Senha",
            error = null,
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.weight(1f))

        ButtonComponent(
            title = "Redefinir Senha",
            onClick = {
                viewModel.clearError()
                viewModel.resetPassword()
            },
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun OtpInput(
    otpValues: List<String>,
    onOtpChanged: (index: Int, value: String) -> Unit
) {
    val otpCount = otpValues.size
    val focusRequesters = remember { List(otpCount) { FocusRequester() } }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(otpCount) { index ->
            OutlinedTextField(
                value = otpValues[index],
                onValueChange = {
                    val newValue = it.takeLast(1)
                    onOtpChanged(index, newValue)
                    if (newValue.isNotEmpty() && index < otpCount - 1) {
                        focusRequesters[index + 1].requestFocus()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequesters[index]),
                textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}