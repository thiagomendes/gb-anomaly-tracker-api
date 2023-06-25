package br.com.gbanomalytracker.repository

import br.com.gbanomalytracker.entity.AnomalyAnalysisResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnomalyAnalysisResultRepository : JpaRepository<AnomalyAnalysisResult, Long>
