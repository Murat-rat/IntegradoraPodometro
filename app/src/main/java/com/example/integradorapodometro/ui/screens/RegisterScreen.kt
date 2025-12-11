package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isRegistered) {
        // Cuando se registra, regresa al login
        onBackToLogin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuario") },
            singleLine = true
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true
        )

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirmar contraseña") },
            singleLine = true
        )

        if (state.error != null) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBackToLogin) {
                Text("Cancelar")
            }
            Button(onClick = { viewModel.register() }) {
                Text("Registrarse")
            }
        }
    }
}
