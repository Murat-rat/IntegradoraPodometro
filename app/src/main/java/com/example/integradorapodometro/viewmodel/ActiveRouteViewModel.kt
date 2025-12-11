package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integradorapodometro.data.model.RecorridoDto
import com.example.integradorapodometro.data.remote.RecorridosApi
import com.example.integradorapodometro.data.remote.RetrofitClient
import com.example.integradorapodometro.data.repository.RecorridosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Estado que usa la pantalla de recorrido activo
data class ActiveRouteUiState(
    val segundos: Int = 0,
    val pasos: Int = 0,
    val distanciaKm: Double = 0.0,
    val velocidadPromedio: Double = 0.0,
    val isRunning: Boolean = false
)

class ActiveRouteViewModel(
    private val usuario: String
) : ViewModel() {

    // --- Retrofit / Repositorio ---
    private val api = RetrofitClient.retrofit.create(RecorridosApi::class.java)
    private val repo = RecorridosRepository(api)

    // --- StateFlow expuesto a la UI ---
    private val _uiState = MutableStateFlow(ActiveRouteUiState())
    val uiState: StateFlow<ActiveRouteUiState> = _uiState

    private var timerJob: Job? = null

    // Arranca el cronómetro
    fun start() {
        if (_uiState.value.isRunning) return

        _uiState.value = _uiState.value.copy(isRunning = true)

        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                delay(1000)
                val nuevosSegundos = _uiState.value.segundos + 1
                recalc(nuevosSegundos, _uiState.value.pasos)
            }
        }
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    fun stopAndSave() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        timerJob?.cancel()

        val state = _uiState.value
        if (state.segundos == 0) return

        val minutos = (state.segundos / 60.0).roundToInt()

        viewModelScope.launch {
            // aquí mandas el recorrido a la API
            val dto = RecorridoDto(
                usuario = usuario,
                tiempoMin = minutos,
                distanciaKm = state.distanciaKm,
                velocidadPromedio = state.velocidadPromedio,
                fecha = "2025-12-02" // puedes cambiar por la fecha actual
            )
            repo.crear(dto)
        }
    }

    // Lo llamará el sensor cuando detecte un paso
    fun onStepDetected() {
        val nuevosPasos = _uiState.value.pasos + 1
        recalc(_uiState.value.segundos, nuevosPasos)
    }

    // Recalcula distancia y velocidad
    private fun recalc(segundos: Int, pasos: Int) {
        val distanciaMetros = pasos * 0.8      // zancada aprox 0.8 m
        val distanciaKm = distanciaMetros / 1000.0
        val horas = segundos / 3600.0
        val velocidad = if (horas > 0) distanciaKm / horas else 0.0

        _uiState.value = _uiState.value.copy(
            segundos = segundos,
            pasos = pasos,
            distanciaKm = distanciaKm,
            velocidadPromedio = velocidad
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
