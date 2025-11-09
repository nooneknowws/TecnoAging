package com.tecno.aging.ui.screens.cadastro.tecnicoCadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.data.repository.CadastroRepository
import com.tecno.aging.domain.models.auth.TecnicoRequest
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import com.tecno.aging.domain.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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

class CadastroTecnicoViewModel(
    private val repository: CadastroRepository = CadastroRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroState())
    val uiState: StateFlow<CadastroState> = _uiState.asStateFlow()

    fun onMatriculaChanged(matricula: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(matricula = matricula), erros = it.erros - "matricula") }
    }
    fun onCpfChanged(cpf: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(cpf = cpf), erros = it.erros - "cpf") }
    }
    fun onNomeChanged(nome: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(nome = nome), erros = it.erros - "nome") }
    }
    fun onTelefoneChanged(telefone: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(telefone = telefone), erros = it.erros - "telefone") }
    }
    fun onSexoChanged(sexo: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(sexo = sexo), erros = it.erros - "sexo") }
    }
    fun onDataNascChanged(dataNasc: String) {
        _uiState.update { it.copy(tecnico = it.tecnico.copy(dataNasc = dataNasc), erros = it.erros - "dataNasc") }
    }
    fun onEnderecoChanged(endereco: Endereco) {
        _uiState.update {
            it.copy(
                tecnico = it.tecnico.copy(endereco = endereco),
                erros = it.erros - "cep" - "logradouro" - "numero" - "bairro" - "municipio" - "uf"
            )
        }
    }
    fun onSenhaChanged(senha: String) {
        _uiState.update { it.copy(senha = senha, erros = it.erros - "senha" - "confirmarSenha") }
    }
    fun onConfirmarSenhaChanged(confirmarSenha: String) {
        _uiState.update { it.copy(confirmarSenha = confirmarSenha, erros = it.erros - "confirmarSenha") }
    }

    fun buscarCep() {
        val cep = _uiState.value.tecnico.endereco.cep
        viewModelScope.launch {
            _uiState.update { it.copy(loadingCep = true, cepError = null) }
            try {
                val response = RetrofitInstance.api.buscarCep(cep)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { apiEndereco ->
                        val updatedEndereco = _uiState.value.tecnico.endereco.copy(
                            logradouro = apiEndereco.logradouro ?: "",
                            bairro = apiEndereco.bairro ?: "",
                            municipio = apiEndereco.localidade ?: "",
                            uf = apiEndereco.uf ?: ""
                        )
                        onEnderecoChanged(updatedEndereco)
                    }
                } else {
                    _uiState.update { it.copy(cepError = "CEP não encontrado") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(cepError = "Erro ao buscar CEP") }
            } finally {
                _uiState.update { it.copy(loadingCep = false) }
            }
        }
    }

    fun resetState() {
        _uiState.value = CadastroState()
    }

    fun submitForm() {
        _uiState.update { it.copy(erros = emptyMap()) }

        val erros = validateForm()
        if (erros.isNotEmpty()) {
            _uiState.update { it.copy(erros = erros) }
            return
        }

        _uiState.update { it.copy(loading = true, error = null) }

        viewModelScope.launch {
            val request = createRequestFromState()
            if (request == null) {
                _uiState.update { it.copy(loading = false, error = "Dados inválidos, verifique o formulário.") }
                return@launch
            }

            repository.registrarTecnico(request)
                .onSuccess {
                    _uiState.update { it.copy(loading = false, success = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    private fun createRequestFromState(): TecnicoRequest? {
        val state = _uiState.value
        val formattedDate = try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            inputFormat.parse(state.tecnico.dataNasc)?.let { outputFormat.format(it) }
        } catch (e: Exception) { null }

        if (formattedDate == null) return null

        return TecnicoRequest(
            matricula = state.tecnico.matricula,
            senha = state.senha,
            nome = state.tecnico.nome,
            sexo = state.tecnico.sexo,
            endereco = state.tecnico.endereco,
            dataNasc = formattedDate,
            cpf = state.tecnico.cpf.filter { it.isDigit() },
            telefone = state.tecnico.telefone.filter { it.isDigit() }
        )
    }

    private fun validateForm(): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(_uiState.value) {
            if (tecnico.matricula.isBlank()) erros["matricula"] = "Matrícula obrigatória"
            if (tecnico.nome.isBlank()) erros["nome"] = "Nome obrigatório"

            val cpfLimpo = tecnico.cpf.filter { it.isDigit() }
            if (cpfLimpo.length != 11 || !ValidationUtils.isCpfValido(cpfLimpo)) {
                erros["cpf"] = "CPF inválido"
            }

            if (tecnico.telefone.filter { it.isDigit() }.length < 10) erros["telefone"] = "Telefone inválido"
            if (tecnico.sexo.isBlank()) erros["sexo"] = "Selecione o sexo"

            if (!ValidationUtils.isDataNascimentoValida(tecnico.dataNasc)) {
                erros["dataNasc"] = "Data inválida (idade deve ser 18-150 anos)"
            }

            if (senha.length < 6) erros["senha"] = "Senha deve ter no mínimo 6 caracteres"
            if (confirmarSenha != senha) erros["confirmarSenha"] = "As senhas não coincidem"
            if (tecnico.endereco.cep.filter { it.isDigit() }.length != 8) erros["cep"] = "CEP inválido"
            if (tecnico.endereco.logradouro.isBlank()) erros["logradouro"] = "Logradouro obrigatório"
            if (tecnico.endereco.numero.isBlank()) erros["numero"] = "Número obrigatório"
            if (tecnico.endereco.bairro.isBlank()) erros["bairro"] = "Bairro obrigatório"
            if (tecnico.endereco.municipio.isBlank()) erros["municipio"] = "Município obrigatório"
            if (tecnico.endereco.uf.isBlank()) erros["uf"] = "UF obrigatório"
        }
        return erros
    }
}