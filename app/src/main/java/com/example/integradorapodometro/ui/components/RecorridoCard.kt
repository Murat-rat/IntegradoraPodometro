package com.example.integradorapodometro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.integradorapodometro.data.model.RecorridoDto

@Composable
fun RecorridoCard(
    recorrido: RecorridoDto,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .clickable { onUpdateClick() }
        ) {
            Text(recorrido.usuario, style = MaterialTheme.typography.bodyLarge)
            Text("Tiempo: ${recorrido.tiempoMin} minutos")
            Text("Distancia: ${recorrido.distanciaKm} km")
            Text("Velocidad Promedio: ${recorrido.velocidadPromedio} km/h")
            Text("Última Modificación: ${recorrido.fecha}")

            Spacer(Modifier.height(6.dp))
            Row {
                Text(
                    "Actualizar",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onUpdateClick() },
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Borrar",
                    modifier = Modifier.clickable { onDeleteClick() },
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
