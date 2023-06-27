package br.com.gbanomalytracker.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
@Schema(description = "A entidade AnomalyAnalysisResult representa o resultado da análise de anomalias para uma métrica monitorada.")
data class AnomalyAnalysisResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Schema(
        description = "ID do Resultado da Análise da Anomalia",
        example = "1",
    )
    val id: Long? = null,

    @field:Schema(
        description = "Data e hora do Resultado da Análise da Anomalia",
        example = "2023-06-22T12:00:00",
    )
    val timestamp: LocalDateTime,

    @field:Schema(
        description = "Valor da Métrica",
        example = "150.0",
    )
    val value: Double,

    @field:Schema(
        description = "Indica se o resultado da análise é uma anomalia",
        example = "true",
    )
    val isAnomaly: Boolean,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detector_id")
    val detector: Detector,
)
