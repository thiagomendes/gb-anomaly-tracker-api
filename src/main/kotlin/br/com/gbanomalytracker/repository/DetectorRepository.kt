package br.com.gbanomalytracker.repository

import br.com.gbanomalytracker.entity.Detector
import org.springframework.data.jpa.repository.JpaRepository

interface DetectorRepository : JpaRepository<Detector, Long>
