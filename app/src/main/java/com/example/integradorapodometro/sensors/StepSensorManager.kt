package com.example.integradorapodometro.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class StepSensorManager(
    context: Context,
    private val onNewStepCount: (Int) -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastMagnitude: Float = 0f
    private var stepCount: Int = 0
    private var lastStepTimeNs: Long = 0L

    private val STEP_THRESHOLD = 1.2f            // sensibilidad del paso
    private val STEP_DELAY_NS = 250_000_000L     // 250 ms entre pasos

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun resetSteps() {
        stepCount = 0
        onNewStepCount(stepCount)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitude = sqrt(x * x + y * y + z * z)
        val delta = magnitude - lastMagnitude
        lastMagnitude = magnitude

        val now = System.nanoTime()

        if (delta > STEP_THRESHOLD && (now - lastStepTimeNs) > STEP_DELAY_NS) {
            stepCount++
            lastStepTimeNs = now
            onNewStepCount(stepCount)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // no lo usamos
    }
}
