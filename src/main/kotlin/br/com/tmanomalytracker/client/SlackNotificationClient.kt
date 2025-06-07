package br.com.tmanomalytracker.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SlackNotificationClient : NotificationClient {

    private val logger: Logger = LoggerFactory.getLogger(SlackNotificationClient::class.java)

    override fun sendNotification(message: String) {
        logger.info("Slack notification client not yet implemented")
    }

    override fun getChannel() = "Slack"
}
