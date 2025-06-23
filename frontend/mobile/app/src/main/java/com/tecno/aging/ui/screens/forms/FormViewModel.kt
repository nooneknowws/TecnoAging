package com.tecno.aging.ui.screens.forms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.DTO.AvaliacaoPostDTO
import com.tecno.aging.domain.models.DTO.RespostaPostDTO
import com.tecno.aging.domain.models.forms.GenericForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormUiState(
    val form: GenericForm? = null,
    val etapaAtual: Int = 0,
    val respostas: Map<Long, String> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val submissionSuccess: Boolean = false
)

class FormViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    private val formId: Long = checkNotNull(savedStateHandle["formId"])
    private val pacienteId: Long = checkNotNull(savedStateHandle["pacienteId"])
    private val tecnicoId: Long? = SessionManager.getUserId()?.toLongOrNull()

    init {
        carregarFormulario()
    }

    private fun carregarFormulario() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, submissionSuccess = false) }
            try {
                val response = RetrofitInstance.api.getFormularioById(formId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(form = response.body(), isLoading = false, etapaAtual = 0, respostas = emptyMap())
                    }
                } else {
                    _uiState.update { it.copy(error = "Falha ao carregar o formulário.", isLoading = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "Erro de conexão.", isLoading = false) }
            }
        }
    }

    fun onRespostaChanged(perguntaId: Long, resposta: String) {
        _uiState.update { currentState ->
            val novasRespostas = currentState.respostas.toMutableMap()
            novasRespostas[perguntaId] = resposta
            currentState.copy(respostas = novasRespostas)
        }
    }

    fun submeterAvaliacao() {
        if (tecnicoId == null) {
            _uiState.update { it.copy(error = "ID do técnico não encontrado na sessão.") }
            return
        }

        val respostasDTO = _uiState.value.respostas.map { (perguntaId, valor) ->
            RespostaPostDTO(perguntaId = perguntaId, valor = valor)
        }

        val avaliacaoDTO = AvaliacaoPostDTO(
            pacienteId = pacienteId,
            tecnicoId = tecnicoId,
            formularioId = formId,
            respostas = respostasDTO
        )

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.salvarAvaliacao(avaliacaoDTO)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(submissionSuccess = true) }
                } else {
                    _uiState.update { it.copy(error = "Falha ao enviar avaliação: ${response.code()} ${response.message()}") }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "Erro de conexão ao enviar.") }
            }
        }
    }

    fun proximaEtapa() {
        _uiState.value.form?.let { form ->
            if (_uiState.value.etapaAtual < form.etapas.size - 1) {
                _uiState.update { it.copy(etapaAtual = it.etapaAtual + 1) }
            }
        }
    }

    fun etapaAnterior() {
        if (_uiState.value.etapaAtual > 0) {
            _uiState.update { it.copy(etapaAtual = it.etapaAtual - 1) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                FormViewModel(
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}