package com.tecno.aging.data.repository

import android.util.Log
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

    suspend fun getPacienteById(pacienteId: Int): Result<Paciente> {
        Log.d("PROFILE_DEBUG", "4. REPO: Entrando em getPacienteById($pacienteId)")
        return try {
            Log.d("PROFILE_DEBUG", "REPO: Chamando apiService.getPacienteById()...")
            val response = apiService.getPacienteById(pacienteId)
            Log.d("PROFILE_DEBUG", "REPO: Resposta da API recebida. Sucesso: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar dados do paciente: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.d("PROFILE_DEBUG", "REPO: EXCEÇÃO! ${e.javaClass.simpleName}: ${e.message}")
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }
}