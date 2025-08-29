package com.tecno.aging.data.repository

import android.util.Log
import com.tecno.aging.data.remote.ApiService
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import kotlin.Result

class AvaliacaoRepository(
    private val apiService: ApiService = RetrofitInstance.api
) {

    suspend fun getAvaliacoesByPaciente(pacienteId: Int): Result<List<HistoricoAvaliacao>> {
        Log.d("HISTORICO_REPO", "Buscando avaliações para o paciente ID: $pacienteId")
        return try {
            val response = apiService.getRespostasByPaciente(pacienteId)

            if (response.isSuccessful && response.body() != null) {
                Log.d(
                    "HISTORICO_REPO",
                    "Sucesso! ${response.body()?.size ?: 0} avaliações encontradas."
                )
                Result.success(response.body()!!)
            } else {
                Log.e("HISTORICO_REPO", "Falha na resposta da API: Código ${response.code()}")
                Result.failure(Exception("Falha ao buscar histórico de avaliações: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("HISTORICO_REPO", "Exceção na chamada de rede: ${e.message}")
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
}