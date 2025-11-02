package com.tecno.aging.ui.screens.forms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.forms.GenericForm
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormsListUiState(
    val forms: List<GenericForm> = emptyList(),
    val patients: List<Paciente> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class FormsListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FormsListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun refresh() {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val formsResponse = RetrofitInstance.api.getTodosFormularios()
                val patientsResponse = RetrofitInstance.api.getPacientes()

                if (formsResponse.isSuccessful && patientsResponse.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            forms = formsResponse.body() ?: emptyList(),
                            patients = patientsResponse.body() ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update { it.copy(error = "Falha ao buscar dados", isLoading = false) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "", isLoading = false) }
                loadInitialData()
            }
        }
    }
}