package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.paciente.Paciente
import kotlin.Result

class PacienteRepository(
    private val apiService: ApiService = RetrofitInstance.api
) {
    suspend fun getPacientes(): Result<List<Paciente>> {
        return try {
            val response = apiService.getPacientes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar pacientes: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }
}