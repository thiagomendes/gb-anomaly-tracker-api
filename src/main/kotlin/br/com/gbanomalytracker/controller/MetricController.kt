package br.com.gbanomalytracker.controller

import br.com.gbanomalytracker.entity.Telemetry
import br.com.gbanomalytracker.service.TelemetryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/metrics")
class MetricController(
    private val telemetryService: TelemetryService,
) {

    @PostMapping
    fun registerMetricValue(
        @RequestBody telemetry: Telemetry,
    ) {
        telemetryService.saveTelemetry(telemetry)
    }

    @GetMapping
    fun listMetrics(): List<Telemetry> {
        return telemetryService.listMetrics()
    }
}
