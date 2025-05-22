package com.tecno.aging.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.screens.cadastro.CadastroScreen
import com.tecno.aging.ui.screens.forms.FormScreen
import com.tecno.aging.ui.screens.forms.IVCF20FormScreen
import com.tecno.aging.ui.screens.forms.TestScreen
import com.tecno.aging.ui.screens.forms.pittsburghFatigabilityScreen
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.login.LoginScreen
import com.tecno.aging.ui.screens.profile.ProfileScreen
import com.tecno.aging.ui.screens.settings.SettingsScreen

@Composable
fun AppNavGraph() {
    val context = LocalContext.current
    SessionManager.init(context)

    val navController = rememberNavController()
    //val startRoute = if (SessionManager.getAuthToken().isNullOrEmpty()) "login" else "home"
    val startRoute = "home"

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        // Autenticação
        composable("login") {
            LoginScreen(navController)
        }
        composable("cadastro") {
            CadastroScreen(
                navController = navController,
                onSuccess = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Fluxo principal
        composable("home") {
            val name = SessionManager.getUserName().orEmpty()
            val id = SessionManager.getUserId().orEmpty()
            val perfil = SessionManager.getUserProfile().orEmpty()

            HomeScreen(
                name = name,
                ID = id,
                Perfil = perfil,
                navController = navController
            )
        }

        composable("test") {
            TestScreen(navController = navController)
        }

        // Formulários
        composable("forms") {
            FormScreen(navController = navController)
        }

        composable("forms/ivcf20") {
            IVCF20FormScreen(onSubmit = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }, navController = navController)
        }
        composable("forms/pittsburgh") {
            pittsburghFatigabilityScreen(onSubmit = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            })
        }

        // Perfil e Configurações
        composable("profile") {
            ProfileScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
    }
}
