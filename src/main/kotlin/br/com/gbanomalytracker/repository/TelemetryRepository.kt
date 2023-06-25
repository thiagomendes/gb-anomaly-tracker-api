package br.com.gbanomalytracker.repository

import br.com.gbanomalytracker.entity.Telemetry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TelemetryRepository : JpaRepository<Telemetry, Long> {
    fun findByMetricNameAndTimestampBetween(
        metricName: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<Telemetry>
}
