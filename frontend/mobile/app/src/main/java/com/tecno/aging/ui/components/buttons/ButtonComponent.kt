package com.tecno.aging.ui.components.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecno.aging.ui.theme.AppColors

enum class ButtonVariant { Primary, Secondary, Danger }

@Composable
fun ButtonComponent(
    title: String,
    variant: ButtonVariant = ButtonVariant.Primary,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val (bg, content) = when (variant) {
        ButtonVariant.Primary   -> ColorPair(AppColors.Primary, Color.White)
        ButtonVariant.Secondary -> ColorPair(AppColors.Blue100, AppColors.Primary)
        ButtonVariant.Danger    -> ColorPair(Color(0xFFDC3545), Color.White)
    }

    Button(
        onClick = onClick,
        enabled = !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = content
        ),
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .height(42.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp),
                strokeWidth = 2.dp,
                color = content
            )
        } else {
            Text(
                text = title,
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private data class ColorPair(val background: Color, val content: Color)