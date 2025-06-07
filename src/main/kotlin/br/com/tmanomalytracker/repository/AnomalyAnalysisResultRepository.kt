package br.com.tmanomalytracker.repository

import br.com.tmanomalytracker.entity.AnomalyAnalysisResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnomalyAnalysisResultRepository : JpaRepository<AnomalyAnalysisResult, Long>
