package br.com.gbanomalytracker.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Schema(description = "A entidade Anomaly representa uma anomalia detectada pelo detector.")
data class Anomaly(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:Schema(
        description = "Detector que detectou a anomalia",
        example = "1",
    )
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detector_id")
    var detector: Detector,

    @field:Schema(
        description = "Data e hora em que a anomalia foi detectada",
        example = "2023-06-27T12:00:00",
    )
    var timestamp: LocalDateTime = LocalDateTime.now(),

    )

