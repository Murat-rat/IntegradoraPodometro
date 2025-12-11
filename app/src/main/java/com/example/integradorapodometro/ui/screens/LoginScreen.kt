package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // Navegación controlada para que no haga loop
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess(state.username)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = state.username,
            onValueChange = viewModel::onUsernameChange,
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
            // si quieres, puedes agregar visualTransformation para ocultarla
        )

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Button(onClick = { viewModel.login() }) {
                Text("Iniciar Sesión")
            }
            Button(onClick = onRegisterClick) {
                Text("Registrarse")
            }
        }
    }
}
