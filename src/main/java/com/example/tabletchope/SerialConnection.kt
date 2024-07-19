package com.example.serialcommunication

import android.hardware.usb.UsbManager
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.util.concurrent.Executors

class SerialConnection(private val usbManager: UsbManager) {

    private var serialPort: UsbSerialPort? = null

    fun findSerialPortDevice() {
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
        if (availableDrivers.isEmpty()) {
            return
        }

        val driver = availableDrivers[0]
        val connection = usbManager.openDevice(driver.device) ?: return

        serialPort = driver.ports[0]
        serialPort?.open(connection)
        serialPort?.setParameters(
            9600,
            UsbSerialPort.DATABITS_8,
            UsbSerialPort.STOPBITS_1,
            UsbSerialPort.PARITY_NONE
        )

        val usbIoManager = SerialInputOutputManager(serialPort, object : SerialInputOutputManager.Listener {
            override fun onNewData(data: ByteArray?) {
                // Handle incoming data if needed
            }

            override fun onRunError(e: Exception?) {
                // Handle errors if needed
            }
        })

        Executors.newSingleThreadExecutor().submit(usbIoManager)
    }

    fun sendCommand(command: String) {
        serialPort?.write(command.toByteArray(), 1000)
    }

    fun close() {
        serialPort?.close()
    }
}
