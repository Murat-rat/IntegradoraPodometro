package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.sensor.StepCounterManager
import com.example.integradorapodometro.viewmodel.ActiveRouteViewModel
import com.example.integradorapodometro.viewmodel.ActiveRouteViewModelFactory

@Composable
fun ActiveRouteScreen(
    username: String,
    onFinish: () -> Unit,
    viewModel: ActiveRouteViewModel = viewModel(
        factory = ActiveRouteViewModelFactory(username)
    )
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val stepManager = remember {
        StepCounterManager(context) {
            viewModel.onStepDetected()
        }
    }

    LaunchedEffect(state.guardadoOk) {
        if (state.guardadoOk) {
            onFinish()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.start()
        stepManager.start()
    }

    DisposableEffect(Unit) {
        onDispose { stepManager.stop() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        val minutes = state.segundos / 60
        val seconds = state.segundos % 60

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.headlineSmall
        )

        Text("Pasos: ${state.pasos}")
        Text("Distancia: ${"%.2f".format(state.distanciaKm)} km")
        Text("Velocidad: ${"%.2f".format(state.velocidadPromedio)} km/h")

        if (state.error != null) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.pause() }) {
                Text("Pausar recorrido")
            }
            Button(onClick = { viewModel.stopAndSave() }) {
                Text("Detener recorrido")
            }
        }
    }
}
