package com.example.integradorapodometro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.integradorapodometro.data.repository.StepRepository
import com.example.integradorapodometro.domain.model.StepRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val stepsToday: Int = 0,
    val goal: Int = 10000,
    val isLoading: Boolean = false,
    val error: String? = null,
    val history: List<StepRecord> = emptyList()
)

class HomeViewModel(
    private val repository: StepRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHistory()
    }

    fun onStepsFromSensor(steps: Int) {
        _uiState.update { it.copy(stepsToday = steps) }
    }

    fun updateGoal(newGoal: Int) {
        _uiState.update { it.copy(goal = newGoal) }
    }

    fun loadHistory() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val data = repository.getSteps()
                _uiState.update { it.copy(isLoading = false, history = data) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun saveTodayRecord() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val today = LocalDate.now().toString()
                val status = if (state.stepsToday >= state.goal) "COMPLETED" else "PENDING"

                val record = StepRecord(
                    date = today,
                    steps = state.stepsToday,
                    goal = state.goal,
                    status = status
                )

                val created = repository.createStep(record)
                _uiState.update {
                    it.copy(history = it.history + created, error = null)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteRecord(record: StepRecord) {
        val id = record.id ?: return
        viewModelScope.launch {
            try {
                repository.deleteStep(id)
                _uiState.update {
                    it.copy(history = it.history.filterNot { r -> r.id == id })
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}

class HomeViewModelFactory(
    private val repository: StepRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
