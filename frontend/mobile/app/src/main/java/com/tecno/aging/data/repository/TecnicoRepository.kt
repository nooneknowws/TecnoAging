package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import kotlin.Result

class TecnicoRepository(
    private val apiService: ApiService = RetrofitInstance.api
) {
    suspend fun getTecnicoById(tecnicoId: Int): Result<Tecnico> {
        return try {
            val response = apiService.getTecnicoById(tecnicoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar dados do técnico: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}