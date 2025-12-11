package com.example.integradorapodometro.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class StepCounterManager(
    context: Context,
    private val onStep: () -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Usamos el acelerómetro
    private val accelSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Para evitar contar mil pasos por un solo movimiento
    private var lastStepTimeNs: Long = 0L

    fun start() {
        accelSensor?.let {
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

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Magnitud total de la aceleración
        val magnitude = sqrt(x * x + y * y + z * z)

        // Quitamos la gravedad aproximada (9.81)
        val accel = magnitude - SensorManager.GRAVITY_EARTH

        val now = System.nanoTime()

        // Umbral + tiempo mínimo entre pasos (~0.25s)
        if (accel > 2.0 && now - lastStepTimeNs > 250_000_000L) {
            lastStepTimeNs = now
            onStep()  // 🔹 aquí notificamos al ViewModel un paso real
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
}

