package com.tecno.aging.ui.screens.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.ui.components.buttons.ArrowLeftComponent
import com.tecno.aging.ui.components.cards.FormCard

data class FormItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun FormScreen(navController: NavController) {
    val forms = listOf(
        FormItem("IVCF-20", Icons.Default.Send, "forms/ivcf20"),
        FormItem("Fatigabilidade de Pittsburgh", Icons.Default.Place, "forms/pittsburgh"),
        FormItem("Sedentarismo", Icons.Default.AccountBox, "settings"), // ajuste temporário
        FormItem("Mini-Mental", Icons.Default.Menu, "profile"), // ajuste temporário
        FormItem("Ver Resultados", Icons.Default.Create, "profile"), // nova funcionalidade
        FormItem("Comparar Resultados", Icons.Default.Info, "profile") // nova funcionalidade
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArrowLeftComponent(navController = navController)

        Text(
            text = "Formulários disponíveis",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        forms.forEach { form ->
            FormCard(
                title = form.title,
                icon = form.icon,
                onClick = { navController.navigate(form.route) }
            )
        }
    }
}


