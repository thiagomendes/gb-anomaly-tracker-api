package br.com.gbanomalytracker.controller

import br.com.gbanomalytracker.dto.TelemetryAggregate
import br.com.gbanomalytracker.entity.Anomaly
import br.com.gbanomalytracker.entity.Detector
import br.com.gbanomalytracker.service.DetectorService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/detectors")
class DetectorController(private val detectorService: DetectorService) {

    @PostMapping
    fun createDetector(@RequestBody detector: Detector) = detectorService.createDetector(detector)

    @GetMapping
    fun getAllDetectors() = detectorService.getAllDetectors()

    @GetMapping("/{id}")
    fun getDetectorById(@PathVariable id: Long) = detectorService.getDetectorById(id)

    @PutMapping("/{id}")
    fun updateDetector(@PathVariable id: Long, @RequestBody detector: Detector) =
        detectorService.updateDetector(id, detector)

    @DeleteMapping("/{id}")
    fun deleteDetector(@PathVariable id: Long) = detectorService.deleteDetector(id)

    @PostMapping("/{id}/anomalies")
    fun createAnomaly(@PathVariable id: Long, @RequestBody anomaly: Anomaly) =
        detectorService.createAnomaly(id, anomaly)

    @DeleteMapping("/{id}/anomalies/{anomalyId}")
    fun deleteAnomaly(@PathVariable id: Long, @PathVariable anomalyId: Long) =
        detectorService.deleteAnomaly(id, anomalyId)

    @GetMapping("/{detectorId}/telemetry")
    fun getAggregatedTelemetry(@PathVariable detectorId: Long): List<TelemetryAggregate> {
        return detectorService.getAggregatedTelemetry(detectorId)
    }
}
