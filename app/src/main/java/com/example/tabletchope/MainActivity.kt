package com.example.serialcommunication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tabletchope.R

class MainActivity : AppCompatActivity() {

    private lateinit var serialConnection: SerialConnection
    private lateinit var creditManager: CreditManager
    private lateinit var valveController: ValveController
    private lateinit var creditsTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var temperatureDisplay: TemperatureDisplay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inicializa as conexões
        serialConnection = SerialConnection()
        serialConnection.connect()
        creditManager = CreditManager(10) // Inicializa com 10 créditos
        valveController = ValveController(serialConnection, creditManager)
        temperatureDisplay = TemperatureDisplay() // Inicializa a conexão com o sensor de temperatura


        creditsTextView = findViewById(R.id.credits_text_view)
        temperatureTextView = findViewById(R.id.temperature_text_view)
        updateCreditsText()

        val buttonOpen: Button = findViewById(R.id.button_open)
        val buttonClose: Button = findViewById(R.id.button_close)
        val buttonGetTemperature: Button = findViewById(R.id.button_get_temperature)

        buttonOpen.setOnClickListener {
            valveController.openValve()
            updateCreditsText()
        }

        buttonClose.setOnClickListener {
            valveController.closeValve()
        }

        buttonGetTemperature.setOnClickListener {
            getTemperature()
        }
    }

    private fun updateCreditsText() {
        creditsTextView.text = "Credits: ${valveController.getCredits()}"
    }

    private fun getTemperature() {
        val temperature = temperatureDisplay.requestTemperature()
        temperatureTextView.text = "Temperatura: $temperature"
    }
    override fun onDestroy() {
        super.onDestroy()
        serialConnection.close()
        temperatureDisplay.close()
    }
}
