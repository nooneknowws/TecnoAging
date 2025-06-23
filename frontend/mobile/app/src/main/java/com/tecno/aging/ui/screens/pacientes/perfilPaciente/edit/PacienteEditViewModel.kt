package com.tecno.aging.ui.screens.pacientes.perfilPaciente.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.repository.PacienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PacienteEditUiState(
    val fotoUri: Uri? = null,
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val sexo: String = "",
    val dataNasc: String = "",
    val rg: String = "",
    val corRaca: String = "",
    val estadoCivil: String = "",
    val escolaridade: String = "",
    val nacionalidade: String = "",
    val municipioNasc: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

class PacienteEditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository: PacienteRepository = PacienteRepository()
    private val _uiState = MutableStateFlow(PacienteEditUiState())
    val uiState: StateFlow<PacienteEditUiState> = _uiState.asStateFlow()

    private val pacienteId: Int = checkNotNull(savedStateHandle["pacienteId"])

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getPacienteById(pacienteId)
                .onSuccess { paciente ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nome = paciente.nome,
                            cpf = paciente.cpf,
                            telefone = paciente.telefone ?: "",
                            sexo = paciente.sexo ?: "",
                            dataNasc = paciente.dataNasc ?: "",
                            rg = paciente.rg ?: "",
                            corRaca = paciente.corRaca ?: "",
                            estadoCivil = paciente.estadoCivil ?: "",
                            escolaridade = paciente.escolaridade ?: "",
                            nacionalidade = paciente.nacionalidade ?: "",
                            municipioNasc = paciente.municipioNasc ?: ""
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun onFotoChange(newUri: Uri?) { _uiState.update { it.copy(fotoUri = newUri) } }
    fun onNomeChange(newValue: String) { _uiState.update { it.copy(nome = newValue) } }
    fun onTelefoneChange(newValue: String) { _uiState.update { it.copy(telefone = newValue) } }
    fun onSexoChange(newValue: String) { _uiState.update { it.copy(sexo = newValue) } }
    fun onDataNascChange(newValue: String) { _uiState.update { it.copy(dataNasc = newValue) } }
    fun onRgChange(newValue: String) { _uiState.update { it.copy(rg = newValue) } }
    fun onCorRacaChange(newValue: String) { _uiState.update { it.copy(corRaca = newValue) } }
    fun onEstadoCivilChange(newValue: String) { _uiState.update { it.copy(estadoCivil = newValue) } }
    fun onEscolaridadeChange(newValue: String) { _uiState.update { it.copy(escolaridade = newValue) } }
    fun onNacionalidadeChange(newValue: String) { _uiState.update { it.copy(nacionalidade = newValue) } }
    fun onMunicipioNascChange(newValue: String) { _uiState.update { it.copy(municipioNasc = newValue) } }

    fun onSaveProfile() {
        // TODO: Implementar a lógica para salvar as alterações do paciente
        println("Salvando perfil do paciente: ${_uiState.value}")
    }
}