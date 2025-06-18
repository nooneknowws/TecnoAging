package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.ui.components.cards.FormCard
import com.tecno.aging.ui.theme.AppColors

data class FormItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsScreen(navController: NavController) {
    val forms = listOf(
        FormItem("FACT-F", Icons.Default.MedicalServices, "factf.json"),
        FormItem(
            "Índice de Vulnerabilidade Clínico-Funcional",
            Icons.Default.PlaylistAddCheck,
            "forms/ivcf-20.json"
        ),
        FormItem("Fatigabilidade de Pittsburgh", Icons.Default.BatteryAlert, "forms/pfs.json"),
        FormItem("Sedentarismo", Icons.Default.Chair, "forms/sedentarismo.json"),
        FormItem("Mini Exame do Estado Mental", Icons.Default.Psychology, "forms/minimental.json"),
        FormItem("Comparar Resultados", Icons.Default.CompareArrows, "profile")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Formulários de Avaliação",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecione um formulário abaixo para iniciar a avaliação com o idoso. Cada teste auxilia na identificação de diferentes aspectos da saúde e bem-estar.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(forms) { form ->
                    FormCard(
                        title = form.title,
                        icon = form.icon,
                        onClick = {
                            if (form.route.endsWith(".json")) {
                                navController.navigate("forms/${form.route}")
                            } else {
                                navController.navigate(form.route)
                            }
                        }
                    )
                }
            }
        }
    }
}


