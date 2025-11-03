package com.tecno.aging.ui.screens.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.ui.components.buttons.ButtonComponent
import com.tecno.aging.ui.components.buttons.ButtonVariant
import com.tecno.aging.ui.components.fields.CpfTextField
import com.tecno.aging.ui.components.fields.PasswordTextField
import com.tecno.aging.ui.theme.AppColors

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
        onSignUpClick = { navController.navigate("cadastro_paciente") },
        onForgotPasswordClick = { navController.navigate("forgot_password") }
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 6.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Image(
            painter = painterResource(id = R.drawable.icntecno),
            contentDescription = "Logo TecnoAging",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        CpfTextField(
            value = state.cpf,
            error = state.cpfError,
            onValueChange = { onEvent(LoginViewModel.LoginEvent.CpfChanged(it)) },
            modifier = componentWidthModifier
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = state.password,
            onValueChange = { onEvent(LoginViewModel.LoginEvent.PasswordChanged(it)) },
            label = "Senha",
            error = state.passwordError,
            modifier = componentWidthModifier
        )

        Spacer(modifier = Modifier.height(24.dp))

        ButtonComponent(
            title = "Entrar",
            loading = state.isLoading,
            modifier = componentWidthModifier,
            onClick = { onEvent(LoginViewModel.LoginEvent.Submit) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Recuperar senha",
            color = AppColors.Blue700,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onForgotPasswordClick() }
        )

        if (state.loginError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = state.loginError,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ButtonComponent(
            title = "Criar conta",
            variant = ButtonVariant.Secondary,
            modifier = componentWidthModifier,
            onClick = onSignUpClick,
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}