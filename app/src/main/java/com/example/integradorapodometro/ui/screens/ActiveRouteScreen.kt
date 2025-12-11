package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.viewmodel.ActiveRouteViewModel
import com.example.integradorapodometro.viewmodel.ActiveRouteViewModelFactory


@Composable
fun ActiveRouteScreen(
    username: String,
    onFinish: () -> Unit
) {
    // Factory del ViewModel
    val factory = remember { ActiveRouteViewModelFactory(username) }

    // ViewModel usando el factory
    val viewModel: ActiveRouteViewModel = viewModel(factory = factory)

    // Leemos el estado directamente (sin collectAsState)
    val state = viewModel.uiState.value

    // ------------ UI ------------
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        val minutes = state.segundos / 60
        val seconds = state.segundos % 60

        val minText = if (minutes < 10) "0$minutes" else "$minutes"
        val secText = if (seconds < 10) "0$seconds" else "$seconds"
        val timeText = "$minText:$secText"

        val distanciaRedondeada =
            kotlin.math.round(state.distanciaKm * 100) / 100.0
        val velocidadRedondeada =
            kotlin.math.round(state.velocidadPromedio * 100) / 100.0

        Text(timeText)
        Text("Pasos: ${state.pasos}")
        Text("Distancia: $distanciaRedondeada km")
        Text("Velocidad: $velocidadRedondeada km/h")

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { viewModel.pause() }) {
                Text("Pausar recorrido")
            }
            Button(onClick = {
                viewModel.stopAndSave()
                onFinish()
            }) {
                Text("Detener recorrido")
            }
        }
    }
}
