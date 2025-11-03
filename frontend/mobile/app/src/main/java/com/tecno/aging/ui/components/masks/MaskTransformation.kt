package com.tecno.aging.ui.components.masks

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MaskTransformation(val mask: String) : VisualTransformation {

    private val placeholder = '#'

    override fun filter(text: AnnotatedString): TransformedText {
        var aIndex = 0
        val masked = buildString {
            mask.forEach { maskChar ->
                if (aIndex < text.length) {
                    if (maskChar == placeholder) {
                        append(text[aIndex])
                        aIndex++
                    } else {
                        append(maskChar)
                    }
                } else {
                    return@forEach
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformedOffset = offset
                var maskIndex = 0
                while (maskIndex < transformedOffset && maskIndex < mask.length) {
                    if (mask[maskIndex] != placeholder) {
                        transformedOffset++
                    }
                    maskIndex++
                }
                return transformedOffset
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = offset
                var maskIndex = 0
                while (maskIndex < offset && maskIndex < mask.length) {
                    if (mask[maskIndex] != placeholder) {
                        originalOffset--
                    }
                    maskIndex++
                }
                return originalOffset
            }
        }

        return TransformedText(AnnotatedString(masked), offsetMapping)
    }
}