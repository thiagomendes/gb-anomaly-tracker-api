package br.com.tmanomalytracker.repository

import br.com.tmanomalytracker.entity.Telemetry
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TelemetryRepository : JpaRepository<Telemetry, Long> {
    fun findByMetricNameAndTimestampBetween(
        metricName: String,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): List<Telemetry>

    @Query(
        "SELECT t FROM Telemetry t WHERE t.metricName = :metricName AND t.timestamp >= :startTime",
    )
    fun findLastMinutes(metricName: String, startTime: LocalDateTime): List<Telemetry>

    @Query(
        "SELECT t FROM Telemetry t WHERE t.metricName = :metricName AND t.timestamp >= :startTime AND t.timestamp < :endTime",
    )
    fun findLastMinutes(metricName: String, startTime: LocalDateTime, endTime: LocalDateTime): List<Telemetry>

    fun findAllByMetricName(metricName: String, pageable: Pageable): List<Telemetry>
}
