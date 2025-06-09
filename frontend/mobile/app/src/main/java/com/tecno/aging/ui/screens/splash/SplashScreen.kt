package com.tecno.aging.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.data.local.SessionManager

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = viewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.AUTHENTICATED -> {
                navController.navigate("home") { popUpTo(0) }
            }
            AuthState.UNAUTHENTICATED -> {
                SessionManager.clearAuthToken()
                navController.navigate("login") { popUpTo(0) }
            }
            AuthState.LOADING -> { /* Não faz nada, apenas espera */ }
        }
    }

    // UI da tela de splash
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator() // Ou sua logo, etc.
    }
}