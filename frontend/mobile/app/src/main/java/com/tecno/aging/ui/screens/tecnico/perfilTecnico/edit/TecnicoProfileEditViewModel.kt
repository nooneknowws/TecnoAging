package com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.tecno.aging.data.repository.TecnicoRepository

data class ProfileEditUiState(
    val fotoUri: Uri? = null,
    val matricula: String = "",
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val sexo: String = "",
    val dataNasc: String = "",
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val complemento: String = "",
    val bairro: String = "",
    val municipio: String = "",
    val uf: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSearchingCep: Boolean = false,
    val cepErrorMessage: String? = null
)

class ProfileEditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository: TecnicoRepository = TecnicoRepository()
    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    private val tecnicoId: Int = checkNotNull(savedStateHandle["tecnicoId"])

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTecnicoById(tecnicoId)
                .onSuccess { tecnico ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            matricula = tecnico.matricula,
                            nome = tecnico.nome,
                            cpf = tecnico.cpf,
                            telefone = tecnico.telefone,
                            sexo = tecnico.sexo,
                            dataNasc = tecnico.dataNascimento,
                            cep = tecnico.endereco?.cep ?: "",
                            logradouro = tecnico.endereco?.logradouro ?: "",
                            numero = tecnico.endereco?.numero?.toString() ?: "",
                            complemento = tecnico.endereco?.complemento ?: "",
                            bairro = tecnico.endereco?.bairro ?: "",
                            municipio = tecnico.endereco?.municipio ?: "",
                            uf = tecnico.endereco?.uf ?: ""
                            // fotoUri precisaria ser carregado separadamente se vier de uma URL
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, cepErrorMessage = error.message) }
                }
        }
    }

    fun onFotoChange(newUri: Uri?) {
        _uiState.update { it.copy(fotoUri = newUri) }
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

    fun onNumeroChange(newValue: String) {
        _uiState.update { it.copy(numero = newValue) }
    }

    fun onComplementoChange(newValue: String) {
        _uiState.update { it.copy(complemento = newValue) }
    }

    fun onUfChange(newValue: String) {
        _uiState.update { it.copy(uf = newValue) }
    }

    fun onCepChanged(newCep: String) {
        val rawCep = newCep.filter { it.isDigit() }
        _uiState.update {
            it.copy(
                cep = newCep,
                cepErrorMessage = null,
                logradouro = "",
                bairro = "",
                municipio = "",
                uf = ""
            )
        }

        if (rawCep.length == 8) {
            buscarCep(rawCep)
        }
    }

    private fun buscarCep(cep: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingCep = true, cepErrorMessage = null) }
            try {
                val response = RetrofitInstance.api.buscarCep(cep)
                if (response.isSuccessful) {
                    response.body()?.let { apiEndereco ->
                        if (apiEndereco.erro == true) {
                            _uiState.update { it.copy(cepErrorMessage = "CEP não encontrado.") }
                        } else {
                            _uiState.update {
                                it.copy(
                                    logradouro = apiEndereco.logradouro ?: "",
                                    bairro = apiEndereco.bairro ?: "",
                                    municipio = apiEndereco.localidade ?: "",
                                    uf = apiEndereco.uf ?: ""
                                )
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(cepErrorMessage = "Erro: ${response.code()}") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(cepErrorMessage = "Falha na conexão.") }
            } finally {
                _uiState.update { it.copy(isSearchingCep = false) }
            }
        }
    }

    fun onSaveProfile() {
        // TODO: Implementar a lógica para salvar as alterações
        println("Salvando perfil: ${_uiState.value}")
    }
}