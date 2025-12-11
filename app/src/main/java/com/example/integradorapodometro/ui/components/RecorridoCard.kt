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
import java.util.Locale

@Composable
fun RecorridoCard(
    recorrido: RecorridoDto,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Formateo bonito de números
    val distanciaStr = String.format(Locale.getDefault(), "%.2f", recorrido.distanciaKm)
    val velocidadStr = String.format(Locale.getDefault(), "%.2f", recorrido.velocidadPromedio)

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
            Text(
                text = recorrido.usuario,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(text = "Tiempo: ${recorrido.tiempoMin} min")
            Text(text = "Distancia: $distanciaStr km")
            Text(text = "Velocidad Promedio: $velocidadStr km/h")
            Text(text = "Última Modificación: ${recorrido.fecha}")

            Spacer(Modifier.height(6.dp))

            Row {
                Text(
                    text = "Actualizar",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onUpdateClick() },
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Borrar",
                    modifier = Modifier.clickable { onDeleteClick() },
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
