package com.tecno.aging.data.repository

import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import kotlinx.coroutines.delay

class ProfileRepository {

    suspend fun getProfile(): Tecnico {
        delay(500)
        return Tecnico(
            matricula = "12345",
            nome = "aaaaaaaaa",
            cpf = "111.222.333-42",
            telefone = "(41) 98888-8888",
            sexo = "Masculino",
            dataNasc = "10/01/1995",
            endereco = Endereco(
                cep = "83408-280",
                logradouro = "Rua das Palmeiras",
                numero = "0",
                complemento = "Casa",
                bairro = "Centro",
                municipio = "Curitiba",
                uf = "PR"
            )
        )
    }

    suspend fun saveProfile(tecnico: Tecnico) {
        delay(1000)
        println("Perfil salvo com sucesso: $tecnico")
    }
}