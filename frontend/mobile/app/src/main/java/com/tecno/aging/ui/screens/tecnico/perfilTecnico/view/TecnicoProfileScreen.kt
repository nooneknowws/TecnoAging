package com.tecno.aging.ui.screens.tecnico.perfilTecnico.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileType: String,
    navController: NavController
) {
    // Mock de dados
    val profile = remember {
        Tecnico(
            matricula = "2021001",
            nome = "Carlos Souza",
            cpf = "123.456.789-00",
            telefone = "(11) 91234-5678",
            sexo = "Masculino",
            dataNasc = "15/08/1985",
            endereco = Endereco(
                cep = "01234-567",
                logradouro = "Rua das Flores",
                numero = "123",
                complemento = "Apto 45",
                bairro = "Jardim Primavera",
                municipio = "São Paulo",
                uf = "SP"
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil do Técnico") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Foto de perfil mock
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botão Editar para técnico
            if (profileType == "tecnico") {
                Button(
                    onClick = { navController.navigate("profile_edit") },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Editar Perfil")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Dados do técnico
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dados Pessoais", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    DataRow(label = "Matrícula", value = profile.matricula)
                    DataRow(label = "Nome", value = profile.nome)
                    DataRow(label = "CPF", value = profile.cpf)
                    DataRow(label = "Telefone", value = profile.telefone)
                    DataRow(label = "Sexo", value = profile.sexo)
                    DataRow(label = "Data de Nascimento", value = profile.dataNasc)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Endereço", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    DataRow(label = "CEP", value = profile.endereco.cep)
                    DataRow(label = "Logradouro", value = profile.endereco.logradouro)
                    DataRow(label = "Número", value = profile.endereco.numero)
                    DataRow(label = "Complemento", value = profile.endereco.complemento)
                    DataRow(label = "Bairro", value = profile.endereco.bairro)
                    DataRow(label = "Município", value = profile.endereco.municipio)
                    DataRow(label = "UF", value = profile.endereco.uf)
                }
            }
        }
    }
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label:",
            modifier = Modifier.width(140.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
