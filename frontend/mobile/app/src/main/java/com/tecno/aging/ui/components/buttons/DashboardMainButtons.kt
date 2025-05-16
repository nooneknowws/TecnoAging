package com.tecno.aging.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DashboardMainButtons(
    navController: NavController,
    hasFinishedLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!hasFinishedLoading) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .background(
                                color = Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            } else {
                DashboardButton(
                    title = "Formulários",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AccountBox,
                            contentDescription = "Formulários",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        // todo
                    },
                    modifier = Modifier.weight(1f)
                )
                DashboardButton(
                    title = "Ver Resultados",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "Ver Resultados",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        // todo
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (!hasFinishedLoading) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .background(
                                color = Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            } else {
                DashboardButton(
                    title = "Contato",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Contato",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        // todo
                    },
                    modifier = Modifier.weight(1f)
                )
                DashboardButton(
                    title = "Ajuda",
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Ajuda",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        // todo
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
