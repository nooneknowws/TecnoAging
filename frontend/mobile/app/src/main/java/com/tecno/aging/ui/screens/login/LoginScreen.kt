package com.tecno.aging.ui.screens.login

import com.tecno.aging.domain.models.auth.LoginResponse
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tecno.aging.R
import com.tecno.aging.data.local.SessionManager
import com.tecno.aging.data.remote.RetrofitInstance
import com.tecno.aging.domain.models.auth.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun LoginScreen(navController: NavController) {
    var cpf by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var cpfError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "TecnoAging", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = cpf,
            onValueChange = {
                cpf = it
                cpfError = ""
                loginError = ""
            },
            label = {
                Text(
                    text = cpfError.ifEmpty { "CPF" },
                    color = if (cpfError.isNotEmpty()) Red else Color.Unspecified
                )
            },
            leadingIcon = { Icon(Icons.Rounded.AccountCircle, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = ""
                loginError = ""
            },
            label = {
                Text(
                    text = passwordError.ifEmpty { "Senha" },
                    color = if (passwordError.isNotEmpty()) Red else Color.Unspecified
                )
            },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.visibility_24)
                else
                    painterResource(id = R.drawable.visibility_off_24)

                Icon(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
            )
        )

        if (loginError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginError, color = Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                cpfError = if (cpf.isBlank()) "CPF é obrigatório" else ""
                passwordError = if (password.isBlank()) "Senha é obrigatória" else ""

                if (cpfError.isEmpty() && passwordError.isEmpty()) {
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitInstance.api.login(
                                LoginRequest(
                                    cpf = cpf.trim(),
                                    senha = password.trim()
                                )
                            )

                            if (response.isSuccessful) {
                                val loginResponse = response.body()
                                if (loginResponse?.success == true) {
                                    SessionManager.apply {
                                        saveAuthToken(loginResponse.token)
                                        saveUserId(loginResponse.ID)
                                        saveUserProfile(loginResponse.Perfil)
                                    }

                                    withContext(Dispatchers.Main) {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                } else {
                                    loginError = loginResponse?.message ?: "Erro ao realizar login"
                                }
                            } else {
                                loginError = when (response.code()) {
                                    401 -> "Credenciais inválidas"
                                    400 -> "Requisição inválida"
                                    else -> "Erro no servidor"
                                }
                            }
                        } catch (e: IOException) {
                            loginError = "Erro de conexão. Verifique sua internet."
                        } catch (e: Exception) {
                            loginError = "Erro inesperado: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Entrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esqueceu a senha?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {  }
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            Text(text = "Não tem uma conta?")
            Text(
                text = " Cadastre-se",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate("cadastro")
                }
            )
        }
    }
}