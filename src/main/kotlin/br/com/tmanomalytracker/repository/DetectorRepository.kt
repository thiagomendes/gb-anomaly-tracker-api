package br.com.tmanomalytracker.repository

import br.com.tmanomalytracker.entity.Detector
import org.springframework.data.jpa.repository.JpaRepository

interface DetectorRepository : JpaRepository<Detector, Long>
