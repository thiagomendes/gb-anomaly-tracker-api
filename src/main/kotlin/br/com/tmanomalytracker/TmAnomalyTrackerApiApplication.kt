package br.com.tmanomalytracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TmAnomalyTrackerApiApplication

fun main(args: Array<String>) {
    runApplication<TmAnomalyTrackerApiApplication>(*args)
}
