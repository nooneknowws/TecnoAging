package com.tecno.aging.ui.components.masks

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MaskedTextField(
    mask: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    var displayText by remember { mutableStateOf("") }

    LaunchedEffect(value) {
        displayText = applyMask(value, mask)
    }

    OutlinedTextField(
        value = displayText,
        onValueChange = { newValue ->
            val rawText = newValue.filter { c -> c.isDigit() }
            onValueChange(rawText)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = MaskTransformation(mask),
        modifier = modifier,
        isError = error != null,
        supportingText = { error?.let { Text(it) } }
    )
}

private fun applyMask(text: String, mask: String): String {
    var maskedText = ""
    var textIndex = 0

    mask.forEach { char ->
        if (textIndex >= text.length) return@forEach
        when (char) {
            '#' -> {
                maskedText += text.getOrNull(textIndex) ?: ""
                textIndex++
            }
            else -> maskedText += char
        }
    }
    return maskedText
}

class MaskTransformation(private val mask: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(applyMask(text.text, mask)),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset + mask.take(offset).count { it != '#' }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset - mask.take(offset).count { it != '#' }
                }
            }
        )
    }
}