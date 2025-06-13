package com.tecno.aging.ui.screens.cadastro.tecnicoCadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.auth.TecnicoRequest
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

open class CadastroViewModel : ViewModel() {
    val _uiState = MutableStateFlow(CadastroState())
    val uiState: StateFlow<CadastroState> = _uiState.asStateFlow()

    fun updateTecnico(tecnico: Tecnico) {
        _uiState.value = _uiState.value.copy(tecnico = tecnico)
    }

    fun updateEndereco(endereco: Endereco) {
        _uiState.value = _uiState.value.copy(
            tecnico = _uiState.value.tecnico.copy(endereco = endereco)
        )
    }

    fun updateSenha(senha: String) {
        _uiState.value = _uiState.value.copy(senha = senha)
    }

    fun updateConfirmarSenha(confirmarSenha: String) {
        _uiState.value = _uiState.value.copy(confirmarSenha = confirmarSenha)
    }
    fun onCepChanged(newCep: String) {
        val rawCep = newCep.filter { it.isDigit() }
        updateEndereco(uiState.value.tecnico.endereco.copy(cep = newCep))

        if (rawCep.length == 8) {
            buscarCep()
        }
    }

    fun buscarCep() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingCep = true, cepError = null)
            try {
                val response = RetrofitInstance.api.buscarCep(_uiState.value.tecnico.endereco.cep)
                if (response.isSuccessful) {
                    response.body()?.let { apiEndereco ->
                        _uiState.value = _uiState.value.copy(
                            tecnico = _uiState.value.tecnico.copy(
                                endereco = _uiState.value.tecnico.endereco.copy(
                                    logradouro = apiEndereco.logradouro,
                                    bairro = apiEndereco.bairro,
                                    municipio = apiEndereco.localidade,
                                    uf = apiEndereco.uf,
                                    cep = apiEndereco.cep
                                )
                            ),
                            cepError = null
                        )
                    }


                } else {
                    _uiState.value = _uiState.value.copy(cepError = "CEP não encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(cepError = "Erro ao buscar CEP")
            } finally {
                _uiState.value = _uiState.value.copy(loadingCep = false)
            }
        }
    }
    fun resetState() {
        _uiState.value = CadastroState()
    }
    fun submitForm() {
        val erros = validateForm()
        if (erros.isEmpty()) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(loading = true, error = null)
                try {
                    // Date format conversion
                    val formattedDate = try {
                        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = inputFormat.parse(uiState.value.tecnico.dataNasc)
                        if (date != null) {
                            outputFormat.format(date)
                        } else {
                            _uiState.value = _uiState.value.copy(error = "Formato de data inválido")
                            return@launch
                        }
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(error = "Formato de data inválido")
                        return@launch
                    }

                    val matriculaLong = try {
                        uiState.value.tecnico.matricula.toLong()
                    } catch (e: NumberFormatException) {
                        -1L
                    }

                    val request = TecnicoRequest(
                        matricula = matriculaLong,
                        senha = uiState.value.senha,
                        nome = uiState.value.tecnico.nome,
                        sexo = uiState.value.tecnico.sexo,
                        endereco = uiState.value.tecnico.endereco.copy(
                            municipio = uiState.value.tecnico.endereco.municipio
                        ),
                        dataNasc = formattedDate.toString(),
                        cpf = uiState.value.tecnico.cpf,
                        telefone = uiState.value.tecnico.telefone
                    )

                    val response = RetrofitInstance.api.registrarTecnico(request)

                    if (response.isSuccessful) {
                        _uiState.value = _uiState.value.copy(success = true)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _uiState.value = _uiState.value.copy(
                            error = errorBody ?: "Error: ${response.code()}"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = "Network error: ${e.message}"
                    )
                } finally {
                    _uiState.value = _uiState.value.copy(loading = false)
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(erros = erros)
        }
    }

    private fun validateForm(): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(_uiState.value) {
            val rawCpf = tecnico.cpf.filter { it.isDigit() }
            val rawTelefone = tecnico.telefone.filter { it.isDigit() }

            try {
                uiState.value.tecnico.matricula.toLong()
            } catch (e: NumberFormatException) {
                erros["matricula"] = "Matrícula inválida"
            }
            if (tecnico.matricula.isBlank()) erros["matricula"] = "Matrícula obrigatória"

            if (tecnico.nome.isBlank()) erros["nome"] = "Nome obrigatório"
            if (rawCpf.length != 11) erros["cpf"] = "CPF inválido"
            if (rawTelefone.length != 11) erros["telefone"] = "Telefone inválido"
            if (tecnico.sexo.isBlank()) erros["sexo"] = "Selecione o sexo"
            if (tecnico.dataNasc.isBlank()) erros["dataNasc"] = "Data obrigatória"
            if (senha.isBlank()) erros["senha"] = "Senha obrigatória"
            if (confirmarSenha != senha) erros["confirmarSenha"] = "Senhas não coincidem"

            val rawCep = tecnico.endereco?.cep?.filter { it.isDigit() } ?: ""
            if (rawCep.length != 8) erros["cep"] = "CEP inválido"
        }
        return erros
    }
}


data class CadastroState(
    val tecnico: Tecnico = Tecnico(),
    val senha: String = "",
    val confirmarSenha: String = "",
    val loading: Boolean = false,
    val loadingCep: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val cepError: String? = null,
    val erros: Map<String, String> = emptyMap()
)