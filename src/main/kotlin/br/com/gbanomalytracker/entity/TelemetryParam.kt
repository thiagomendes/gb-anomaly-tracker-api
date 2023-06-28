package br.com.gbanomalytracker.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
@Schema(description = "Entidade utilizada para guardar parametros adicionais da metrica enviada por telemetria")
data class TelemetryParam(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @field:Schema(
        description = "Chave identificadora do parâmetro",
        example = "ID do Produto",
    )
    var key: String,

    @field:Schema(
        description = "Valor do parâmetro",
        example = "12345",
    )
    var value: String,
)