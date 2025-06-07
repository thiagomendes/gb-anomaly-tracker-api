package br.com.tmanomalytracker.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun usersMicroserviceOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info().title("TM Anomaly Tracker API")
                    .description("Documentação da API do TM Anomaly Tracker")
                    .version("1.0"),
            )
    }
}
