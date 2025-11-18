package com.tecno.aging.ui.screens.pacientes.perfilPaciente.edit

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.repository.PacienteRepository
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.domain.utils.convertUriToBase64
import com.tecno.aging.domain.utils.base64ToImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class PacienteEditUiState(
    val fotoUri: Uri? = null,
    val fotoBitmap: ImageBitmap? = null,
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
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val userMessage: String? = null
)

class PacienteEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: PacienteRepository = PacienteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PacienteEditUiState())
    val uiState: StateFlow<PacienteEditUiState> = _uiState.asStateFlow()

    private val pacienteId: Int = checkNotNull(savedStateHandle["pacienteId"])
    private var originalPaciente: Paciente? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getPacienteById(pacienteId)
                .onSuccess { paciente ->
                    originalPaciente = paciente

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nome = paciente.nome,
                            cpf = paciente.cpf,
                            telefone = paciente.telefone ?: "",
                            sexo = paciente.sexo ?: "",
                            dataNasc = formatarDataParaUi(paciente.dataNasc),
                            rg = paciente.rg ?: "",
                            corRaca = paciente.corRaca ?: "",
                            estadoCivil = paciente.estadoCivil ?: "",
                            escolaridade = paciente.escolaridade ?: "",
                            nacionalidade = paciente.nacionalidade ?: "",
                            municipioNasc = paciente.municipioNasc ?: "",
                            fotoBitmap = base64ToImageBitmap(paciente.fotoPerfil)
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun onFotoChange(newUri: Uri?) { _uiState.update { it.copy(fotoUri = newUri, fotoBitmap = null) } }
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

    fun onSaveProfile(contentResolver: ContentResolver) {
        if (originalPaciente == null) {
            _uiState.update { it.copy(errorMessage = "Erro: Paciente original não carregado.") }
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        val formattedDate = formatarDataParaApi(uiState.value.dataNasc)

        val pacienteAtualizado = originalPaciente!!.copy(
            nome = uiState.value.nome,
            telefone = uiState.value.telefone,
            sexo = uiState.value.sexo,
            dataNasc = formattedDate,
            rg = uiState.value.rg,
            corRaca = uiState.value.corRaca,
            estadoCivil = uiState.value.estadoCivil,
            escolaridade = uiState.value.escolaridade,
            nacionalidade = uiState.value.nacionalidade,
            municipioNasc = uiState.value.municipioNasc
        )

        viewModelScope.launch {
            repository.updatePaciente(pacienteId, pacienteAtualizado)
                .onSuccess {
                    SessionManager.saveUserName(uiState.value.nome)
                    val fotoUri = uiState.value.fotoUri
                    if (fotoUri == null) {
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                userMessage = "Perfil atualizado com sucesso!",
                                saveSuccess = true
                            )
                        }
                    } else {
                        val base64Image = convertUriToBase64(contentResolver, fotoUri)
                        if (base64Image != null) {
                            repository.uploadFotoPaciente(pacienteId, base64Image)
                                .onSuccess {
                                    _uiState.update {
                                        it.copy(
                                            isSaving = false,
                                            userMessage = "Perfil e foto atualizados!",
                                            saveSuccess = true
                                        )
                                    }
                                }
                                .onFailure {
                                    _uiState.update { it.copy(isSaving = false, userMessage = "Perfil salvo, mas falha ao enviar foto.") }
                                }
                        } else {
                            _uiState.update { it.copy(isSaving = false, userMessage = "Perfil salvo, mas falha ao processar foto.") }
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null, errorMessage = null) }
    }

    private fun formatarDataParaUi(dataApi: String?): String {
        if (dataApi.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dataApi)
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            dataApi
        }
    }

    private fun formatarDataParaApi(dataUi: String?): String? {
        if (dataUi.isNullOrEmpty()) return null
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dataUi)
            date?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            originalPaciente?.dataNasc
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                PacienteEditViewModel(
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}