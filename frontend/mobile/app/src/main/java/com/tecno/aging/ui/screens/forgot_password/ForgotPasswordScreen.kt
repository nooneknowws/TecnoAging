package com.tecno.aging.ui.screens.forgot_password

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import com.tecno.aging.ui.screens.login.LoginViewModel

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
                    1 -> Step1_EnterEmail(uiState, viewModel)
                    2 -> Step2_EnterOtp(uiState, viewModel)
                    3 -> Step3_ResetPassword(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
private fun Step1_EnterEmail(uiState: ForgotPasswordUiState, viewModel: ForgotPasswordViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Insira seu e-mail", style = MaterialTheme.typography.headlineSmall)
        Text("Enviaremos um código de verificação para o seu e-mail.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errorMessage != null
        )

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
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
        Text("Verifique seu E-mail", style = MaterialTheme.typography.headlineSmall)
        Text("Insira o código de 5 dígitos que enviamos.", textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))

        OtpInput(
            otpValues = uiState.otp,
            onOtpChanged = viewModel::onOtpChange
        )

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
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
            error = "",
            passwordVisible = true,
            onToggleVisibility = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        PasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            error = "",
            passwordVisible = true,
            onToggleVisibility = {},
            modifier = Modifier.fillMaxWidth()
        )

        if (uiState.errorMessage != null) {
            Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
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
    val focusRequesters = remember { List(5) { FocusRequester() } }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                value = value,
                onValueChange = {
                    onOtpChanged(index, it)
                    if (it.isNotEmpty() && index < 4) {
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