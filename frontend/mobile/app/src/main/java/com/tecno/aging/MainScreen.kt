package com.tecno.aging

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.tecno.aging.ui.screens.home.HomeScreen
import com.tecno.aging.ui.screens.profile.ProfileScreen
import com.tecno.aging.ui.screens.settings.SettingsScreen


@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val items = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Menu", Icons.Default.Menu, "Menu"),
        NavItem("Perfil", Icons.Default.Person, "profile"),
        NavItem("settings", Icons.Default.Settings, "settings")
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
   when (selectedIndex) {
       0 -> HomeScreen()
       1 -> SettingsScreen()
       2 -> ProfileScreen()
       3 -> SettingsScreen()
   }
}
