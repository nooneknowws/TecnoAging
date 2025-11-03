package com.tecno.aging.ui.screens.tecnico.editAvaliacao

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.repository.AvaliacaoRepository
import com.tecno.aging.domain.models.DTO.AvaliacaoPostDTO
import com.tecno.aging.domain.models.DTO.RespostaPostDTO
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class AvaliacaoEditUiState(
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val error: String? = null,
    val avaliacao: HistoricoAvaliacao? = null,
    val respostas: Map<Long, String> = emptyMap()
)

class AvaliacaoEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AvaliacaoRepository = AvaliacaoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AvaliacaoEditUiState())
    val uiState: StateFlow<AvaliacaoEditUiState> = _uiState.asStateFlow()
    private val avaliacaoId: Long = checkNotNull(savedStateHandle["avaliacaoId"])

    init {
        loadAvaliacao()
    }

    fun loadAvaliacao() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getAvaliacaoById(avaliacaoId)
                .onSuccess { data ->
                    val respostasMap = data.respostas.associate {
                        it.pergunta.id to it.valor
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            avaliacao = data,
                            respostas = respostasMap
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun onRespostaChanged(perguntaId: Long, resposta: String) {
        _uiState.update { currentState ->
            val novasRespostas = currentState.respostas.toMutableMap()
            novasRespostas[perguntaId] = resposta
            currentState.copy(respostas = novasRespostas)
        }
    }

    fun submitUpdate() {
        if (_uiState.value.isSubmitting || _uiState.value.avaliacao == null) return

        val tecnicoId = SessionManager.getUserId()?.toLongOrNull()
        if (tecnicoId == null) {
            _uiState.update { it.copy(error = "ID do técnico não encontrado.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }
        val avaliacaoOriginal = _uiState.value.avaliacao!!

        val agora = OffsetDateTime.now()
        val formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val dataAtualizacao = agora.format(formatador)

        val respostasDTO = _uiState.value.respostas.map { (perguntaId, valor) ->
            RespostaPostDTO(perguntaId = perguntaId, valor = valor)
        }

        val avaliacaoDTO = AvaliacaoPostDTO(
            pacienteId = avaliacaoOriginal.pacienteId,
            tecnicoId = tecnicoId,
            formularioId = avaliacaoOriginal.formularioId,
            respostas = respostasDTO,
            dataCriacao = avaliacaoOriginal.dataCriacao ?: dataAtualizacao,
            dataAtualizacao = dataAtualizacao
        )

        viewModelScope.launch {
            repository.updateAvaliacao(avaliacaoOriginal.id, avaliacaoDTO)
                .onSuccess {
                    _uiState.update { it.copy(isSubmitting = false, submissionSuccess = true) }
                    loadAvaliacao()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSubmitting = false, error = error.message) }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                AvaliacaoEditViewModel(savedStateHandle = savedStateHandle)
            }
        }
    }
}