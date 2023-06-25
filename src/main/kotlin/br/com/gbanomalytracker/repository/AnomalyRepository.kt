package br.com.gbanomalytracker.repository

import br.com.gbanomalytracker.entity.Anomaly
import org.springframework.data.jpa.repository.JpaRepository

interface AnomalyRepository : JpaRepository<Anomaly, Long>
