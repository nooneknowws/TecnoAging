package com.tecno.aging.ui.components.forms

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MaskedInput(
    value: String,
    onValueChange: (String) -> Unit,
    mask: String,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    val visualTransformation = remember(mask) {
        MaskTransformation(mask)
    }

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val filtered = newValue.filter { it.isDigit() }
            if (filtered.length <= mask.count { it == '#' }) {
                onValueChange(filtered)
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(label) },
        modifier = modifier,
        isError = error != null,
        supportingText = { error?.let { Text(text = it) } }
    )
}

class MaskTransformation(private val mask: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(mask.count { it == '#' })

        var out = ""
        var maskIndex = 0
        trimmed.forEach { char ->
            while (maskIndex < mask.length && mask[maskIndex] != '#') {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return out.take(offset).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return out.take(offset).filter { it.isDigit() }.length
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}