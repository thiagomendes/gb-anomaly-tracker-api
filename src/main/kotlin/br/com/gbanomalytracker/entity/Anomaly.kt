package br.com.gbanomalytracker.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
@Schema(description = "A entidade Anomaly representa uma anomalia detectada em uma métrica monitorada.")
data class Anomaly(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @field:Schema(
        description = "ID da Anomalia",
        example = "1",
    )
    var id: Long? = null,

    @field:Schema(
        description = "Valor detectado da Anomalia",
        example = "150.0",
    )
    var detectedValue: Double,

    @field:Schema(
        description = "Data e hora em que a Anomalia foi detectada",
        example = "2023-06-22T12:00:00",
    )
    val detectedTime: LocalDateTime,

    @field:Schema(
        description = "Variação detectada da Anomalia",
        example = "30.0",
    )
    var detectedVariation: Double,
)
