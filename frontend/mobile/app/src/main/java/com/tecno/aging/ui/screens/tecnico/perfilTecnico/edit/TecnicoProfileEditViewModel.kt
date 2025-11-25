package com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.data.repository.TecnicoRepository
import com.tecno.aging.domain.models.DTO.TecnicoUpdateRequest
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import com.tecno.aging.domain.utils.convertUriToBase64
import com.tecno.aging.domain.utils.base64ToImageBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class ProfileEditUiState(
    val fotoUri: Uri? = null,
    val fotoBitmap: ImageBitmap? = null,
    val matricula: String = "",
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val sexo: String = "",
    val dataNasc: String = "",
    val endereco: Endereco = Endereco(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val userMessage: String? = null,
    val isSearchingCep: Boolean = false,
    val cepErrorMessage: String? = null
)

class ProfileEditViewModel() : ViewModel() {

    private val repository: TecnicoRepository = TecnicoRepository()
    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()
    private val tecnicoId: Int? = SessionManager.getUserId()?.toIntOrNull()
    private var originalTecnico: Tecnico? = null

    init {
        loadProfile()
    }

    private fun loadProfile() {
        if (tecnicoId == null) {
            _uiState.update { it.copy(isLoading = false, userMessage = "ID de usuário inválido.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTecnicoById(tecnicoId)
                .onSuccess { tecnico ->
                    SessionManager.saveUserName(uiState.value.nome)
                    originalTecnico = tecnico

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            matricula = tecnico.matricula,
                            nome = tecnico.nome,
                            cpf = tecnico.cpf,
                            telefone = tecnico.telefone,
                            sexo = tecnico.sexo,
                            dataNasc = tecnico.dataNasc,
                            endereco = Endereco(
                                cep = tecnico.endereco.cep,
                                logradouro = tecnico.endereco.logradouro,
                                numero = tecnico.endereco.numero.toString(),
                                complemento = tecnico.endereco.complemento ?: "",
                                bairro = tecnico.endereco.bairro,
                                municipio = tecnico.endereco.municipio,
                                uf = tecnico.endereco.uf
                            ),
                            fotoBitmap = base64ToImageBitmap(tecnico.fotoPerfil)
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = error.message
                        )
                    }
                }
        }
    }

    fun onFotoChange(newUri: Uri?) {
        _uiState.update { it.copy(fotoUri = newUri, fotoBitmap = null) }
    }

    fun onNomeChange(newValue: String) {
        _uiState.update { it.copy(nome = newValue) }
    }

    fun onTelefoneChange(newValue: String) {
        _uiState.update { it.copy(telefone = newValue) }
    }

    fun onSexoChange(newValue: String) {
        _uiState.update { it.copy(sexo = newValue) }
    }

    fun onDataNascChange(newValue: String) {
        _uiState.update { it.copy(dataNasc = newValue) }
    }

    fun onCepChanged(newCep: String) {
        _uiState.update {
            it.copy(endereco = it.endereco.copy(cep = newCep), cepErrorMessage = null)
        }
        if (newCep.filter { it.isDigit() }.length == 8) {
            buscarCep(newCep)
        }
    }

    fun onNumeroChange(newValue: String) {
        _uiState.update { it.copy(endereco = it.endereco.copy(numero = newValue)) }
    }

    fun onComplementoChange(newValue: String) {
        _uiState.update { it.copy(endereco = it.endereco.copy(complemento = newValue)) }
    }

    fun buscarCep(cep: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingCep = true) }
            try {
                val response = RetrofitInstance.api.buscarCep(cep)
                if (response.isSuccessful && response.body()?.erro != true) {
                    response.body()?.let { apiEndereco ->
                        _uiState.update {
                            it.copy(
                                endereco = it.endereco.copy(
                                    logradouro = apiEndereco.logradouro,
                                    bairro = apiEndereco.bairro,
                                    municipio = apiEndereco.localidade,
                                    uf = apiEndereco.uf
                                )
                            )
                        }
                    }
                } else {
                    _uiState.update { it.copy(cepErrorMessage = "CEP não encontrado.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(cepErrorMessage = "Falha na conexão.") }
            } finally {
                _uiState.update { it.copy(isSearchingCep = false) }
            }
        }
    }

    fun onSaveProfile(contentResolver: ContentResolver) {
        if (tecnicoId == null || originalTecnico == null) {
            _uiState.update { it.copy(userMessage = "Não foi possível salvar. Dados originais não carregados.") }
            return
        }
        _uiState.update { it.copy(isSaving = true) }

        val formattedDate = try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            inputFormat.parse(uiState.value.dataNasc)?.let { outputFormat.format(it) } ?: originalTecnico!!.dataNasc
        } catch (e: Exception) {
            originalTecnico!!.dataNasc
        }

        val request = TecnicoUpdateRequest(
            nome = uiState.value.nome,
            telefone = uiState.value.telefone,
            sexo = uiState.value.sexo,
            dataNasc = formattedDate,
            endereco = uiState.value.endereco,
            matricula = originalTecnico!!.matricula,
            cpf = originalTecnico!!.cpf,
            idade = originalTecnico!!.idade,
            ativo = originalTecnico!!.ativo
        )

        viewModelScope.launch {
            repository.updateTecnico(tecnicoId, request)
                .onSuccess {
                    val fotoUri = uiState.value.fotoUri
                    if (fotoUri == null) {
                        _uiState.update { it.copy(isSaving = false, userMessage = "Perfil atualizado com sucesso!") }
                        delay(1500)
                        _uiState.update { it.copy(saveSuccess = true) }
                    } else {
                        val base64Image = convertUriToBase64(contentResolver, fotoUri)
                        if (base64Image != null) {
                            repository.uploadFotoTecnico(tecnicoId, base64Image)
                                .onSuccess {
                                    _uiState.update { it.copy(isSaving = false, userMessage = "Perfil e foto atualizados!") }
                                    delay(1500)
                                    _uiState.update { it.copy(saveSuccess = true) }
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
                    _uiState.update { it.copy(isSaving = false, userMessage = error.message) }
                }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
}