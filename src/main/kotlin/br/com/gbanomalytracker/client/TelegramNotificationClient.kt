package br.com.gbanomalytracker.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TelegramNotificationClient : NotificationClient {

    private val logger: Logger = LoggerFactory.getLogger(TelegramNotificationClient::class.java)

    private val botToken = "6230446354:AAFY69NgXn6svEFcrr5Tg8QNFtc43sGeiXQ"
    private val chatId = "-1001748653117"
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

        val response: ResponseEntity<String> = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
        // Tratamentos adicionais na resposta, se necessário
    }

    override fun getChannel() = "Telegram"
}

