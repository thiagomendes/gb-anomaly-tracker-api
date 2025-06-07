package br.com.tmanomalytracker.repository

import br.com.tmanomalytracker.entity.Anomaly
import org.springframework.data.jpa.repository.JpaRepository

interface AnomalyRepository : JpaRepository<Anomaly, Long>
