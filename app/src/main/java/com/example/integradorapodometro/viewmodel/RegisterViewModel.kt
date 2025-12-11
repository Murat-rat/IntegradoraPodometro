package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integradorapodometro.data.remote.RecorridosApi
import com.example.integradorapodometro.data.remote.RetrofitClient
import com.example.integradorapodometro.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false
)

class RegisterViewModel : ViewModel() {

    private val api = RetrofitClient.retrofit.create(RecorridosApi::class.java)
    private val repo = AuthRepository(api)

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = value)
    }

    fun register() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            _uiState.value = state.copy(error = "Todos los campos son obligatorios")
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, error = null)
                repo.register(state.username, state.password)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al registrar: ${e.message}"
                )
            }
        }
    }
}
