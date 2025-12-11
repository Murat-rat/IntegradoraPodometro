package com.example.integradorapodometro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.integradorapodometro.domain.model.StepRecord
import com.example.integradorapodometro.viewmodel.HomeUiState
import androidx.compose.foundation.text.KeyboardOptions




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onRefresh: () -> Unit,
    onSaveToday: () -> Unit,
    onDeleteRecord: (StepRecord) -> Unit,
    onGoalChange: (Int) -> Unit,
    onResetSteps: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Podómetro") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            StepsCard(
                stepsToday = state.stepsToday,
                goal = state.goal,
                onGoalChange = onGoalChange,
                onSaveToday = onSaveToday,
                onResetSteps = onResetSteps
            )

            Spacer(modifier = Modifier.height(16.dp))

            HistorySection(
                history = state.history,
                isLoading = state.isLoading,
                error = state.error,
                onRefresh = onRefresh,
                onDeleteRecord = onDeleteRecord
            )
        }
    }
}

@Composable
private fun StepsCard(
    stepsToday: Int,
    goal: Int,
    onGoalChange: (Int) -> Unit,
    onSaveToday: () -> Unit,
    onResetSteps: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text("Pasos de hoy", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$stepsToday pasos",
                style = MaterialTheme.typography.headlineMedium
            )

            val progress =
                if (goal > 0) (stepsToday.toFloat() / goal).coerceIn(0f, 1f) else 0f

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = "Meta: $goal pasos",
                style = MaterialTheme.typography.bodyMedium
            )

            var goalInput by remember { mutableStateOf(goal.toString()) }

            androidx.compose.material3.OutlinedTextField(
                value = goalInput,
                onValueChange = {
                    goalInput = it
                    it.toIntOrNull()?.let(onGoalChange)
                },
                label = { Text("Actualizar meta diaria") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onResetSteps) {
                    Text("Reiniciar pasos")
                }
                Button(onClick = onSaveToday) {
                    Text("Guardar registro de hoy")
                }
            }
        }
    }
}

@Composable
private fun HistorySection(
    history: List<StepRecord>,
    isLoading: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    onDeleteRecord: (StepRecord) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Historial", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = onRefresh) {
            Text("Actualizar")
        }
    }

    when {
        isLoading -> {
            CircularProgressIndicator()
        }

        error != null -> {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error
            )
        }

        history.isEmpty() -> {
            Text("No hay registros aún.")
        }

        else -> {
            LazyColumn {
                items(history) { record ->
                    HistoryItem(
                        record = record,
                        onDelete = { onDeleteRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(
    record: StepRecord,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(record.date, style = MaterialTheme.typography.bodyMedium)
                Text("${record.steps} pasos", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Meta: ${record.goal} – ${record.status}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(onClick = onDelete) {
                Text("Eliminar")
            }
        }
    }
}

