import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.tecno.aging.ui.components.masks.MaskTransformation

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
        modifier = modifier
            .fillMaxWidth(),
        textStyle = LocalTextStyle.current,
        isError = error != null,
        supportingText = { error?.let { Text(text = it) } },
    )
}