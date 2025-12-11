package com.example.integradorapodometro.data.repository

import com.example.integradorapodometro.data.model.RecorridoDto
import com.example.integradorapodometro.data.remote.RecorridosApi

class RecorridosRepository(
    private val api: RecorridosApi
) {

    suspend fun obtenerGlobales(): List<RecorridoDto> =
        api.getRecorridosGlobales()

    suspend fun obtenerPorUsuario(usuario: String): List<RecorridoDto> =
        api.getRecorridosUsuario(usuario)

    suspend fun crear(recorrido: RecorridoDto): RecorridoDto =
        api.crearRecorrido(recorrido)

    suspend fun actualizar(id: Int, recorrido: RecorridoDto): RecorridoDto =
        api.actualizarRecorrido(id, recorrido)

    suspend fun borrar(id: Int) =
        api.borrarRecorrido(id)
}
