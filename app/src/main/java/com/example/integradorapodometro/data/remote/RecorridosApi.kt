package com.example.integradorapodometro.data.remote

import com.example.integradorapodometro.data.model.UserDto
import com.example.integradorapodometro.data.model.RecorridoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.PUT

interface RecorridosApi {

    // ---------- AUTH ----------
    @POST("users/login")
    suspend fun login(@Body user: UserDto): UserDto

    @POST("users/register")
    suspend fun register(@Body user: UserDto): UserDto

    // ---------- RECORRIDOS ----------
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
