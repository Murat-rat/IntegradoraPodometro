package com.example.integradorapodometro.data.repository

import com.example.integradorapodometro.data.model.UserDto
import com.example.integradorapodometro.data.remote.RecorridosApi

class AuthRepository(
    private val api: RecorridosApi
) {

    suspend fun login(username: String, password: String): UserDto {
        val dto = UserDto(username = username, password = password)
        return api.login(dto)
    }

    suspend fun register(username: String, password: String): UserDto {
        val dto = UserDto(username = username, password = password)
        return api.register(dto)
    }
}
