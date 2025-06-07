package br.com.tmanomalytracker.scheduler

import br.com.tmanomalytracker.service.DetectorService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AnomalyDetectionScheduler(
    private val detectorService: DetectorService,
) {
    private val logger: Logger = LoggerFactory.getLogger(AnomalyDetectionScheduler::class.java)

    @Transactional
    @Scheduled(cron = "0 * * * * ?")
    fun scheduleCheckAnomalies() {
        val detectors = detectorService.getAllDetectors()

        detectors.forEach { detector ->
            detectorService.checkAnomalies(detector)
        }
    }
}
