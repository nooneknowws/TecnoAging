package com.tecno.aging.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userId: String = "",
    val userProfile: String = ""
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        _uiState.update {
            it.copy(
                userName = SessionManager.getUserName().orEmpty(),
                userId = SessionManager.getUserId().orEmpty(),
                userProfile = SessionManager.getUserProfile().orEmpty()
            )
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500)
            loadUserData()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}