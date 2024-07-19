package com.example.serialcommunication

import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.tabletchope.R

class MainActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var serialConnection: SerialConnection
    private lateinit var creditManager: CreditManager
    private lateinit var valveController: ValveController
    private lateinit var creditsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbManager = getSystemService(USB_SERVICE) as UsbManager
        serialConnection = SerialConnection(usbManager)
        creditManager = CreditManager(10) // Inicializa com 10 créditos
        valveController = ValveController(serialConnection, creditManager)

        creditsTextView = findViewById(R.id.credits_text_view)
        updateCreditsText()

        val buttonOpen: Button = findViewById(R.id.button_open)
        val buttonClose: Button = findViewById(R.id.button_close)

        buttonOpen.setOnClickListener {
            valveController.openValve()
            updateCreditsText()
        }

        buttonClose.setOnClickListener {
            valveController.closeValve()
        }

        serialConnection.findSerialPortDevice()
    }

    private fun updateCreditsText() {
        creditsTextView.text = "Credits: ${valveController.getCredits()}"
    }

    override fun onDestroy() {
        super.onDestroy()
        serialConnection.close()
    }
}
