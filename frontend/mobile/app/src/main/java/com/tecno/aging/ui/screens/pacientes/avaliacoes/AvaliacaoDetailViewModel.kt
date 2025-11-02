package com.tecno.aging.ui.screens.pacientes.avaliacoes

import android.util.Log
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

data class AvaliacaoDetailUiState(
    val isLoading: Boolean = true,
    val avaliacao: HistoricoAvaliacao? = null,
    val error: String? = null
)

class AvaliacaoDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: AvaliacaoRepository = AvaliacaoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AvaliacaoDetailUiState())
    val uiState: StateFlow<AvaliacaoDetailUiState> = _uiState.asStateFlow()
    private val avaliacaoId: Long = checkNotNull(savedStateHandle["avaliacaoId"])

    init {
        loadAvaliacaoDetails()
    }

    private fun loadAvaliacaoDetails() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getAvaliacaoById(avaliacaoId)
                .onSuccess { data ->
                    _uiState.update { it.copy(isLoading = false, avaliacao = data) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                AvaliacaoDetailViewModel(savedStateHandle = savedStateHandle)
            }
        }
    }
}