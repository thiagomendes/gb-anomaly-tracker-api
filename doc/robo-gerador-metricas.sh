#!/bin/bash

BASE_URL="http://localhost:8080"  # Coloque a URL base da sua API aqui
METRIC_NAME=$1

# Função para enviar telemetria
sendTelemetry() {
  metricName=$1
  value=$2

  json="{ \"metricName\": \"$metricName\", \"value\": $value }"

  curl -X POST -H "Content-Type: application/json" -d "$json" "$BASE_URL/metrics"
}

# Função para gerar um valor aleatório entre 1 e 10
generateRandomValue() {
  value=$(shuf -i 1-10 -n 1)
  echo "$value"
}

# Exemplo de envio de telemetria com valor aleatório
sendTelemetry "$METRIC_NAME" $(generateRandomValue)

echo "Envio de telemetria concluído."

# Para rodar: watch -n 5 ./robo-gerador-metricas.sh "Volume de Vendas"