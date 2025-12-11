package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradorapodometro.data.model.RecorridoDto
import com.example.integradorapodometro.ui.components.RecorridoCard
import com.example.integradorapodometro.viewmodel.RecorridosViewModel
import com.example.integradorapodometro.viewmodel.RecorridosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRoutesScreen(
    username: String,
    onStartNew: () -> Unit,
    onViewGlobal: () -> Unit
) {
    val factory = remember { RecorridosViewModelFactory(username) }
    val viewModel: RecorridosViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<RecorridoDto?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recorridos") }
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
                Button(onClick = onViewGlobal) {
                    Text("Ver Recorridos de Otros")
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
                items(state.misRecorridos) { recorrido ->
                    RecorridoCard(
                        recorrido = recorrido,
                        onUpdateClick = {
                            selected = recorrido
                            showUpdateDialog = true
                        },
                        onDeleteClick = {
                            selected = recorrido
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDeleteDialog && selected != null && selected?.id != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar recorrido") },
            text = { Text("¿Está seguro que desea eliminar este recorrido?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.borrarRecorrido(selected!!.id!!)
                    showDeleteDialog = false
                }) { Text("Sí") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("No") }
            }
        )
    }

    if (showUpdateDialog && selected != null) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = { Text("Actualizar recorrido") },
            text = { Text("Se incrementarán 5 minutos al tiempo como ejemplo de UPDATE.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.actualizarRecorrido(selected!!)
                    showUpdateDialog = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showUpdateDialog = false }) { Text("Cancelar") }
            }
        )
    }
}
