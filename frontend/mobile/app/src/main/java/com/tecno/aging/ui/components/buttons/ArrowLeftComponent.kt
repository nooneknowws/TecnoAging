package com.tecno.aging.ui.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.ui.theme.AppColors.Black

@Composable
fun ArrowLeftComponent(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "Voltar",
        tint = Black,
        modifier = modifier
            .padding(start = 0.dp)
            .size(28.dp)
            .clickable { navController.popBackStack() }
    )
}
