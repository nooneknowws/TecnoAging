package com.tecno.aging.ui.screens.cadastro.pacienteCadastro

import kotlin.collections.filter
import kotlin.collections.toMutableList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.data.repository.PacienteRepository
import com.tecno.aging.domain.models.pessoa.paciente.Contato
import com.tecno.aging.domain.models.pessoa.paciente.PacienteRequest
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.validation.CadastroPacienteValidator
import com.tecno.aging.domain.validation.CadastroPacienteValidator.validateStep1
import com.tecno.aging.domain.validation.CadastroPacienteValidator.validateStep2
import com.tecno.aging.domain.validation.CadastroPacienteValidator.validateStep3
import com.tecno.aging.domain.validation.CadastroPacienteValidator.validateStep4
import com.tecno.aging.domain.validation.CadastroPacienteValidator.validateStep5
import com.tecno.aging.domain.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class CadastroPacienteState(
    val nome: String = "",
    val cpf: String = "",
    val sexo: String = "",
    val dataNascimento: String = "",
    val telefone: String = "",
    val endereco: Endereco = Endereco(),
    val peso: String = "",
    val altura: String = "",
    val socioeconomico: String = "",
    val escolaridade: String = "",
    val estadoCivil: String = "",
    val nacionalidade: String = "",
    val municipioNasc: String = "",
    val ufNasc: String = "",
    val corRaca: String = "",
    val rg: String = "",
    val dataExpedicao: String = "",
    val orgaoEmissor: String = "",
    val ufEmissor: String = "",
    val contatos: List<Contato> = listOf(Contato("", "", "")),
    val senha: String = "",
    val confirmarSenha: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val erros: Map<String, String> = emptyMap(),
    val loadingCep: Boolean = false,
    val cepError: String? = null,
    val currentStep: Int = 0,
)

