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
import com.tecno.aging.data.repository.AvaliacaoRepository
import com.tecno.aging.domain.models.DTO.AvaliacaoPostDTO
import com.tecno.aging.domain.models.DTO.RespostaPostDTO
import com.tecno.aging.domain.models.forms.GenericForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class FormUiState(
    val form: GenericForm? = null,
    val etapaAtual: Int = 0,
    val respostas: Map<Long, String> = emptyMap(),
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val submissionSuccess: Boolean = false,
    val dataCriacao: String? = null,
    val pageTitle: String = "Carregando..."
)

class FormViewModel(
    savedStateHandle: SavedStateHandle,
    private val avaliacaoRepository: AvaliacaoRepository = AvaliacaoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    private val avaliacaoId: Long? = savedStateHandle["avaliacaoId"]
    private val formId: Long? = savedStateHandle["formId"]
    private var pacienteId: Long? = savedStateHandle["pacienteId"]
    private val tecnicoId: Long? = if (SessionManager.getUserProfile().equals("TECNICO", ignoreCase = true)) {
        SessionManager.getUserId()?.toLongOrNull()
    } else {
        null
    }

    private val isEditMode = avaliacaoId != null

    init {
        if (isEditMode) {
            loadFormForEdit()
        } else {
            loadFormForCreate()
        }
    }

    private fun loadFormForCreate() {
        val formIdToLoad = checkNotNull(formId) { "formId não pode ser nulo no modo de criação" }
        pacienteId = checkNotNull(pacienteId) { "pacienteId não pode ser nulo no modo de criação" }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, submissionSuccess = false) }
            try {
                val response = RetrofitInstance.api.getFormularioById(formIdToLoad)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            form = response.body(),
                            isLoading = false,
                            etapaAtual = 0,
                            respostas = emptyMap(),
                            pageTitle = response.body()!!.titulo
                        )
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

    private fun loadFormForEdit() {
        val avaliacaoIdToLoad = checkNotNull(avaliacaoId)
        _uiState.update { it.copy(isLoading = true, submissionSuccess = false) }

        viewModelScope.launch {
            avaliacaoRepository.getAvaliacaoById(avaliacaoIdToLoad)
                .onFailure {
                    _uiState.update { s -> s.copy(isLoading = false, error = "Falha ao carregar avaliação: ${it.message}") }
                    return@launch
                }
                .onSuccess { avaliacao ->
                    val formIdToLoad = avaliacao.formularioId
                    pacienteId = avaliacao.pacienteId

                    try {
                        val formResponse = RetrofitInstance.api.getFormularioById(formIdToLoad)
                        if (formResponse.isSuccessful && formResponse.body() != null) {

                            val respostasMap = avaliacao.respostas.associate {
                                it.pergunta.id to it.valor
                            }

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    form = formResponse.body(),
                                    respostas = respostasMap,
                                    pageTitle = "Editar: ${formResponse.body()!!.titulo}",
                                    dataCriacao = avaliacao.dataCriacao
                                )
                            }
                        } else {
                            _uiState.update { s -> s.copy(isLoading = false, error = "Falha ao carregar template do formulário.") }
                            loadFormForEdit()
                        }
                    } catch (e: Exception) {
                        _uiState.update { s -> s.copy(isLoading = false, error = "Erro de conexão ao carregar formulário.") }
                    }
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

    fun submitForm() {
        if (isEditMode) {
            updateAvaliacao()
        } else {
            createNewAvaliacao()
        }
    }

    private fun createNewAvaliacao() {
        if (_uiState.value.isSubmitting) return
        if (pacienteId == null) {
            _uiState.update { it.copy(error = "ID do paciente não encontrado.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }

        val agora = OffsetDateTime.now()
        val formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val dataFormatada = agora.format(formatador)

        val respostasDTO = _uiState.value.respostas.map { (perguntaId, valor) ->
            RespostaPostDTO(perguntaId = perguntaId, valor = valor)
        }

        val avaliacaoDTO = AvaliacaoPostDTO(
            pacienteId = checkNotNull(pacienteId),
            tecnicoId = tecnicoId,
            formularioId = checkNotNull(_uiState.value.form?.id),
            respostas = respostasDTO,
            dataCriacao = dataFormatada,
            dataAtualizacao = dataFormatada
        )

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.salvarAvaliacao(avaliacaoDTO)
                if (response.isSuccessful) {
                    _uiState.update { it.copy(submissionSuccess = true, isSubmitting = false) }
                } else {
                    _uiState.update { it.copy(error = "Falha ao enviar avaliação: ${response.code()}", isSubmitting = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "Erro de conexão ao enviar.", isSubmitting = false) }
            }
        }
    }

    private fun updateAvaliacao() {
        if (_uiState.value.isSubmitting) return
        val avaliacaoIdToUpdate = checkNotNull(avaliacaoId)
        if (tecnicoId == null || pacienteId == null) {
            _uiState.update { it.copy(error = "IDs de técnico ou paciente não encontrados.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true) }

        val agora = OffsetDateTime.now()
        val formatador = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val dataFormatada = agora.format(formatador)

        val respostasDTO = _uiState.value.respostas.map { (perguntaId, valor) ->
            RespostaPostDTO(perguntaId = perguntaId, valor = valor)
        }

        val avaliacaoDTO = AvaliacaoPostDTO(
            pacienteId = checkNotNull(pacienteId),
            tecnicoId = checkNotNull(tecnicoId),
            formularioId = checkNotNull(_uiState.value.form?.id),
            respostas = respostasDTO,
            dataCriacao = _uiState.value.dataCriacao ?: dataFormatada,
            dataAtualizacao = dataFormatada
        )

        viewModelScope.launch {
            avaliacaoRepository.updateAvaliacao(avaliacaoIdToUpdate, avaliacaoDTO)
                .onSuccess {
                    _uiState.update { it.copy(submissionSuccess = true, isSubmitting = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = "Falha ao atualizar: ${error.message}", isSubmitting = false) }
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