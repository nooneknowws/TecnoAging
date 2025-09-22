package com.tecno.aging.ui.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.ui.components.forms.TextFieldWithError
import com.tecno.aging.ui.theme.cleanTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSection(
    address: Endereco,
    onAddressChange: (Endereco) -> Unit,
    onCepSearch: () -> Unit,
    loadingCep: Boolean,
    cepError: String?,
    modifier: Modifier = Modifier
) {
    var expandedUF by remember { mutableStateOf(false) }
    val estadosBrasileiros = listOf(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
        "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
        "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    )

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Endereço",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextFieldWithError(
                value = address.cep,
                onValueChange = { onAddressChange(address.copy(cep = it.filter { c -> c.isDigit() })) },
                label = "CEP",
                error = cepError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onCepSearch,
                modifier = Modifier.padding(start = 8.dp),
                enabled = !loadingCep && address.cep.length == 8
            ) {
                if (loadingCep) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buscar")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address.logradouro,
            onValueChange = { onAddressChange(address.copy(logradouro = it)) },
            label = { Text("Logradouro") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = address.numero,
                onValueChange = { onAddressChange(address.copy(numero = it)) },
                label = { Text("Número") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = address.complemento,
                onValueChange = { onAddressChange(address.copy(complemento = it)) },
                label = { Text("Complemento") },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedTextField(
            value = address.bairro,
            onValueChange = { onAddressChange(address.copy(bairro = it)) },
            label = { Text("Bairro") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = address.municipio ?: "",
                onValueChange = { onAddressChange(address.copy(municipio = it)) },
                label = { Text("Município") },
                modifier = Modifier.weight(2f),
                colors = cleanTextFieldColors()
            )

            ExposedDropdownMenuBox(
                expanded = expandedUF,
                onExpandedChange = { expandedUF = it },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    readOnly = true,
                    value = address.uf ?: "",
                    onValueChange = { },
                    label = { Text("UF") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUF)
                    },
                    modifier = Modifier.menuAnchor(),
                    colors = cleanTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedUF,
                    onDismissRequest = { expandedUF = false }
                ) {
                    estadosBrasileiros.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                onAddressChange(address.copy(uf = estado))
                                expandedUF = false
                            }
                        )
                    }
                }
            }
        }
    }
}
