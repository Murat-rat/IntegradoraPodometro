package com.example.integradorapodometro.data.model

data class RecorridoDto(
    val id: Int? = null,
    val usuario: String,
    val tiempoMin: Int,
    val distanciaKm: Double,
    val velocidadPromedio: Double,
    val fecha: String
)
