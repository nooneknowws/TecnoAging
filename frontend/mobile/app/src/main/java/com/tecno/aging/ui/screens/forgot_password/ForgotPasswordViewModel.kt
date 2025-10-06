package com.tecno.aging.ui.screens.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val currentStep: Int = 1,
    val email: String = "",
    val otp: List<String> = List(5) { "" },
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val resetSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onOtpChange(index: Int, value: String) {
        if (value.length <= 1) {
            val newOtp = _uiState.value.otp.toMutableList()
            newOtp[index] = value
            _uiState.update { it.copy(otp = newOtp) }
        }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun sendOtp() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            _uiState.update { it.copy(errorMessage = "Por favor, insira um e-mail válido.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500)
            _uiState.update { it.copy(isLoading = false, currentStep = 2) }
        }
    }

    fun verifyOtp() {
        if (_uiState.value.otp.any { it.isBlank() }) {
            _uiState.update { it.copy(errorMessage = "Por favor, preencha todos os campos do código.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500)
            _uiState.update { it.copy(isLoading = false, currentStep = 3) }
        }
    }

    fun resetPassword() {
        val state = _uiState.value
        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "A nova senha deve ter no mínimo 6 caracteres.") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "As senhas não coincidem.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500)
            _uiState.update { it.copy(isLoading = false, resetSuccess = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}