package com.tecno.aging.ui.components.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tecno.aging.R
import com.tecno.aging.ui.theme.AppColors

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= 30) {
                    onValueChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = "Ícone de Senha") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.visibility_24)
                else
                    painterResource(id = R.drawable.visibility_off_24)

                Icon(
                    painter = image,
                    contentDescription = "Mostrar/Ocultar senha",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            },
            isError = !error.isNullOrEmpty(),

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
                focusedTrailingIconColor = AppColors.Primary,
                unfocusedTrailingIconColor = AppColors.Black,
                errorTrailingIconColor = AppColors.Danger
            )
        )

        if (error != null) {
            Text(
                text = error,
                color = AppColors.Danger,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}