package com.tecno.aging.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.repository.AuthRepository
import com.tecno.aging.ui.components.cards.DashboardCard
import com.tecno.aging.ui.theme.AppColors
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val drawerItems = if (uiState.userProfile.equals("PACIENTE", ignoreCase = true)) {
        listOf(
            NavigationItem("Dashboard", Icons.Outlined.Home, "home"),
            NavigationItem("Meu Perfil", Icons.Outlined.Person, "paciente_profile"),
            NavigationItem("Meu Histórico", Icons.Outlined.History, "historico_avaliacoes"),
            NavigationItem("Iniciar Novo Teste", Icons.Outlined.PlayCircleOutline, "forms"),
            NavigationItem("Sair", Icons.AutoMirrored.Outlined.ExitToApp, "logout")
        )
    } else {
        listOf(
            NavigationItem("Dashboard", Icons.Outlined.Home, "home"),
            NavigationItem("Meu Perfil", Icons.Outlined.Person, "tecnico_profile"),
            NavigationItem("Lista de Pacientes", Icons.Outlined.Groups, "pacientes_list"),
            NavigationItem("Formulários", Icons.Outlined.Description, "forms"),
            NavigationItem("Sair", Icons.AutoMirrored.Outlined.ExitToApp, "logout")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                drawerItems = drawerItems,
                onItemClick = { item ->
                    scope.launch { drawerState.close() }

                    when (item.route) {
                        "home" -> navController.navigate("home")
                        "logout" -> {
                            scope.launch {
                                AuthRepository().logout()
                                SessionManager.clearAuthToken()
                                navController.navigate("login") { popUpTo(0) }
                            }
                        }
                        "tecnico_profile" -> navController.navigate("tecnico_profile")
                        "pacientes_list" -> navController.navigate("pacientes_list")
                        "forms" -> navController.navigate("forms")
                        "paciente_profile" -> navController.navigate("patient_profile/${uiState.userId}")
                        "historico_avaliacoes" -> navController.navigate("historico_avaliacoes/${uiState.userId}")
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                CenteredTopAppBar(
                    name = uiState.userName,
                    onMenuClick = { scope.launch { drawerState.open() } })
            },
            modifier = modifier
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = viewModel::refresh,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                MainContent(
                    perfil = uiState.userProfile,
                    navController = navController,
                    id = uiState.userId
                )
            }
        }
    }
}


@Composable
fun AppDrawerContent(
    drawerItems: List<NavigationItem>,
    onItemClick: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<NavigationItem?>(null) }

    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = AppColors.Gray50
    ) {
        Spacer(Modifier.height(16.dp))
        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = item == selectedItem,
                onClick = {
                    selectedItem = item
                    onItemClick(item)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = AppColors.Black,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = AppColors.Black,
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopAppBar(
    name: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Abrir menu de navegação",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}


@Composable
fun MainContent(
    perfil: String,
    navController: NavController,
    id: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (perfil.equals("TECNICO", ignoreCase = true)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.list_pacientes),
                            contentDescription = "Lista de Pacientes",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Lista de Pacientes",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("pacientes_list") }
                )
                DashboardCard(
                    icon = {
                        Icon(
                            Icons.Filled.AccountBox,
                            contentDescription = "Formulários",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Formulários",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("forms") }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_perfil),
                            contentDescription = "Atualizar Perfil",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Atualizar Perfil",
                    modifier = Modifier.weight(0.5f),
                    onClick = {
                        navController.navigate("tecnico_profile_edit/$id")
                    }
                )
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Cadastrar Tecnico",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Cadastrar Tecnico",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("cadastro_tecnico") }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Cadastrar Paciente",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Cadastrar Paciente",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("cadastro_paciente") }
                )
            }
        } else if (perfil.equals("PACIENTE", ignoreCase = true)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Iniciar Novo Teste",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Iniciar Novo Teste",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("forms") }
                )
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_perfil),
                            contentDescription = "Atualizar Perfil",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Atualizar Perfil",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("patient_profile_edit/$id") }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.resultados),
                            contentDescription = "Últimas Avaliações",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Histórico de Avaliações",
                    modifier = Modifier.weight(0.5f),
                    onClick = { navController.navigate("historico_avaliacoes/$id") }
                )
            }
        }

    }
}