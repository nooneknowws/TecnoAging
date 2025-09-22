package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.domain.models.pessoa.paciente.PacienteRequest
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

    suspend fun getPacienteById(pacienteId: Int): Result<Paciente> {
        return try {
            val response = apiService.getPacienteById(pacienteId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar dados do paciente: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }

    suspend fun registrarPaciente(request: PacienteRequest): Result<Unit> {
        return try {
            val response = apiService.registrarPaciente(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                Result.failure(Exception("Falha no cadastro: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}