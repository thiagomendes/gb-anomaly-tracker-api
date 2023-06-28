$baseUrl = "http://localhost:8080"  # Coloque a URL base da sua API aqui
$metricName = $args[0]
$params = $args[1]  # Novo argumento para parâmetros

# Função para enviar telemetria
function SendTelemetry($metricName, $value, $params) {
    $body = New-Object PSObject
    $body | Add-Member -type NoteProperty -name "metricName" -value $metricName
    $body | Add-Member -type NoteProperty -name "value" -value $value
    $body | Add-Member -type NoteProperty -name "params" -value $params
    $json = $body | ConvertTo-Json

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
    SendTelemetry -metricName $metricName -value $value -params $params  # Adicione os parâmetros aqui

    Write-Host "Envio de telemetria concluído."

    Start-Sleep -Seconds 5
}


# Para rodar:
# Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process -Force
# $params = @( @{"key"="produto"; "value"="iphone"} )
# .\doc\robo-gerador-metricas-com-parametros-windows.ps1 "Volume de Vendas" $params