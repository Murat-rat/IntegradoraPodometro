package com.example.integradorapodometro.data.repository
import com.example.integradorapodometro.data.remote.StepApiService
import com.example.integradorapodometro.data.remote.toDomain
import com.example.integradorapodometro.data.remote.toDto
import com.example.integradorapodometro.domain.model.StepRecord

class StepRepository(
    private val api: StepApiService
) {

    suspend fun getSteps(): List<StepRecord> {
        return api.getSteps().map { it.toDomain() }
    }

    suspend fun createStep(record: StepRecord): StepRecord {
        return api.createStep(record.toDto()).toDomain()
    }

    suspend fun updateStep(record: StepRecord): StepRecord {
        val id = record.id ?: error("ID requerido para actualizar")
        return api.updateStep(id, record.toDto()).toDomain()
    }

    suspend fun deleteStep(id: Int) {
        api.deleteStep(id)
    }
}