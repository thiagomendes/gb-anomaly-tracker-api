package br.com.gbanomalytracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class GbAnomalyTrackerApiApplication

fun main(args: Array<String>) {
    runApplication<GbAnomalyTrackerApiApplication>(*args)
}
