package com.example.integradorapodometro.data.remote
import com.example.integradorapodometro.domain.model.StepRecord

data class StepRecordDto(
    val id: Int? = null,
    val date: String,
    val steps: Int,
    val goal: Int,
    val status: String
)

fun StepRecordDto.toDomain() = StepRecord(
    id = id,
    date = date,
    steps = steps,
    goal = goal,
    status = status
)

fun StepRecord.toDto() = StepRecordDto(
    id = id,
    date = date,
    steps = steps,
    goal = goal,
    status = status
)
