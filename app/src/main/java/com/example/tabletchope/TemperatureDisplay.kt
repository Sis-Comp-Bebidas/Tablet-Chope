package com.example.serialcommunication

import android.os.Handler
import android.os.Looper
import android.widget.TextView

class TemperatureDisplay(
        private val serialConnection: SerialConnection,
        private val temperatureTextView: TextView
) {
    private val handler = Handler(Looper.getMainLooper())
    private var isUpdating = false

    private val updateInterval = 5000L // Intervalo de 5 segundos para as atualizações

    // Método para iniciar as atualizações automáticas da temperatura
    fun startTemperatureUpdates() {
        isUpdating = true
        handler.post(updateRunnable)
    }

    // Runnable que será executado periodicamente para solicitar e atualizar a temperatura
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isUpdating) {
                val temperature = requestTemperature()
                temperatureTextView.text = "Temperatura: $temperature"
                handler.postDelayed(this, updateInterval)
            }
        }
    }

    // Método para interromper as atualizações automáticas da temperatura
    fun stopTemperatureUpdates() {
        isUpdating = false
        handler.removeCallbacks(updateRunnable)
    }

    // Método para solicitar a temperatura da Raspberry Pi via TCP
    fun requestTemperature(): String {
        serialConnection.sendCommand("ler_sensor")
        return serialConnection.receiveResponse()
    }
}

