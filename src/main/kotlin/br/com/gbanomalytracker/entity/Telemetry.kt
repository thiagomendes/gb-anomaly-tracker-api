package br.com.gbanomalytracker.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Telemetry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val metricName: String,
    val value: Double,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
