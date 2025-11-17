package com.tecno.aging.data.repository

import android.util.Log
import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.ImageUploadRequest
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
            Log.d("PacienteRepository", "Buscando paciente com ID: $pacienteId")
            val response = apiService.getPacienteById(pacienteId)
            Log.d("PacienteRepository", "Resposta recebida: code=${response.code()}, isSuccessful=${response.isSuccessful}, body is null=${response.body() == null}")

            if (response.isSuccessful && response.body() != null) {
                val paciente = response.body()!!
                Log.d("PacienteRepository", "Paciente encontrado: nome=${paciente.nome}, idade=${paciente.idade}")
                Result.success(paciente)
            } else {
                val errorMsg = "Falha ao buscar dados do paciente: code=${response.code()}, message=${response.message()}, body is null=${response.body() == null}"
                Log.e("PacienteRepository", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("PacienteRepository", "Exceção ao buscar paciente: ${e.message}", e)
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

    suspend fun updatePaciente(pacienteId: Int, paciente: Paciente): Result<Paciente> {
        return try {
            val response = apiService.updatePaciente(pacienteId, paciente)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                Result.failure(Exception("Falha ao atualizar paciente: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }

    suspend fun getFotoPaciente(pacienteId: Int): Result<String> {
        return try {
            val response = apiService.getFotoPaciente(pacienteId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.image)
            } else if (response.code() == 404) {
                Result.success("")
            } else {
                Result.failure(Exception("Falha ao buscar foto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadFotoPaciente(pacienteId: Int, base64Image: String): Result<Unit> {
        return try {
            val request = ImageUploadRequest(image = base64Image)
            val response = apiService.uploadFotoPaciente(pacienteId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Falha ao enviar foto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}