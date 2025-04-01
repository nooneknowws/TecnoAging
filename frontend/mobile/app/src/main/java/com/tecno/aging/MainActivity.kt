package com.tecno.aging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tecno.aging.ui.theme.TecnoAgingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TecnoAgingTheme {
                MainScreen()
//                val navController = rememberNavController()
//                NavHost(
//                    navController = navController,
//                    startDestination = "login"
//                ) {
//                    composable("login") { LoginScreen(navController) }
//                    composable("cadastro") { CadastroScreen(navController) }
//                    composable("home") { HomeScreen(navController) }
//                }
            }
        }
    }
  }



