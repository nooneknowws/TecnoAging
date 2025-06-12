package com.tecno.aging.ui.screens.pacientes.historico

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecno.aging.data.repository.AvaliacaoRepository
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Representa o estado da tela: carregando, com dados, ou com erro.
data class HistoricoUiState(
    val isLoading: Boolean = true,
    val avaliacoes: List<HistoricoAvaliacao> = emptyList(),
    val error: String? = null
)

class HistoricoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AvaliacaoRepository = AvaliacaoRepository()
) : ViewModel() {

    // Pega o ID do paciente que foi passado durante a navegação.
    private val pacienteId: Int = checkNotNull(savedStateHandle["pacienteId"])

    private val _uiState = MutableStateFlow(HistoricoUiState())
    val uiState: StateFlow<HistoricoUiState> = _uiState.asStateFlow()

    init {
        // Assim que o ViewModel é criado, ele já inicia o carregamento do histórico.
        loadHistorico()
    }

    private fun loadHistorico() {
        // Define o estado inicial como "carregando".
        _uiState.update { it.copy(isLoading = true) }

        // Inicia uma coroutine para fazer a chamada de rede em background.
        viewModelScope.launch {
            repository.getAvaliacoesByPaciente(pacienteId)
                .onSuccess { listaDeAvaliacoes ->
                    // Em caso de sucesso, atualiza o estado com a lista recebida.
                    _uiState.update {
                        it.copy(isLoading = false, avaliacoes = listaDeAvaliacoes)
                    }
                }
                .onFailure { erro ->
                    // Em caso de falha, atualiza o estado com a mensagem de erro.
                    _uiState.update {
                        it.copy(isLoading = false, error = erro.message)
                    }
                }
        }
    }

    // Factory para permitir que o ViewModel seja criado com o SavedStateHandle.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                HistoricoViewModel(
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}