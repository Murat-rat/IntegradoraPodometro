package com.example.integradorapodometro.data.remote

import com.example.integradorapodometro.data.model.RecorridoDto
import com.example.integradorapodometro.data.model.UserDto
import retrofit2.http.*

interface RecorridosApi {

    // ---------- Auth (demo sencilla, sin tokens) ----------
    @POST("users/login")
    suspend fun login(@Body user: UserDto): UserDto

    @POST("users/register")
    suspend fun register(@Body user: UserDto): UserDto

    // ---------- Recorridos ----------
    @GET("recorridos")
    suspend fun getRecorridosGlobales(): List<RecorridoDto>

    @GET("recorridos/usuario/{usuario}")
    suspend fun getRecorridosUsuario(
        @Path("usuario") usuario: String
    ): List<RecorridoDto>

    @POST("recorridos")
    suspend fun crearRecorrido(
        @Body recorrido: RecorridoDto
    ): RecorridoDto

    @PUT("recorridos/{id}")
    suspend fun actualizarRecorrido(
        @Path("id") id: Int,
        @Body recorrido: RecorridoDto
    ): RecorridoDto

    @DELETE("recorridos/{id}")
    suspend fun borrarRecorrido(
        @Path("id") id: Int
    )
}
