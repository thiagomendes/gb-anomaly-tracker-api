package br.com.tmanomalytracker.dto

import java.time.LocalDateTime

data class TelemetryAggregate(
    val timestamp: LocalDateTime,
    val value: Double
)
