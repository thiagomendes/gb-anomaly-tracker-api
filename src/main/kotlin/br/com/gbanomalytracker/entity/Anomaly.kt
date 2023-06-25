package br.com.gbanomalytracker.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Anomaly(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var detectedValue: Double,
    val detectedTime: LocalDateTime,
    var detectedVariation: Double,
)
