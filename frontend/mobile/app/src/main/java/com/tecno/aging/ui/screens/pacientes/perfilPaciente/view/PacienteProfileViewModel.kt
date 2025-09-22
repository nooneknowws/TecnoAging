package com.tecno.aging.ui.screens.pacientes.perfilPaciente.view

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecno.aging.data.repository.PacienteRepository
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PacienteProfileUiState(
    val isLoading: Boolean = true,
    val paciente: Paciente? = null,
    val errorMessage: String? = null
)

class PacienteProfileViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PacienteRepository = PacienteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PacienteProfileUiState())
    val uiState: StateFlow<PacienteProfileUiState> = _uiState.asStateFlow()
    private val pacienteId: Int = checkNotNull(savedStateHandle["pacienteId"])

    init {
        loadProfile()
    }

    fun loadProfile() {
        Log.d("PROFILE_DEBUG", "1. Entrando em loadProfile() com pacienteId: $pacienteId")
        viewModelScope.launch {
            Log.d("PROFILE_DEBUG", "2. Dentro do viewModelScope.launch")
            _uiState.update { it.copy(isLoading = true) }

            Log.d("PROFILE_DEBUG", "3. Chamando repository.getPacienteById()")
            repository.getPacienteById(pacienteId)
                .onSuccess { pacienteData ->
                    Log.d("PROFILE_DEBUG", "5. Sucesso retornado pelo repositório.")
                    _uiState.update {
                        it.copy(isLoading = false, paciente = pacienteData, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    Log.d("PROFILE_DEBUG", "5. Falha retornada pelo repositório: ${error.message}")
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            Log.d("PROFILE_DEBUG", "6. Fim do bloco viewModelScope.launch")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                PacienteProfileViewModel(
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}