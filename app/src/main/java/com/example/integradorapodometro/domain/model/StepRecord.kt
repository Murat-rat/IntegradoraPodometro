package com.example.integradorapodometro.domain.model

data class StepRecord(
    val id: Int? = null,
    val date: String,
    val steps: Int,
    val goal: Int,
    val status: String
)