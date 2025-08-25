
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInput(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerState = rememberDatePickerState()

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            // CORRIGIDO: Usando o parâmetro 'label'
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Selecionar data")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(dateFormatter.format(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
