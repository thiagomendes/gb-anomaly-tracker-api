package br.com.gbanomalytracker.scheduler

import br.com.gbanomalytracker.client.NotificationClient
import br.com.gbanomalytracker.entity.Anomaly
import br.com.gbanomalytracker.entity.AnomalyAnalysisResult
import br.com.gbanomalytracker.entity.Detector
import br.com.gbanomalytracker.repository.AnomalyAnalysisResultRepository
import br.com.gbanomalytracker.service.DetectorService
import br.com.gbanomalytracker.service.TelemetryService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class AnomalyDetectionScheduler(
    private val detectorService: DetectorService,
    private val telemetryService: TelemetryService,
    private val notifiers: List<NotificationClient>,
    private val anomalyAnalysisResultRepository: AnomalyAnalysisResultRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(AnomalyDetectionScheduler::class.java)

    @Transactional
    @Scheduled(fixedDelay = 1 * 60 * 1000) // Executa a cada 1 minuto
    fun detectAnomalies() {
        logger.info("Executando detecção de anomalias...")

        val detectors = detectorService.getAllDetectors()
        detectors.forEach { detector ->
            logger.info("Processando detector: ID = ${detector.id}, Nome da métrica = ${detector.metricName}")

            val metricName = detector.metricName
            val alertIntervalMinutes = detector.alertIntervalMinutes

            val currentTime = LocalDateTime.now()
            val endTime = currentTime.minusMinutes(alertIntervalMinutes.toLong())
            val startTime = endTime.minusMinutes(alertIntervalMinutes.toLong())

            logger.info("Período de análise: de $startTime a $endTime")

            val telemetryList = telemetryService.getTelemetryByMetricNameAndTimestampRange(
                metricName,
                startTime,
                endTime,
            )

            if (telemetryList.isNotEmpty()) {
                logger.info("Número de telemetrias encontradas: ${telemetryList.size}")

                val firstSum = telemetryList.subList(0, telemetryList.size / 2).sumOf { it.value }
                val secondSum = telemetryList.subList(telemetryList.size / 2, telemetryList.size).sumOf { it.value }

                logger.info("Soma dos valores no primeiro intervalo: $firstSum")
                logger.info("Soma dos valores no segundo intervalo: $secondSum")

                val variation = calculateVariation(firstSum, secondSum)

                logger.info("Variação calculada: $variation")

                val anomalyDetected = isAnomaly(variation, detector.alertVariation, detector.direction)

                val analysisResult = AnomalyAnalysisResult(
                    metricName = detector.metricName,
                    timestamp = currentTime,
                    value = secondSum,
                    isAnomaly = anomalyDetected,
                    detector = detector,
                )

                anomalyAnalysisResultRepository.save(analysisResult)

                if (anomalyDetected) {
                    logger.info("Anomalia detectada!")
                    detector.id?.let { detectorId ->
                        val anomaly = Anomaly(
                            detectedValue = secondSum,
                            detectedVariation = variation,
                            detectedTime = currentTime,
                        )
                        detectorService.createAnomaly(detectorId, anomaly)

                        logger.info("Anomalia registrada no detector: ID = $detectorId")

                        sendNotification(detector)
                    }
                } else {
                    logger.info("Nenhuma anomalia detectada.")
                }
            } else {
                logger.info("Nenhuma telemetria encontrada para o detector.")
            }
        }

        logger.info("Detecção de anomalias concluída.")
    }

    private fun calculateVariation(firstValue: Double, secondValue: Double): Double {
        return ((secondValue - firstValue) / firstValue) * 100
    }

    private fun isAnomaly(variation: Double, alertVariation: Double, direction: String): Boolean {
        return when (direction) {
            "acima" -> variation > alertVariation
            "abaixo" -> variation < -alertVariation
            "acima/abaixo" -> variation > alertVariation || variation < -alertVariation
            else -> false
        }
    }

    fun sendNotification(detector: Detector) {
        notifiers.stream()
            .filter { detector.alertChannel.contains(it.getChannel()) }
            .forEach { it.sendNotification(detector.alertMessage) }
    }

}
