// LoginScreen.kt
package com.tecno.aging.ui.screens.login


import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.LoginButton
import com.tecno.aging.ui.components.fields.CpfTextField
import com.tecno.aging.ui.components.fields.PasswordTextField

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LoginContent(
        state = state,
        onEvent = viewModel::handleEvent,
        onSignUpClick = { navController.navigate("cadastro") },
        onForgotPasswordClick = { /* Handle forgot password */ }
    )
}
@Composable
private fun LoginContent(
    state: LoginViewModel.LoginUiState,
    onEvent: (LoginViewModel.LoginEvent) -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TecnoAging",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        CpfTextField(
            value = state.cpf,
            error = state.cpfError,
            onValueChange = { onEvent(LoginViewModel.LoginEvent.CpfChanged(it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            value = state.password,
            error = state.passwordError,
            passwordVisible = state.passwordVisible,
            onValueChange = { onEvent(LoginViewModel.LoginEvent.PasswordChanged(it)) },
            onToggleVisibility = { onEvent(LoginViewModel.LoginEvent.ToggleVisibility) }
        )

        if (state.loginError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = state.loginError, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LoginButton(
            isLoading = state.isLoading,
            enabled = !state.isLoading,
            onClick = { onEvent(LoginViewModel.LoginEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esqueceu a senha?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable(onClick = onForgotPasswordClick)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            Text(text = "Não tem uma conta?")
            Text(
                text = " Cadastre-se",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(onClick = onSignUpClick)
            )
        }
    }
}