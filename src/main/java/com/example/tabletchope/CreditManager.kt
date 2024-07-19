package com.example.serialcommunication

class CreditManager(private var credits: Int) {

    fun useCredit(): Boolean {
        return if (credits > 0) {
            credits--
            true
        } else {
            false
        }
    }

    fun addCredits(amount: Int) {
        credits += amount
    }

    fun getCredits(): Int {
        return credits
    }

    fun hasCredits(): Boolean {
        return credits > 0
    }
}