class CadastroPacienteViewModel(
    private val repository: PacienteRepository = PacienteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroPacienteState())
    val uiState: StateFlow<CadastroPacienteState> = _uiState.asStateFlow()

    fun onNomeChange(value: String) = _uiState.update { it.copy(nome = value, erros = it.erros - "nome") }
    fun onCpfChange(value: String) = _uiState.update { it.copy(cpf = value, erros = it.erros - "cpf") }
    fun onSexoChange(value: String) = _uiState.update { it.copy(sexo = value, erros = it.erros - "sexo") }
    fun onDataNascimentoChange(value: String) = _uiState.update { it.copy(dataNascimento = value, erros = it.erros - "dataNascimento") }
    fun onTelefoneChange(value: String) = _uiState.update { it.copy(telefone = value, erros = it.erros - "telefone") }

    fun onEnderecoChange(value: Endereco) = _uiState.update {
        it.copy(
            endereco = value,
            erros = it.erros - "cep" - "logradouro" - "numero" - "bairro" - "municipio" - "uf"
        )
    }

    fun onPesoChange(value: String) = _uiState.update { it.copy(peso = value, erros = it.erros - "peso") }
    fun onAlturaChange(value: String) = _uiState.update { it.copy(altura = value, erros = it.erros - "altura") }
    fun onSocioeconomicoChange(value: String) = _uiState.update { it.copy(socioeconomico = value, erros = it.erros - "socioeconomico") }
    fun onEscolaridadeChange(value: String) = _uiState.update { it.copy(escolaridade = value, erros = it.erros - "escolaridade") }
    fun onEstadoCivilChange(value: String) = _uiState.update { it.copy(estadoCivil = value, erros = it.erros - "estadoCivil") }
    fun onNacionalidadeChange(value: String) = _uiState.update { it.copy(nacionalidade = value, erros = it.erros - "nacionalidade") }
    fun onMunicipioNascChange(value: String) = _uiState.update { it.copy(municipioNasc = value, erros = it.erros - "municipioNasc") }
    fun onUfNascChange(value: String) = _uiState.update { it.copy(ufNasc = value, erros = it.erros - "ufNasc") }
    fun onCorRacaChange(value: String) = _uiState.update { it.copy(corRaca = value, erros = it.erros - "corRaca") }
    fun onRgChange(value: String) = _uiState.update { it.copy(rg = value, erros = it.erros - "rg") }
    fun onDataExpedicaoChange(value: String) = _uiState.update { it.copy(dataExpedicao = value, erros = it.erros - "dataExpedicao") }
    fun onOrgaoEmissorChange(value: String) = _uiState.update { it.copy(orgaoEmissor = value, erros = it.erros - "orgaoEmissor") }
    fun onUfEmissorChange(value: String) = _uiState.update { it.copy(ufEmissor = value, erros = it.erros - "ufEmissor") }
    fun onSenhaChange(value: String) = _uiState.update { it.copy(senha = value, erros = it.erros - "senha" - "confirmarSenha") }
    fun onConfirmarSenhaChange(value: String) = _uiState.update { it.copy(confirmarSenha = value, erros = it.erros - "confirmarSenha") }

    fun onContatoChange(index: Int, updatedContato: Contato) {
        _uiState.update {
            val currentContatos = it.contatos.toMutableList()
            currentContatos[index] = updatedContato
            it.copy(contatos = currentContatos)
        }
    }
    fun addContato() {
        _uiState.update {
            it.copy(contatos = it.contatos + Contato("", "", ""))
        }
    }
    fun removeContato(index: Int) {
        _uiState.update {
            val currentContatos = it.contatos.toMutableList()
            currentContatos.removeAt(index)
            it.copy(contatos = currentContatos)
        }
    }

    fun buscarCep() {
        val cep = _uiState.value.endereco.cep.filter { it.isDigit() }
        if (cep.length != 8) return

        viewModelScope.launch {
            _uiState.update { it.copy(loadingCep = true, cepError = null) }
            try {
                val response = RetrofitInstance.api.buscarCep(cep)
                if (response.isSuccessful && response.body()?.erro != true) {
                    response.body()?.let { apiEndereco ->
                        val updatedEndereco = _uiState.value.endereco.copy(
                            logradouro = apiEndereco.logradouro ?: "",
                            bairro = apiEndereco.bairro ?: "",
                            municipio = apiEndereco.localidade ?: "",
                            uf = apiEndereco.uf ?: ""
                        )
                        onEnderecoChange(updatedEndereco)
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

    fun onNextStepClick() {
        val currentStep = _uiState.value.currentStep
        val erros = when (currentStep) {
            0 -> validateStep1(_uiState.value)
            1 -> validateStep2(_uiState.value)
            2 -> validateStep3(_uiState.value)
            3 -> validateStep4(_uiState.value)
            4 -> validateStep5(_uiState.value)
            else -> emptyMap()
        }

        if (erros.isEmpty()) {
            _uiState.update { it.copy(currentStep = currentStep + 1, erros = emptyMap()) }
        } else {
            _uiState.update { it.copy(erros = erros) }
        }
    }

    fun onPreviousStepClick() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 0) {
            _uiState.update { it.copy(currentStep = currentStep - 1, erros = emptyMap()) }
        }
    }

    fun submitForm() {
        _uiState.update { it.copy(erros = emptyMap()) }

        val allErrors = mutableMapOf<String, String>()
        allErrors.putAll(validateStep1(_uiState.value))
        allErrors.putAll(validateStep2(_uiState.value))
        allErrors.putAll(validateStep3(_uiState.value))
        allErrors.putAll(validateStep4(_uiState.value))
        allErrors.putAll(validateStep5(_uiState.value))

        if (allErrors.isNotEmpty()) {
            _uiState.update { it.copy(erros = allErrors) }
            return
        }

        _uiState.update { it.copy(loading = true, error = null) }

        viewModelScope.launch {
            val request = createRequestFromState()
            if (request == null) {
                _uiState.update { it.copy(loading = false, error = "Erro ao formatar os dados. Verifique as datas e números.") }
                return@launch
            }

            repository.registrarPaciente(request)
                .onSuccess {
                    _uiState.update { it.copy(loading = false, success = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(loading = false, error = error.message) }
                }
        }
    }

    private fun createRequestFromState(): PacienteRequest? {
        val state = _uiState.value
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return try {
            PacienteRequest(
                nome = state.nome,
                cpf = state.cpf.filter { it.isDigit() },
                sexo = state.sexo,
                dataNascimento = dateFormat.format(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(state.dataNascimento)!!),
                telefone = state.telefone.filter { it.isDigit() },
                senha = state.senha,
                endereco = state.endereco,
                peso = state.peso.replace(",", ".").toDouble(),
                altura = state.altura.replace(",", ".").toDouble(),
                socioeconomico = state.socioeconomico,
                escolaridade = state.escolaridade,
                estadoCivil = state.estadoCivil,
                nacionalidade = state.nacionalidade,
                municipioNasc = state.municipioNasc,
                ufNasc = state.ufNasc,
                corRaca = state.corRaca,
                rg = state.rg,
                dataExpedicao = dateFormat.format(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(state.dataExpedicao)!!),
                orgaoEmissor = state.orgaoEmissor,
                ufEmissor = state.ufEmissor,
                contatos = state.contatos.filter {
                    it.nome.isNotBlank() && it.telefone.isNotBlank() && it.parentesco.isNotBlank()
                }
            )
        } catch (e: Exception) {
            null
        }
    }

    fun resetState() { _uiState.value = CadastroPacienteState() }
}