package com.tecno.aging.ui.components.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecno.aging.ui.theme.AppColors

@Composable
fun CpfTextField(
    value: String,
    error: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= 14) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("CPF") },
            leadingIcon = { Icon(Icons.Rounded.AccountCircle, contentDescription = "Ícone de CPF") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            isError = error.isNotEmpty(),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColors.White,
                unfocusedContainerColor = AppColors.White,
                errorContainerColor = AppColors.White,
                cursorColor = AppColors.Primary,
                focusedIndicatorColor = AppColors.Primary,
                unfocusedIndicatorColor = AppColors.Black,
                errorIndicatorColor = AppColors.Danger,
                focusedLabelColor = AppColors.Primary,
                unfocusedLabelColor = AppColors.Gray500,
                errorLabelColor = AppColors.Danger,
                focusedLeadingIconColor = AppColors.Primary,
                unfocusedLeadingIconColor = AppColors.Black,
                errorLeadingIconColor = AppColors.Danger,
            )
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = AppColors.Danger,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}