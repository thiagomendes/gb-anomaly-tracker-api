package br.com.tmanomalytracker.service

import br.com.tmanomalytracker.entity.Telemetry
import br.com.tmanomalytracker.repository.TelemetryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TelemetryService(private val telemetryRepository: TelemetryRepository) {

    fun saveTelemetry(telemetry: Telemetry): Telemetry {
        return telemetryRepository.save(telemetry)
    }

    fun listMetrics(): List<Telemetry> {
        return telemetryRepository.findAll()
    }

    fun getTelemetryByMetricNameAndTimestampRange(
        metricName: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<Telemetry> {
        return telemetryRepository.findByMetricNameAndTimestampBetween(metricName, startTime, endTime)
    }
}
