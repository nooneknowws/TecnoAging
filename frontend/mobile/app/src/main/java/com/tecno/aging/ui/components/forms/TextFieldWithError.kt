package com.tecno.aging.ui.components.forms

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextFieldWithError(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        isError = error != null,
        supportingText = {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    if (passwordVisible) {
                        Text("👁️", style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text("👁️🗨️", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    )
}