package com.tecno.aging.ui.screens.tecnico.perfilTecnico.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileEditUiState(
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

class ProfileEditViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

    init {
        loadInitialProfile()
    }

    private fun loadInitialProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    matricula = "12345",
                    nome = "aaaaaaaaa",
                    cpf = "111.222.333-42",
                    telefone = "(41) 98888-8888",
                    sexo = "Masculino",
                    dataNasc = "10/01/1995",
                    cep = "83408-280",
                    logradouro = "Rua das Palmeiras",
                    numero = "0",
                    complemento = "Casa",
                    bairro = "Centro",
                    municipio = "Curitiba",
                    uf = "PR"
                )
            }
        }
    }

    fun onNomeChange(newValue: String) { _uiState.update { it.copy(nome = newValue) } }
    fun onTelefoneChange(newValue: String) { _uiState.update { it.copy(telefone = newValue) } }
    fun onSexoChange(newValue: String) { _uiState.update { it.copy(sexo = newValue) } }
    fun onDataNascChange(newValue: String) { _uiState.update { it.copy(dataNasc = newValue) } }
    fun onNumeroChange(newValue: String) { _uiState.update { it.copy(numero = newValue) } }
    fun onComplementoChange(newValue: String) { _uiState.update { it.copy(complemento = newValue) } }
    fun onUfChange(newValue: String) { _uiState.update { it.copy(uf = newValue) } }

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