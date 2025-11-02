package com.tecno.aging.ui.screens.tecnico.listaDePacientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.repository.PacienteRepository
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PacienteListUiState(
    val isLoading: Boolean = true,
    val pacientes: List<Paciente> = emptyList(),
    val errorMessage: String? = null
)

class PacienteListViewModel(
    private val repository: PacienteRepository = PacienteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PacienteListUiState())
    val uiState: StateFlow<PacienteListUiState> = _uiState.asStateFlow()

    init {
        loadPacientes()
    }

    fun refreshProfile() {
        loadPacientes()
    }

    fun loadPacientes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getPacientes()
                .onSuccess { pacientesList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pacientes = pacientesList,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                    refreshProfile()
                }
        }
    }
}