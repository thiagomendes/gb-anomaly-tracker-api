package br.com.gbanomalytracker.entity

import jakarta.persistence.*

@Entity
data class Detector(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var metricName: String,
    var alertVariation: Double,
    var alertIntervalMinutes: Int,
    var alertMessage: String,
    var alertChannel: List<String> = mutableListOf(),
    var direction: String,
    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var anomalies: MutableList<Anomaly> = ArrayList(),
    @OneToMany(mappedBy = "detector")
    val anomalyAnalysisResults: List<AnomalyAnalysisResult> = mutableListOf(),
)
