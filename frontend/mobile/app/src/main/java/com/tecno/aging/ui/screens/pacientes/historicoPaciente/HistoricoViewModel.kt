package com.tecno.aging.ui.screens.pacientes.historicoPaciente

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

data class HistoricoUiState(
    val isLoading: Boolean = true,
    val avaliacoes: List<HistoricoAvaliacao> = emptyList(),
    val error: String? = null
)

class HistoricoViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AvaliacaoRepository = AvaliacaoRepository()
) : ViewModel() {

    private val pacienteId: Int = checkNotNull(savedStateHandle["pacienteId"])

    private val _uiState = MutableStateFlow(HistoricoUiState())
    val uiState: StateFlow<HistoricoUiState> = _uiState.asStateFlow()

    init {
        loadHistorico()
    }

    private fun loadHistorico() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            repository.getAvaliacoesByPaciente(pacienteId)
                .onSuccess { listaDeAvaliacoes ->
                    _uiState.update {
                        it.copy(isLoading = false, avaliacoes = listaDeAvaliacoes)
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(isLoading = false, error = erro.message)
                    }
                }
        }
    }

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