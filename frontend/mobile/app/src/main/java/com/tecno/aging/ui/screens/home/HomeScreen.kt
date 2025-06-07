package com.tecno.aging.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.ui.components.cards.DashboardCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopAppBar(
    name: String,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    navController: NavController,
    modifier: Modifier
)
{
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var expanded by remember { mutableStateOf(false) }


    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Text(
                text = "Bem-vindo(a), $name",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Menu de Perfil",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar Perfil") },
                    onClick = {
                        expanded = false
                        onProfileClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Contato") },
                    onClick = {}
                )
                DropdownMenuItem(
                    text = { Text("Ajuda") },
                    onClick = {}
                )
                DropdownMenuItem(
                    text = { Text("Sair") },
                    onClick = {onLogoutClick()}
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun MainContent(modifier: Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                icon = Icons.Filled.AccountBox,
                title = "Formulários",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("forms") }
            )
            DashboardCard(
                icon = Icons.Filled.Create,
                title = "Ver Resultados",
                modifier = Modifier.weight(1f),
                onClick = {  }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                icon = Icons.Filled.Email,
                title = "Contato",
                modifier = Modifier.weight(1f),
                onClick = {  }
            )
            DashboardCard(
                icon = Icons.Filled.Info,
                title = "Ajuda",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("settings") }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                icon = Icons.Filled.Add,
                title = "Novo Cadastro",
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("cadastro") }
            )
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is HomeViewModel.HomeUiState.LogoutSuccess -> {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenteredTopAppBar(
                modifier = Modifier,
                name = SessionManager.getUserName().toString(),
                onProfileClick = { /* handle profile click */ },
                onLogoutClick = { viewModel.handleEvent(HomeViewModel.HomeEvent.Logout) },
                navController = navController
            )
        }
    ) { innerPadding ->
        MainContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController
        )
    }
}




