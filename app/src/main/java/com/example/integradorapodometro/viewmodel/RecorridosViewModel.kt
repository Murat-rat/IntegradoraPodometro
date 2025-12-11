package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integradorapodometro.data.model.RecorridoDto
import com.example.integradorapodometro.data.remote.RecorridosApi
import com.example.integradorapodometro.data.remote.RetrofitClient
import com.example.integradorapodometro.data.repository.RecorridosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RecorridosUiState(
    val misRecorridos: List<RecorridoDto> = emptyList(),
    val globales: List<RecorridoDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecorridosViewModel(
    private val usuario: String
) : ViewModel() {

    private val api = RetrofitClient.retrofit.create(RecorridosApi::class.java)
    private val repo = RecorridosRepository(api)

    private val _uiState = MutableStateFlow(RecorridosUiState())
    val uiState: StateFlow<RecorridosUiState> = _uiState

    init {
        cargarMisRecorridos()
        cargarGlobales()
    }

    fun cargarMisRecorridos() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val lista = repo.obtenerPorUsuario(usuario)
                _uiState.value = _uiState.value.copy(
                    misRecorridos = lista,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar mis recorridos"
                )
            }
        }
    }

    fun cargarGlobales() {
        viewModelScope.launch {
            try {
                val lista = repo.obtenerGlobales()
                _uiState.value = _uiState.value.copy(globales = lista)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar globales"
                )
            }
        }
    }

    fun borrarRecorrido(id: Int) {
        viewModelScope.launch {
            try {
                repo.borrar(id)
                cargarMisRecorridos()
                cargarGlobales()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo borrar el recorrido"
                )
            }
        }
    }

    // UPDATE (PUT): ejemplo sencillo, incrementa 5 minutos
    fun actualizarRecorrido(recorrido: RecorridoDto) {
        val id = recorrido.id ?: return
        viewModelScope.launch {
            try {
                val actualizado = recorrido.copy(
                    tiempoMin = recorrido.tiempoMin + 5
                )
                repo.actualizar(id, actualizado)
                cargarMisRecorridos()
                cargarGlobales()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo actualizar el recorrido"
                )
            }
        }
    }
}
