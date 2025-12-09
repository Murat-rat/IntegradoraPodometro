package com.example.integradorapodometro.data

data class Caminata(
    val id: Int,
    val tiempo: Long,
    val distancia: Long,
    val velocidad: Long,
    val nombre: String,
    val fecha: Long
)
