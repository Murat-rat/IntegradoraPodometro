package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.viewmodel.RecorridosViewModel
import com.example.integradorapodometro.viewmodel.RecorridosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRoutesScreen(
    username: String,
    onStartNew: () -> Unit,
    onViewGlobal: () -> Unit,
    viewModel: RecorridosViewModel = viewModel(
        factory = RecorridosViewModelFactory(username)
    )
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Mis Recorridos", style = MaterialTheme.typography.headlineSmall)

        if (state.isLoading) {
            Text("Cargando...")
        }

        if (state.error != null) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(state.misRecorridos) { r ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // aquí podrías abrir diálogo de continuar/borrar
                        }
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text("Tiempo: ${r.tiempoMin} min")
                        Text("Distancia: ${r.distanciaKm} km")
                        Text("Velocidad: ${r.velocidadPromedio} km/h")
                        Text("Fecha: ${r.fecha}")
                        Button(onClick = { r.id?.let { viewModel.borrar(it) } }) {
                            Text("Borrar")
                        }
                    }
                }
            }
        }

        Button(
            onClick = onStartNew,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar nuevo recorrido")
        }
    }
}
