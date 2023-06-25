package br.com.gbanomalytracker.service

import br.com.gbanomalytracker.entity.Anomaly
import br.com.gbanomalytracker.entity.Detector
import br.com.gbanomalytracker.repository.AnomalyRepository
import br.com.gbanomalytracker.repository.DetectorRepository
import org.springframework.stereotype.Service

@Service
class DetectorService(
    private val detectorRepository: DetectorRepository,
    private val anomalyRepository: AnomalyRepository,
) {

    fun createDetector(detector: Detector): Detector {
        return detectorRepository.save(detector)
    }

    fun getAllDetectors(): List<Detector> {
        return detectorRepository.findAll()
    }

    fun getDetectorById(id: Long): Detector {
        return detectorRepository.findById(id).orElseThrow { NoSuchElementException("Detector with ID $id not found") }
    }

    fun updateDetector(id: Long, updatedDetector: Detector): Detector {
        val detector = getDetectorById(id)
        detector.metricName = updatedDetector.metricName
        detector.alertVariation = updatedDetector.alertVariation
        detector.alertIntervalMinutes = updatedDetector.alertIntervalMinutes
        detector.alertMessage = updatedDetector.alertMessage
        detector.alertChannel = updatedDetector.alertChannel
        return detectorRepository.save(detector)
    }

    fun deleteDetector(id: Long) {
        detectorRepository.deleteById(id)
    }

    fun createAnomaly(detectorId: Long, anomaly: Anomaly): Anomaly {
        val detector = getDetectorById(detectorId)
        val savedAnomaly = anomalyRepository.save(anomaly)
        detector.anomalies.add(savedAnomaly)
        detectorRepository.save(detector)
        return savedAnomaly
    }

    fun deleteAnomaly(detectorId: Long, anomalyId: Long) {
        val detector = getDetectorById(detectorId)
        val anomaly = anomalyRepository.findById(anomalyId)
            .orElseThrow { NoSuchElementException("Anomaly with ID $anomalyId not found") }
        detector.anomalies.removeIf { it.id == anomaly.id }
        detectorRepository.save(detector)
    }
}
