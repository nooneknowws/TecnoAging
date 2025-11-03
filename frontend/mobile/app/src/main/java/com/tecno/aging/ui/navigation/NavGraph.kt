package com.tecno.aging.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.screens.cadastro.pacienteCadastro.CadastroPacienteScreen
import com.tecno.aging.ui.screens.cadastro.tecnicoCadastro.CadastroScreen
import com.tecno.aging.ui.screens.forgot_password.ForgotPasswordScreen
import com.tecno.aging.ui.screens.forms.FormScreen
import com.tecno.aging.ui.screens.forms.FormsScreen
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.login.LoginScreen
import com.tecno.aging.ui.screens.pacientes.avaliacoes.AvaliacaoDetailScreen
import com.tecno.aging.ui.screens.pacientes.historicoPaciente.HistoricoScreen
import com.tecno.aging.ui.screens.pacientes.perfilPaciente.edit.PacienteEditScreen
import com.tecno.aging.ui.screens.tecnico.listaDePacientes.PacienteListScreen
import com.tecno.aging.ui.screens.pacientes.perfilPaciente.view.PacienteProfileScreen
import com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit.ProfileEditScreen
import com.tecno.aging.ui.screens.splash.SplashScreen
import com.tecno.aging.ui.screens.tecnico.editAvaliacao.AvaliacaoEditScreen
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
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("cadastro_paciente") {
            CadastroPacienteScreen(
                navController = navController,
                onSuccess = {
                    navController.navigate("pacientes_list") {
                        popUpTo("home")
                    }
                }
            )
        }

        // Fluxo principal
        composable("home") {
            HomeScreen(
                navController = navController,
            )
        }

        // Formulários
        composable("forms") {
            FormsScreen(navController = navController)
        }

        composable(
            route = "form/{formId}/{pacienteId}",
            arguments = listOf(
                navArgument("formId") { type = NavType.LongType },
                navArgument("pacienteId") { type = NavType.LongType }
            )
        ) {
            FormScreen(navController = navController)
        }

        composable(
            route = "avaliacao_edit/{avaliacaoId}",
            arguments = listOf(navArgument("avaliacaoId") { type = NavType.LongType })
        ) {
            AvaliacaoEditScreen(navController = navController)
        }

        //recuperar senha

        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController)
        }

        // Perfil e Configurações
        composable("tecnico_profile") {
            TecnicoProfileScreen(navController = navController)
        }

        composable(
            route = "tecnico_profile_edit/{tecnicoId}",
            arguments = listOf(navArgument("tecnicoId") { type = NavType.IntType })
        ) {
            ProfileEditScreen(
                navController = navController
            )
        }

        composable("pacientes_list") {
            PacienteListScreen(
                navController = navController,
                onNavigateToProfile = { pacienteId ->
                    navController.navigate("patient_profile/$pacienteId")
                },
                onNavigateToEditProfile = { pacienteId ->
                    navController.navigate("patient_profile_edit/$pacienteId")
                }
            )
        }

        composable(
            route = "patient_profile/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) {
            PacienteProfileScreen(navController = navController)
        }

        composable(
            route = "patient_profile_edit/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) {
            PacienteEditScreen(navController = navController)
        }

        composable(
            route = "historico_avaliacoes/{pacienteId}",
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) {
            HistoricoScreen(navController = navController)
        }

        composable(
            route = "avaliacao_detail/{avaliacaoId}",
            arguments = listOf(navArgument("avaliacaoId") { type = NavType.LongType })
        ) {
            AvaliacaoDetailScreen(navController = navController)
        }
    }
}
