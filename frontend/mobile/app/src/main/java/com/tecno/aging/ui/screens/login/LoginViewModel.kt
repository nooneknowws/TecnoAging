
package com.tecno.aging.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.auth.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.CpfChanged -> updateCpf(event.value)
            is LoginEvent.PasswordChanged -> updatePassword(event.value)
            is LoginEvent.ToggleVisibility -> togglePasswordVisibility()
            is LoginEvent.Submit -> performLogin()
        }
    }

    private fun updateCpf(value: String) {
        _uiState.value = _uiState.value.copy(
            cpf = value,
            cpfError = "",
            loginError = ""
        )
    }

    private fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordError = "",
            loginError = ""
        )
    }

    private fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            passwordVisible = !_uiState.value.passwordVisible
        )
    }

    private fun performLogin() {
        val currentCpf = _uiState.value.cpf.trim()
        val currentPassword = _uiState.value.password.trim()

        val cpfError = if (currentCpf.isBlank()) "CPF é obrigatório" else ""
        val passwordError = if (currentPassword.isBlank()) "Senha é obrigatória" else ""

        _uiState.value = _uiState.value.copy(
            cpfError = cpfError,
            passwordError = passwordError,
            isLoading = cpfError.isEmpty() && passwordError.isEmpty(),
            loginError = ""
        )

        if (cpfError.isEmpty() && passwordError.isEmpty()) {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance.api.login(
                        LoginRequest(
                            cpf = currentCpf,
                            senha = currentPassword
                        )
                    )


                    when {
                        response.isSuccessful -> {
                            response.body()?.let { loginResponse ->

                                if (loginResponse.success) {
                                    SessionManager.apply {
                                        saveAuthToken(loginResponse.token)
                                        saveUserId(loginResponse.ID)
                                        saveUserProfile(loginResponse.Perfil)
                                        saveUserName(loginResponse.Nome)
                                    }
                                    _uiState.value = _uiState.value.copy(
                                        loginSuccess = true,
                                        isLoading = false
                                    )
                                } else {
                                    _uiState.value = _uiState.value.copy(
                                        loginError = loginResponse.message ?: "Erro ao realizar login",
                                        isLoading = false
                                    )
                                }
                            } ?: run {
                                _uiState.value = _uiState.value.copy(
                                    loginError = "Resposta inválida do servidor",
                                    isLoading = false
                                )
                            }
                        }

                        else -> {
                            _uiState.value = _uiState.value.copy(
                                loginError = when (response.code()) {
                                    401 -> "Credenciais inválidas"
                                    400 -> "Requisição inválida"
                                    500 -> "Erro interno do servidor"
                                    else -> "Erro desconhecido (${response.code()})"
                                },
                                isLoading = false
                            )
                        }
                    }
                } catch (e: IOException) {
                    _uiState.value = _uiState.value.copy(
                        loginError = "Erro de conexão. Verifique sua internet.",
                        isLoading = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        loginError = "Erro inesperado: ${e.localizedMessage ?: "Unknown error"}",
                        isLoading = false
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

data class LoginUiState(
    val cpf: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val cpfError: String = "",
    val passwordError: String = "",
    val loginError: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

sealed class LoginEvent {
    data class CpfChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    object ToggleVisibility : LoginEvent()
    object Submit : LoginEvent()
}
}