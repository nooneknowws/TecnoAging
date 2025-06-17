package com.tecno.aging.ui.screens.forms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tecno.aging.domain.models.forms.GenericForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStreamReader

data class FormUiState(
    val form: GenericForm? = null,
    val etapaAtual: Int = 0,
    val respostas: Map<String, String> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class FormViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState: StateFlow<FormUiState> = _uiState.asStateFlow()

    fun carregarFormulario(fileName: String) {
        // Evita recarregar se já estiver carregado
        if (_uiState.value.form != null && _uiState.value.form?.tipo == fileName.removeSuffix(".json")) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val jsonStream = context.assets.open("forms/$fileName")
                val reader = InputStreamReader(jsonStream)
                val form = Gson().fromJson(reader, GenericForm::class.java)
                _uiState.update { it.copy(form = form, isLoading = false, etapaAtual = 0, respostas = emptyMap()) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "Falha ao carregar o formulário.", isLoading = false) }
            }
        }
    }

    fun onRespostaChanged(perguntaTexto: String, etapaIndex: Int, resposta: String) {
        val chaveResposta = "etapa_${etapaIndex}_pergunta_${perguntaTexto}"
        _uiState.update { currentState ->
            val novasRespostas = currentState.respostas.toMutableMap()
            novasRespostas[chaveResposta] = resposta
            currentState.copy(respostas = novasRespostas)
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
}