package com.tecno.aging.ui.screens.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.repository.AuthRepository
import com.tecno.aging.domain.models.auth.ResetPasswordDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ForgotPasswordUiState(
    val currentStep: Int = 1,
    val cpf: String = "",
    val otp: List<String> = List(6) { "" },
    val telefoneMascarado: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val resetSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    private val repository: AuthRepository = AuthRepository()

    fun onCpfChange(value: String) {
        _uiState.update { it.copy(cpf = value) }
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
        val cpfLimpo = _uiState.value.cpf.filter { it.isDigit() }
        if (cpfLimpo.length != 11) {
            _uiState.update { it.copy(errorMessage = "Por favor, insira um CPF válido.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            repository.enviarCodigo(cpfLimpo)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentStep = 2,
                            telefoneMascarado = response.telefone
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun verifyOtp() {
        val codigoCompleto = _uiState.value.otp.joinToString("")
        if (codigoCompleto.length != 6) {
            _uiState.update { it.copy(errorMessage = "Por favor, preencha o código de 6 dígitos.") }
            return
        }
        _uiState.update { it.copy(currentStep = 3, errorMessage = null) }
    }

    fun resetPassword() {
        val state = _uiState.value
        val codigoCompleto = state.otp.joinToString("")

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

            val dto = ResetPasswordDTO(
                cpf = state.cpf.filter { it.isDigit() },
                codigo = codigoCompleto,
                novaSenha = state.newPassword,
                confirmarSenha = state.confirmPassword
            )

            repository.resetarSenha(dto)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, resetSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}