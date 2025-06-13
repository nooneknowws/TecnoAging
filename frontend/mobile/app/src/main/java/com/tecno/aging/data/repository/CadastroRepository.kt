package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.auth.TecnicoRequest
import kotlin.Result

class CadastroRepository(private val apiService: ApiService = RetrofitInstance.api) {

    suspend fun registrarTecnico(request: TecnicoRequest): Result<Unit> {
        return try {
            val response = apiService.registrarTecnico(request)
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