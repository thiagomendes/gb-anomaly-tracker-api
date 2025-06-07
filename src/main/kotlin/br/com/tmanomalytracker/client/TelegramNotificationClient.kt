package br.com.tmanomalytracker.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TelegramNotificationClient : NotificationClient {

    private val logger: Logger = LoggerFactory.getLogger(TelegramNotificationClient::class.java)

    private val botToken = "..."
    private val chatId = "-..."
    private val url = "https://api.telegram.org/bot$botToken/sendMessage"

    private val restTemplate = RestTemplate()

    override fun sendNotification(message: String) {
        logger.info("Sending telegram notification ")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestBody = """
            {
                "chat_id": "$chatId",
                "text": "$message",
                "parse_mode": "Markdown"
            }
        """.trimIndent()

        val entity = HttpEntity(requestBody, headers)

        restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
    }

    override fun getChannel() = "Telegram"
}
