package com.tecno.aging.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthState {
    LOADING,
    AUTHENTICATED,
    UNAUTHENTICATED
}

class SplashViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState.LOADING)
    val authState = _authState.asStateFlow()

    init {
        checkTokenValidity()
    }

    private fun checkTokenValidity() {
        viewModelScope.launch {
            delay(1000)

            val token = SessionManager.getAuthToken()
            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.UNAUTHENTICATED
                return@launch
            }

            repository.verifyJwt().onSuccess { isValid ->
                _authState.value = if (isValid) AuthState.AUTHENTICATED else AuthState.UNAUTHENTICATED
            }.onFailure {
                _authState.value = AuthState.UNAUTHENTICATED
            }
        }
    }
}