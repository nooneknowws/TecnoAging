// LoginScreen.kt
package com.tecno.aging.ui.screens.login


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.buttons.ButtonVariant
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
    val componentWidthModifier = Modifier
        .fillMaxWidth(0.9f)
        .height(52.dp)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE4E9FC)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TecnoAging",
                fontSize = 32.sp,
                color = Color(0xFF3C3C3C),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            CpfTextField(
                value = state.cpf,
                error = state.cpfError,
                onValueChange = { onEvent(LoginViewModel.LoginEvent.CpfChanged(it)) },
                modifier = componentWidthModifier
            )

            Spacer(modifier = Modifier.height(24.dp))

            PasswordTextField(
                value = state.password,
                error = state.passwordError,
                passwordVisible = state.passwordVisible,
                onValueChange = { onEvent(LoginViewModel.LoginEvent.PasswordChanged(it)) },
                onToggleVisibility = { onEvent(LoginViewModel.LoginEvent.ToggleVisibility) },
                modifier = componentWidthModifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Recuperar senha",
                color = Color(0xFF5F6DF5),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable { onForgotPasswordClick() }
                    .align(Alignment.End)
            )

            if (state.loginError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.loginError,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            ButtonComponent(
                title = "Login",
                loading = state.isLoading,
                onClick = { onEvent(LoginViewModel.LoginEvent.Submit) },
                modifier = componentWidthModifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            ButtonComponent(
                title = "Cadastro",
                variant = ButtonVariant.Secondary,
                onClick = onSignUpClick,
                modifier = componentWidthModifier
            )
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    LoginContent(
        state = LoginViewModel.LoginUiState(
            cpf = "123.456.789-00",
            password = "senha123",
            passwordVisible = true,
            cpfError = "",
            passwordError = "",
            loginError = "",
            isLoading = false,
            loginSuccess = false
        ),
        onEvent = {},
        onSignUpClick = {},
        onForgotPasswordClick = {}
    )
}