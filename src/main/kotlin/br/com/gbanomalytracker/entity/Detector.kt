package br.com.gbanomalytracker.entity

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
@Schema(description = "A entidade Detector representa um detector de anomalias, contendo informações sobre a métrica monitorada, as configurações de alerta, os dados de anomalias detectadas e o objetivo do detector.")
data class Detector(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @field:Schema(
        description = "Nome do detector",
        example = "Detector para monitoramento de queda no volume de vendas",
    )
    var detectorName: String,

    @field:Schema(
        description = "Nome da métrica que será monitorada",
        example = "Volume de Vendas",
    )
    var metricName: String,

    @field:Schema(
        description = "Variação em percentual a ser monitorada",
        example = "10",
    )
    var alertVariation: Double,

    @field:Schema(
        description = "Intervalo de monitoramento em minutos",
        example = "5",
    )
    var alertIntervalMinutes: Long,

    @field:Schema(
        description = "Mensagem que será apresentada quando uma anomalia for detectada",
        example = "Foi detectada uma variação de X% abaixo do volume de vendas padrão",
    )
    var alertMessage: String,

    @field:Schema(
        description = "Canais para recebimento do alerta de anomalia",
        example = "[Slack, Telegram]",
    )
    var alertChannel: List<String> = mutableListOf(),

    @field:Schema(
        description = "Direção de monitoramento para detecção das anomalias",
        example = "acima, abaixo, acima/abaixo",
    )
    var direction: String,

    @field:Schema(
        description = "Instruções em caso de anomalias detectadas",
        example = "Acionar o time A",
    )
    var action: String?,

    @field:Schema(
        description = "Objetivo do detector",
        example = "Monitorar e identificar quedas significativas no volume de vendas",
    )
    var objective: String?,

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var anomalies: MutableList<Anomaly> = ArrayList(),

    @field:Schema(
        description = "Método de agregação para métricas",
        example = "avg, count, sum",
    )
    var aggregationMethod: String,

    @OneToMany(cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
    var params: MutableList<DetectorParam>? = ArrayList(),
)