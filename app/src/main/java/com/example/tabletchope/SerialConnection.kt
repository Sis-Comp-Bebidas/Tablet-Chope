package com.example.serialcommunication

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

class SerialConnection {

    private var ipAddress: String? = null
    private var socket: Socket? = null

    init {
        ipAddress = getUsb0IpAddress()
    }

    private fun getUsb0IpAddress(): String? {
        try {
            val process = Runtime.getRuntime().exec("ip -4 addr show usb0")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line!!.trim().startsWith("inet ")) {
                    return line!!.split(" ")[1].split("/")[0]
                }
            }
        } catch (e: Exception) {
            Log.e("SerialConnection", "Erro ao obter IP da interface usb0", e)
        }
        return null
    }

    fun connect() {
        ipAddress?.let {
            try {
                socket = Socket(it, 22)
                Log.d("SerialConnection", "Conectado ao servidor TCP/IP $ipAddress:22 com sucesso.")
            } catch (e: Exception) {
                Log.e("SerialConnection", "Erro ao conectar ao servidor TCP/IP $ipAddress:22", e)
            }
        } ?: run {
            Log.e("SerialConnection", "Não foi possível determinar o IP da interface de rede.")
        }
    }

    fun sendCommand(command: String) {
        socket?.getOutputStream()?.write((command + "\n").toByteArray())
    }

    fun close() {
        socket?.close()
    }
}
