package com.example.serialcommunication

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TemperatureDisplay(private val ipAddress: String, private val port: Int) {

    private var socket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    init {
        connect()
    }

    private fun connect() {
        try {
            socket = Socket(ipAddress, port)
            out = PrintWriter(socket?.getOutputStream(), true)
            `in` = BufferedReader(InputStreamReader(socket?.getInputStream()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun requestTemperature(): String {
        return try {
            out?.println("ler_sensor")
            val response = `in`?.readLine()
            response ?: "Sem resposta do servidor."
        } catch (e: Exception) {
            e.printStackTrace()
            "Erro ao solicitar a temperatura."
        }
    }

    fun close() {
        try {
            out?.close()
            `in`?.close()
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
