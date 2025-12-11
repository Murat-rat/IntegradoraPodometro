package com.example.integradorapodometro.data.remote
import retrofit2.http.*

interface StepApiService {

    @GET("steps")
    suspend fun getSteps(): List<StepRecordDto>

    @POST("steps")
    suspend fun createStep(@Body step: StepRecordDto): StepRecordDto

    @PUT("steps/{id}")
    suspend fun updateStep(
        @Path("id") id: Int,
        @Body step: StepRecordDto
    ): StepRecordDto

    @DELETE("steps/{id}")
    suspend fun deleteStep(@Path("id") id: Int)
}