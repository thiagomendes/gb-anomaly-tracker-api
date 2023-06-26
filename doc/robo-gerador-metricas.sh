#!/bin/bash

baseUrl="http://localhost:8080"  # Coloque a URL base da sua API aqui
metricName="$1"

# Função para enviar telemetria
sendTelemetry() {
  local metricName="$1"
  local value="$2"

  local json="{ \"metricName\": \"$metricName\", \"value\": $value }"

  curl -X POST -H "Content-Type: application/json" -d "$json" "$baseUrl/metrics"
}

# Função para gerar um valor aleatório entre 1 e 10
generateRandomValue() {
  local value=$((RANDOM % 10 + 1))
  echo "$value"
}

# Loop infinito para enviar telemetria a cada 5 segundos
while true; do
  value=$(generateRandomValue)
  sendTelemetry "$metricName" "$value"

  echo "Envio de telemetria concluído."

  sleep 5
done

# Para rodar: ./doc/robo-gerador-metricas.sh "Volume de Vendas"