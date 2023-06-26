$baseUrl = "http://localhost:8080"  # Coloque a URL base da sua API aqui
$metricName = $args[0]

# Função para enviar telemetria
function SendTelemetry($metricName, $value) {
    $json = "{ ""metricName"": ""$metricName"", ""value"": $value }"
    Invoke-RestMethod -Method Post -Uri "$baseUrl/metrics" -ContentType "application/json" -Body $json
}

# Função para gerar um valor aleatório entre 1 e 10
function GenerateRandomValue() {
    $random = Get-Random -Minimum 1 -Maximum 11
    return $random
}

# Loop infinito para enviar telemetria a cada 5 segundos
while ($true) {
    $value = GenerateRandomValue
    SendTelemetry -metricName $metricName -value $value

    Write-Host "Envio de telemetria concluído."

    Start-Sleep -Seconds 5
}

# Para rodar:
# Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
# .\robo-gerador-metricas-windows.ps1 "Volume de Vendas"