package com.tecno.aging.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.ui.navigation.NavItem
import com.tecno.aging.ui.screens.cadastro.CadastroScreen
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.login.LoginScreen
import com.tecno.aging.ui.screens.profile.ProfileScreen
import com.tecno.aging.ui.screens.settings.SettingsScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    SessionManager.init(context)
    val userName = SessionManager.getUserName() ?: ""
    val token = SessionManager.getAuthToken() ?: ""
    val userId = SessionManager.getUserId() ?: ""
    val userProfile = SessionManager.getUserProfile() ?: ""

    val navItems = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Settings", Icons.Default.Settings, "settings"),
        NavItem("Profile", Icons.Default.Person, "profile")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = { Icon(item.icon, item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    name = userName,
                    ID = userId,
                    Perfil = userProfile,
                    navController = navController,
                    onLogout = {},
                    onProfileClick = {}
                )
            }
            composable("settings") { SettingsScreen() }
            composable("profile") { ProfileScreen(profileType = "tecnico") }
            composable("cadastro") {
                CadastroScreen(
                    navController = navController,
                    onSuccess = {
                        navController.navigate("login") {
                            popUpTo("cadastro") { inclusive = true }
                        }
                    }
                )
            }
            composable("login") {
                LoginScreen(
                    navController = navController
                )
            }
        }
    }
}