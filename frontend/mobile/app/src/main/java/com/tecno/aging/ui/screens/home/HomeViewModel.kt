package com.tecno.aging.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {
    sealed class HomeEvent {
        object Logout : HomeEvent()
        object NavigateToProfile : HomeEvent()
    }

    sealed class HomeUiState {
        data class Success(
            val userName: String,
            val userId: String,
            val userProfile: String
        ) : HomeUiState()

        object Loading : HomeUiState()
        object LogoutSuccess : HomeUiState()
        data class Error(val message: String) : HomeUiState()
    }

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val id = SessionManager.getUserId() ?: ""
                val user = RetrofitInstance.api.buscarTecnico(id)

                _uiState.value = HomeUiState.Success(
                    userName = user.nome,
                    userId = user.id,
                    userProfile = SessionManager.getUserProfile().toString()
                )

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Failed to load user data: ${e.message}")
            }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when(event) {
            HomeEvent.Logout -> logout()
            HomeEvent.NavigateToProfile -> navigateToProfile()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                RetrofitInstance.api.logout()
                _uiState.value = HomeUiState.LogoutSuccess
            } catch (e: IOException) {
                _uiState.value = HomeUiState.Error("Logout failed: ${e.message}")
                Log.e("HomeViewModel", "Logout error", e)
            }
        }
    }

    private fun navigateToProfile() {
        // You could either handle navigation here through state
        // or keep it simple and let the composable handle it
    }
}