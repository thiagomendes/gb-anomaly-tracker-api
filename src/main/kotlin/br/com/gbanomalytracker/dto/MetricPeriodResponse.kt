package br.com.gbanomalytracker.dto

import java.time.LocalDateTime

data class MetricPeriodResponse(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val valueSum: Double,
)
