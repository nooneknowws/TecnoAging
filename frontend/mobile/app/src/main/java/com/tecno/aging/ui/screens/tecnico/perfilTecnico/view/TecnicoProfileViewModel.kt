package com.tecno.aging.ui.screens.tecnico.perfilTecnico.view

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.repository.TecnicoRepository
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TecnicoProfileUiState(
    val isLoading: Boolean = true,
    val tecnico: Tecnico? = null,
    val fotoBase64: String? = null,
    val error: String? = null
)

class TecnicoProfileViewModel(
    private val repository: TecnicoRepository = TecnicoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TecnicoProfileUiState())
    val uiState: StateFlow<TecnicoProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun refreshProfile() {
        loadProfile()
    }

    private fun loadProfile() {
        val tecnicoId = SessionManager.getUserId()?.toIntOrNull()

        if (tecnicoId == null) {
            _uiState.update { it.copy(isLoading = false, error = "ID do usuário não encontrado na sessão.") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getTecnicoById(tecnicoId)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tecnico = data,
                            fotoBase64 = data.fotoPerfil
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = "") }
                    refreshProfile()
                }
        }
    }
}