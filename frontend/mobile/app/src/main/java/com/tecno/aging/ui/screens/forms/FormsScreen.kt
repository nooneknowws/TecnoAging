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
import androidx.compose.ui.text.style.TextAlign
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
        FormItem("Sedentarismo", Icons.Default.AccountBox, "forms/sedentarismo"),
        FormItem("Mini-Mental", Icons.Default.Menu, "forms/meem"),
        FormItem("Ver Resultados", Icons.Default.Create, "profile"),
        FormItem("Comparar Resultados", Icons.Default.Info, "profile")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArrowLeftComponent(navController = navController)

        Text(
            text = "Formulários de Avaliação",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Selecione um formulário abaixo para iniciar a avaliação com o idoso. Cada teste auxilia na identificação de diferentes aspectos da saúde e bem-estar.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
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


