package com.tecno.aging.data.repository

import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.DTO.AvaliacaoPostDTO
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import kotlin.Result

class AvaliacaoRepository(
    private val apiService: ApiService = RetrofitInstance.api
) {

    suspend fun getAvaliacoesByPaciente(pacienteId: Int): Result<List<HistoricoAvaliacao>> {
        return try {
            val response = apiService.getRespostasByPaciente(pacienteId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar histórico de avaliações: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvaliacaoById(avaliacaoId: Long): Result<HistoricoAvaliacao> {
        return try {
            val response = apiService.getAvaliacaoById(avaliacaoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao buscar detalhes da avaliação"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAvaliacao(avaliacaoId: Long, avaliacao: AvaliacaoPostDTO): Result<Map<String, Any>> {
        return try {
            val response = apiService.updateAvaliacao(avaliacaoId, avaliacao)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Falha ao atualizar avaliação, tente novamente mais tarde"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}