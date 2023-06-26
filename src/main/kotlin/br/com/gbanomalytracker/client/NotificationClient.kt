package br.com.gbanomalytracker.client

interface NotificationClient {
    fun sendNotification(message: String)

    fun getChannel(): String
}