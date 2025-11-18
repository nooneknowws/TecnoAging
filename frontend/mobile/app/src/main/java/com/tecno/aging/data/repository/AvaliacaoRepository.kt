package com.tecno.aging.data.repository

import android.util.Log
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
            Log.d("AvaliacaoRepository", "Buscando avaliações para pacienteId=$pacienteId")
            val response = apiService.getRespostasByPaciente(pacienteId)
            Log.d("AvaliacaoRepository", "Resposta: code=${response.code()}, isSuccessful=${response.isSuccessful}, body is null=${response.body() == null}")

            if (response.isSuccessful && response.body() != null) {
                val avaliacoes = response.body()!!
                Log.d("AvaliacaoRepository", "Sucesso! ${avaliacoes.size} avaliações recebidas")
                Result.success(avaliacoes)
            } else {
                val errorMsg = "Falha ao buscar histórico de avaliações: Código ${response.code()}, body is null=${response.body() == null}"
                Log.e("AvaliacaoRepository", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("AvaliacaoRepository", "Exceção ao buscar avaliações: ${e.message}", e)
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