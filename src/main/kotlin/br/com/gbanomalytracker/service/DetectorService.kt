package br.com.gbanomalytracker.service

import br.com.gbanomalytracker.client.NotificationClient
import br.com.gbanomalytracker.dto.TelemetryAggregate
import br.com.gbanomalytracker.entity.Anomaly
import br.com.gbanomalytracker.entity.Detector
import br.com.gbanomalytracker.entity.DetectorParam
import br.com.gbanomalytracker.entity.TelemetryParam
import br.com.gbanomalytracker.repository.AnomalyRepository
import br.com.gbanomalytracker.repository.DetectorRepository
import br.com.gbanomalytracker.repository.TelemetryRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class DetectorService(
    private val detectorRepository: DetectorRepository,
    private val anomalyRepository: AnomalyRepository,
    private val telemetryRepository: TelemetryRepository,
    private val notifiers: List<NotificationClient>,
) {

    private val logger = LoggerFactory.getLogger(DetectorService::class.java)

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
        anomalyRepository.delete(anomaly)
        detectorRepository.save(detector)
    }

    fun checkAnomalies(detector: Detector) {
        logger.info("Verificando anomalias para o detector: ${detector.detectorName}")

        val currentTime = LocalDateTime.now()
        val startTimeCurrentInterval = currentTime.minusMinutes(detector.alertIntervalMinutes.toLong())
        val startTimePreviousInterval = startTimeCurrentInterval.minusMinutes(detector.alertIntervalMinutes.toLong())

        val inputParams: List<TelemetryParam> = detector.params?.map { it.toTelemetryParam() } ?: emptyList()

        val telemetriesCurrentInterval =
            telemetryRepository.findLastMinutes(detector.metricName, startTimeCurrentInterval)
                .filter { telemetry ->
                    inputParams.all { inputParam ->
                        telemetry.params?.any { param ->
                            param.key == inputParam.key && param.value == inputParam.value
                        } ?: false
                    }
                }

        val telemetriesPreviousInterval = telemetryRepository.findLastMinutes(
            detector.metricName,
            startTimePreviousInterval,
            startTimeCurrentInterval,
        ).filter { telemetry ->
            inputParams.all { inputParam ->
                telemetry.params?.any { param ->
                    param.key == inputParam.key && param.value == inputParam.value
                } ?: false
            }
        }

        logger.info("Iniciando a verificação de anomalias para o detector: ${detector.id}")
        logger.info("Usando métricas de $startTimeCurrentInterval até agora para o intervalo atual.")
        logger.info("Usando métricas de $startTimePreviousInterval até $startTimeCurrentInterval para o intervalo anterior.")

        // De acordo com o aggregationMethod, calculamos a métrica relevante
        val metricCurrentInterval = when (detector.aggregationMethod) {
            "avg" -> if (telemetriesCurrentInterval.isNotEmpty()) {
                telemetriesCurrentInterval.map { it.value }
                    .average()
            } else {
                0.0
            }

            "sum" -> telemetriesCurrentInterval.sumOf { it.value }
            "count" -> telemetriesCurrentInterval.count().toDouble()
            else -> throw IllegalArgumentException("Método de agregação desconhecido: ${detector.aggregationMethod}")
        }

        val metricPreviousInterval = when (detector.aggregationMethod) {
            "avg" -> if (telemetriesPreviousInterval.isNotEmpty()) {
                telemetriesPreviousInterval.map { it.value }
                    .average()
            } else {
                0.0
            }

            "sum" -> telemetriesPreviousInterval.sumOf { it.value }
            "count" -> telemetriesPreviousInterval.count().toDouble()
            else -> throw IllegalArgumentException("Método de agregação desconhecido: ${detector.aggregationMethod}")
        }

        logger.info("Calculando ${detector.aggregationMethod} para o intervalo atual: $metricCurrentInterval")
        logger.info("Calculando ${detector.aggregationMethod} para o intervalo anterior: $metricPreviousInterval")

        // Calculamos a taxa de mudança
        val rateOfChange =
            if (metricPreviousInterval != 0.0) (metricCurrentInterval - metricPreviousInterval) / metricPreviousInterval else 0.0
        logger.info("Calculando a taxa de mudança: $rateOfChange")

        // Verificamos se a taxa de mudança é uma anomalia
        val isAnomaly = when (detector.direction) {
            "acima" -> rateOfChange > (detector.alertVariation / 100.0)
            "abaixo" -> rateOfChange < -(detector.alertVariation / 100.0)
            "acima/abaixo" -> Math.abs(rateOfChange) > (detector.alertVariation / 100.0)
            else -> false
        }

        logger.info("Is Anomaly: $isAnomaly")

        // Se for uma anomalia, criamos e retornamos uma nova entidade Anomaly
        if (isAnomaly) {
            detector.id?.let { detectorId ->
                val anomaly = Anomaly(
                    detector = detector,
                    timestamp = startTimeCurrentInterval,
                )
                createAnomaly(detectorId, anomaly)
                sendNotification(detector)
            }
        }
    }

    fun sendNotification(detector: Detector) {
        notifiers.stream()
            .filter { detector.alertChannel.contains(it.getChannel()) }
            .forEach { it.sendNotification(detector.alertMessage) }
    }

    fun getAggregatedTelemetry(detectorId: Long): List<TelemetryAggregate> {
        val detector = detectorRepository.findById(detectorId)
            .orElseThrow { NoSuchElementException("Detector with id: $detectorId not found") }

        val inputParams: List<TelemetryParam> = detector.params?.map { it.toTelemetryParam() } ?: emptyList()

        val telemetries = telemetryRepository.findAllByMetricName(detector.metricName, PageRequest.of(0, 1000))
            .filter { telemetry ->
                inputParams.all { inputParam ->
                    telemetry.params?.any { param ->
                        param.key == inputParam.key && param.value == inputParam.value
                    } ?: false
                }
            }

        return telemetries
            .groupBy { it.timestamp.truncatedTo(ChronoUnit.MINUTES) }
            .map { (timestamp, telemetryList) ->
                val value = when (detector.aggregationMethod) {
                    "avg" -> telemetryList.map { it.value }.average()
                    "sum" -> telemetryList.map { it.value }.sum()
                    "count" -> telemetryList.size.toDouble()
                    else -> throw IllegalArgumentException("Unknown aggregation method: ${detector.aggregationMethod}")
                }
                TelemetryAggregate(timestamp = timestamp, value = value)
            }
    }

    fun DetectorParam.toTelemetryParam(): TelemetryParam {
        return TelemetryParam(
            id = this.id,
            key = this.key,
            value = this.value,
        )
    }
}
