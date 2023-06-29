#!/bin/bash

baseUrl="http://localhost:8080"  # Coloque a URL base da sua API aqui
metricName="$1"

#!/bin/bash

baseUrl="http://localhost:8080"  # Coloque a URL base da sua API aqui
metricName="$1"
params="$2"  # Novo argumento para parâmetros

# Função para enviar telemetria
sendTelemetry() {
  local metricName="$1"
  local value="$2"
  local params="$3"  # Novo parâmetro para params

  # Adicione os parâmetros ao JSON
  local json="{ \"metricName\": \"$metricName\", \"value\": $value, \"params\": $params }"

  curl -X POST -H "Content-Type: application/json" -d "$json" "$baseUrl/metrics"
}

# Função para gerar um valor aleatório entre 1 e 10
generateRandomValue() {
  local value=100
  echo "$value"
}

# Loop infinito para enviar telemetria a cada 5 segundos
while true; do
  value=$(generateRandomValue)
  sendTelemetry "$metricName" "$value" "$params"  # Adicione os parâmetros aqui

  echo "Envio de telemetria concluído."

  sleep 2
done

# Para rodar: ./doc/robo-gerador-metricas-com-parametros.sh "Volume de Vendas" '[{"key": "produto", "value": "iphone"}]'