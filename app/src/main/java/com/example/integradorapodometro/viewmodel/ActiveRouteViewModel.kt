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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

// ---------- STATE PARA LA PANTALLA DE RECORRIDO ACTIVO ----------
data class ActiveRouteUiState(
    val segundos: Int = 0,
    val pasos: Int = 0,
    val distanciaKm: Double = 0.0,
    val velocidadPromedio: Double = 0.0,
    val isRunning: Boolean = false,
    val guardadoOk: Boolean = false,
    val error: String? = null
)

// ---------- VIEWMODEL ----------
class ActiveRouteViewModel(
    private val usuario: String
) : ViewModel() {

    private val api = RetrofitClient.retrofit.create(RecorridosApi::class.java)
    private val repo = RecorridosRepository(api)

    private val _uiState = MutableStateFlow(ActiveRouteUiState())
    val uiState: StateFlow<ActiveRouteUiState> = _uiState

    private var timerJob: Job? = null

    // Inicia el cronómetro
    fun start() {
        if (_uiState.value.isRunning) return

        _uiState.value = _uiState.value.copy(
            isRunning = true,
            guardadoOk = false,
            error = null
        )

        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                delay(1000)
                val newSeconds = _uiState.value.segundos + 1
                recalc(newSeconds, _uiState.value.pasos)
            }
        }
    }

    // Pausa el cronómetro (no borra datos)
    fun pause() {
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    // Detiene, guarda en API y marca guardadoOk
    fun stopAndSave() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        timerJob?.cancel()

        val state = _uiState.value
        if (state.segundos == 0) return  // nada que guardar

        val minutos = (state.segundos / 60.0).roundToInt()

        viewModelScope.launch {
            try {
                // Fecha actual yyyy-MM-dd
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaActual = formatter.format(Date())

                val dto = RecorridoDto(
                    usuario = usuario,
                    tiempoMin = minutos,
                    distanciaKm = state.distanciaKm,
                    velocidadPromedio = state.velocidadPromedio,
                    fecha = fechaActual
                )

                repo.crear(dto)

                _uiState.value = _uiState.value.copy(guardadoOk = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al guardar: ${e.message}"
                )
            }
        }
    }

    // Lo llama el StepCounterManager por cada paso detectado
    fun onStepDetected() {
        val newSteps = _uiState.value.pasos + 1
        recalc(_uiState.value.segundos, newSteps)
    }

    // Recalcula distancia y velocidad con los datos actuales
    private fun recalc(segundos: Int, pasos: Int) {
        val distanciaMetros = pasos * 0.8          // ~0.8 m por paso
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
