package com.example.integradorapodometro.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.ui.components.RecorridoCard
import com.example.integradorapodometro.viewmodel.RecorridosViewModel
import com.example.integradorapodometro.viewmodel.RecorridosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalRoutesScreen(
    username: String,
    onStartNew: () -> Unit,
    onViewMine: () -> Unit
) {
    val factory = remember { RecorridosViewModelFactory(username) }
    val viewModel: RecorridosViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recorridos Globales") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onStartNew) {
                    Text("Iniciar Nuevo Recorrido")
                }
                Button(onClick = onViewMine) {
                    Text("Ver Mis Recorridos")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (state.error != null) {
                Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.globales) { recorrido ->
                    RecorridoCard(
                        recorrido = recorrido,
                        onUpdateClick = { /* solo lectura aquí */ },
                        onDeleteClick = { /* nada, no borras globales de otros */ }
                    )
                }
            }
        }
    }
}
