package com.tecno.aging.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.screens.cadastro.tecnicoCadastro.CadastroScreen
import com.tecno.aging.ui.screens.forms.FormScreen
import com.tecno.aging.ui.screens.forms.FormsScreen
import com.tecno.aging.ui.screens.forms.TestScreen
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.login.LoginScreen
import com.tecno.aging.ui.screens.pacientes.historicoPaciente.HistoricoScreen
import com.tecno.aging.ui.screens.tecnico.listaDePacientes.PacienteListScreen
import com.tecno.aging.ui.screens.pacientes.perfilPaciente.view.PacienteProfileScreen
import com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit.ProfileEditScreen
import com.tecno.aging.ui.screens.splash.SplashScreen
import com.tecno.aging.ui.screens.tecnico.perfilTecnico.view.TecnicoProfileScreen

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
        composable("cadastro_tecnico") {
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
                perfil = perfil,
                navController = navController,
            )
        }

        composable("test") {
            TestScreen(navController = navController)
        }

        // Formulários
        composable("forms") {
            FormsScreen(navController = navController)
        }

        composable(
            route = "forms/{formFileName}",
            arguments = listOf(navArgument("formFileName") { type = NavType.StringType })
        ) { backStackEntry ->
            val formFileName = backStackEntry.arguments?.getString("formFileName")
            if (formFileName != null) {
                FormScreen(
                    formFileName = formFileName,
                    navController = navController
                )
            }
        }

        // Perfil e Configurações
        composable("tecnico_profile") {
            TecnicoProfileScreen(navController = navController)
        }

        composable("tecnico_profile_edit") {
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

        composable(
            route = "historico_avaliacoes/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) {
            HistoricoScreen(navController = navController)
        }
    }
}
