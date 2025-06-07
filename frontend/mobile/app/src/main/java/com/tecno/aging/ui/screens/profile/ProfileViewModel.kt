package com.tecno.aging.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    sealed class ProfileUiState {
        data class Success(val tecnico: Tecnico) : ProfileUiState()
        object Loading : ProfileUiState()
        data class Error(val message: String) : ProfileUiState()
    }

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val id = SessionManager.getUserId() ?: ""
                val tecnico = RetrofitInstance.api.buscarTecnico(id)
                _uiState.value = ProfileUiState.Success(tecnico)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Falha ao carregar perfil: ${e.message}")
            }
        }
    }

    fun refreshData() {
        loadProfileData()
    }
}