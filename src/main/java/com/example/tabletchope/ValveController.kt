package com.example.serialcommunication

class ValveController(private val serialConnection: SerialConnection, private val creditManager: CreditManager) {

    fun openValve() {
        if (creditManager.useCredit()) {
            serialConnection.sendCommand("OPEN")
        } else {
            closeValve()
        }
    }

    fun closeValve() {
        serialConnection.sendCommand("CLOSE")
    }

    fun getCredits(): Int {
        return creditManager.getCredits()
    }
}
