package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.auth.EnviarCodigoRequest
import com.tecno.aging.domain.models.auth.EnviarCodigoResponse
import com.tecno.aging.domain.models.auth.ResetPasswordDTO
import kotlin.Result

class AuthRepository(private val apiService: ApiService = RetrofitInstance.api) {

    suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Falha na chamada de logout: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyJwt(): Result<Boolean> {
        return try {
            val response = apiService.verifyJwt()

            if (response.isSuccessful && response.body()?.valid == true) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enviarCodigo(cpf: String): Result<EnviarCodigoResponse> {
        return try {
            val response = apiService.enviarCodigoRecuperacao(EnviarCodigoRequest(cpf = cpf))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = RetrofitInstance.parseErrorBody(errorBody)
                    ?: "CPF não encontrado ou inválido"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }

    suspend fun resetarSenha(dto: ResetPasswordDTO): Result<String> {
        return try {
            val response = apiService.resetarSenha(dto)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()?.get("message") ?: "Sucesso")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = RetrofitInstance.parseErrorBody(errorBody)
                    ?: "Código inválido ou senhas não coincidem"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }
}