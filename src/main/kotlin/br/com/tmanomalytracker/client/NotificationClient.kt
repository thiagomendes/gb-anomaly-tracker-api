package br.com.tmanomalytracker.client

interface NotificationClient {
    fun sendNotification(message: String)

    fun getChannel(): String
}