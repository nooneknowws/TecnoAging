package com.tecno.aging.ui.navigation

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.screens.cadastro.CadastroScreen
import com.tecno.aging.ui.screens.forms.FormScreen
import com.tecno.aging.ui.screens.forms.IVCF20FormScreen
import com.tecno.aging.ui.screens.forms.MeemFormScreen
import com.tecno.aging.ui.screens.forms.SedentarismoFormScreen
import com.tecno.aging.ui.screens.forms.TestScreen
import com.tecno.aging.ui.screens.forms.pittsburghFatigabilityScreen
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.login.LoginScreen
import com.tecno.aging.ui.screens.pacientes.PacienteListScreen
import com.tecno.aging.ui.screens.pacientes.PacienteProfileScreen
import com.tecno.aging.ui.screens.profile.ProfileEditScreen
import com.tecno.aging.ui.screens.profile.ProfileScreen
import com.tecno.aging.ui.screens.settings.SettingsScreen
import com.tecno.aging.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val context = LocalContext.current
    SessionManager.init(context)

    val navController = rememberNavController()
    val startRoute = "splash"

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

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
                navController = navController,
                onLogout = {},
                onProfileClick = {}
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

        composable("forms/sedentarismo") {
            SedentarismoFormScreen(onSubmit = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }, navController = navController)
        }

        composable("forms/meem") {
            MeemFormScreen(onSubmit = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }, navController = navController)
        }

        // Perfil e Configurações
        composable("profile") {
            ProfileScreen(profileType = "tecnico", navController = navController)
        }

        composable("profile_edit") {
            ProfileEditScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("pacientes_list") {
            PacienteListScreen(
                onNavigateToProfile = { pacienteId ->
                    navController.navigate("patient_profile/$pacienteId")
                },
                onNavigateToEditProfile = { /* ... */ }
            )
        }

        composable(
            route = "patient_profile/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) {
            PacienteProfileScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}
