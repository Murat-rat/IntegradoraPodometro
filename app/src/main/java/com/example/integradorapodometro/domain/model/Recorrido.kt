package com.example.integradorapodometro.domain.model

data class Recorrido(
    val id: Int,
    val usuario: String,
    val tiempoMin: Int,
    val distanciaKm: Double,
    val velocidadPromedio: Double,
    val fecha: String
)
