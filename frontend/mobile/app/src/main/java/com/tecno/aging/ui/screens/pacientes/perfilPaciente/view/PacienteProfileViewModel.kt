package com.tecno.aging.ui.screens.pacientes.perfilPaciente.view

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.ui.graphics.ImageBitmap
import com.tecno.aging.data.repository.PacienteRepository
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.domain.utils.base64ToImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PacienteProfileUiState(
    val isLoading: Boolean = true,
    val paciente: Paciente? = null,
    val fotoBitmap: ImageBitmap? = null,
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

    fun refreshProfile() {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            Log.d("PacienteProfile", "Iniciando carregamento do perfil para pacienteId=$pacienteId")
            _uiState.update { it.copy(isLoading = true) }

            val result = repository.getPacienteById(pacienteId)
            Log.d("PacienteProfile", "Resultado do repositório: success=${result.isSuccess}, failure=${result.isFailure}")

            result
                .onSuccess { pacienteData ->
                    Log.d("PacienteProfile", "Sucesso! Paciente: nome=${pacienteData.nome}, cpf=${pacienteData.cpf}, idade=${pacienteData.idade}")
                    Log.d("PacienteProfile", "Foto base64 presente: ${pacienteData.fotoPerfil != null && pacienteData.fotoPerfil.isNotBlank()}")

                    // Converte base64 para ImageBitmap
                    val fotoBitmap = base64ToImageBitmap(pacienteData.fotoPerfil)
                    Log.d("PacienteProfile", "Foto bitmap convertida: ${fotoBitmap != null}")

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            paciente = pacienteData,
                            errorMessage = null,
                            fotoBitmap = fotoBitmap
                        )
                    }
                    Log.d("PacienteProfile", "Estado atualizado. paciente não é null: ${_uiState.value.paciente != null}")
                }
                .onFailure { error ->
                    Log.e("PacienteProfile", "Erro ao carregar perfil: ${error.message}", error)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
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