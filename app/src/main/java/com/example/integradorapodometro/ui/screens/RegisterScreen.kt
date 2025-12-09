package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.integradorapodometro.ui.components.InputField
import com.example.integradorapodometro.viewmodel.LoginViewModel

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ) {

        Text("Inicio de Sesión")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {}) {
                Text("Iniciar Sesion")
            }

            Button(onClick = {}) {
                Text("Registrarse")
            }
        }

        InputField(
            value = viewModel.username.value,
            onValueChange = { viewModel.username.value = it },
            label = "Usuario"
        )

        InputField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = "Contraseña"
        )

        if (viewModel.loginError.value.isNotEmpty()) {
            Text(
                text = viewModel.loginError.value,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(onClick = {}) {
            Text("Iniciar Sesion")
            viewModel.login {
                navController.navigate("menu") {
                    popUpTo("login") { inclusive = true } // Evita volver al login
                }
            }
        }
    }
}