package com.example.integradorapodometro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.integradorapodometro.data.remote.RetrofitClient
import com.example.integradorapodometro.data.repository.StepRepository
import com.example.integradorapodometro.sensors.StepSensorManager
import com.example.integradorapodometro.ui.screens.HomeScreen
import com.example.integradorapodometro.ui.theme.IntegradoraPodometroTheme
import com.example.integradorapodometro.viewmodel.HomeViewModel
import com.example.integradorapodometro.viewmodel.HomeViewModelFactory

class MainActivity : ComponentActivity() {

    // ViewModel del podómetro (MVVM)
    private val homeViewModel: HomeViewModel by viewModels {
        val api = RetrofitClient.stepApi
        val repo = StepRepository(api)
        HomeViewModelFactory(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IntegradoraPodometroTheme {
                val state by homeViewModel.uiState.collectAsState()
                val context = LocalContext.current

                // Manager del acelerómetro
                val sensorManager = remember {
                    StepSensorManager(
                        context = context,
                        onNewStepCount = { steps ->
                            homeViewModel.onStepsFromSensor(steps)
                        }
                    )
                }

                // Registrar / desregistrar el listener del sensor
                DisposableEffect(Unit) {
                    sensorManager.start()
                    onDispose { sensorManager.stop() }
                }

                // Pantalla principal del podómetro
                HomeScreen(
                    state = state,
                    onRefresh = { homeViewModel.loadHistory() },
                    onSaveToday = { homeViewModel.saveTodayRecord() },
                    onDeleteRecord = { homeViewModel.deleteRecord(it) },
                    onGoalChange = { homeViewModel.updateGoal(it) },
                    onResetSteps = { sensorManager.resetSteps() }
                )
            }
        }
    }
}
