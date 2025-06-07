# TM Anomaly Tracker API

TM Anomaly Tracker API é uma aplicação simples escrita em Kotlin com Spring Boot para registrar métricas e detectar anomalias automaticamente. O projeto surgiu em um hackathon e serve como base para experimentações futuras.

## Tecnologias

- Kotlin / Spring Boot 3
- PostgreSQL
- Agendamentos com Spring Scheduler
- Documentação via OpenAPI (Swagger)

## Configuração do banco

Por padrão a aplicação espera uma instância local do PostgreSQL com as seguintes credenciais (veja `src/main/resources/application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tm-anomaly-tracker-db
spring.datasource.username=tm-anomaly-tracker-db-user
spring.datasource.password=tm-anomaly-tracker-db-pass
```

É possível subir um banco local rapidamente utilizando Docker:

```bash
docker run --name tm-anomaly-tracker-postgres -e POSTGRES_USER=tm-anomaly-tracker-db-user -e POSTGRES_PASSWORD=tm-anomaly-tracker-db-pass -e POSTGRES_DB=tm-anomaly-tracker-db -p 5432:5432 -d postgres
```

## Build e execução

É necessário ter o JDK 17 e o Maven instalados. Para rodar a aplicação em modo de desenvolvimento execute:

```bash
mvn spring-boot:run
```

A documentação interativa da API ficará disponível em `http://localhost:8080/swagger-ui.html`.

## Executando com Docker

Caso prefira utilizar contêineres, siga os passos abaixo para subir a aplicação e o banco de dados utilizando **Docker Compose**:

1. Certifique-se de ter o [Docker](https://docs.docker.com/get-docker/) e o [Docker Compose](https://docs.docker.com/compose/install/) instalados.
2. Clone este repositório e, no diretório raiz, execute o comando abaixo para construir a imagem e iniciar os serviços:

   ```bash
   docker compose up --build -d
   ```

3. Aguarde até que a aplicação esteja pronta. Você pode acompanhar os logs com:

   ```bash
   docker compose logs -f app
   ```

4. A API ficará disponível em `http://localhost:8080` e a documentação em `http://localhost:8080/swagger-ui.html`.
5. Para parar e remover os contêineres, execute:

   ```bash
   docker compose down
   ```

## Endpoints principais

- `POST /metrics` – registra uma nova métrica de telemetria.
- `GET  /metrics` – lista todas as métricas registradas.
- `POST /detectors` – cria um detector de anomalias.
- `GET  /detectors/{id}` – recupera um detector específico.
- `GET  /detectors/{id}/telemetry` – retorna métricas agregadas para o detector.

Um agendador (`AnomalyDetectionScheduler`) roda a cada minuto verificando se há anomalias nas métricas monitoradas.

## Utilitários

O diretório `doc/` contém alguns exemplos úteis, como um script simples para enviar métricas continuamente:

```bash
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
# Função para gerar um valor aleatório
generateRandomValue() {
  local value=$((RANDOM % 30 + 1))
  echo "$value"
}
while true; do
  value=$(generateRandomValue)
  sendTelemetry "$metricName" "$value"
  echo "Envio de telemetria concluído."
  sleep 2
done
```

Há também um arquivo `TMAnomalyTrackerCollection.json` com uma coleção de requisições para o Postman e uma página `simulador-metricas.html` para testes rápidos via navegador.

## Contribuição

Sinta-se à vontade para abrir issues ou pull requests. Este projeto ainda está em fase inicial e novas funcionalidades são bem-vindas.

