
# Desafio Itau Statistics API

## Overview

This project is a Spring Boot application that provides endpoints to manage and analyze transaction data. The API includes endpoints to create transactions, clear all transactions, and calculate statistics for transactions that occurred in the last 60 seconds.

## Prerequisites

- Java 21 or higher
- Gradle 8.13
- Docker (optional, for containerization)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yvesguilherme/challenges.git
cd desafio-itau-statistics
```

### Build the Application

```bash
./gradlew build
```

### Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080/v1`.

## API Endpoints

The API endpoints are documented using Swagger. You can access the Swagger UI to explore and test the endpoints.

- **Swagger UI:** `http://localhost:8080/v1/swagger.html`

## Configuration

The application can be configured using the `application.yml` file located in the `src/main/resources` directory. Key configurations include:

- `server.servlet.context-path`: Base path for the API endpoints
- `statistics.timeframe.seconds`: Timeframe in seconds for calculating statistics

## Docker

To run the application in a Docker container, follow these steps:

### Build Docker Image

```bash
docker build -t desafio-itau-statistics .
```

### Run Docker Container

```bash
docker run -p 8080:8080 desafio-itau-statistics
```

## Observability

The application includes endpoints for health checks and application info:

- **Health Check:** `GET /v1/actuator/health`
- **Application Info:** `GET /v1/actuator/info`

## Logging

Logging is configured in the `application.yml` file. Adjust the logging level as needed:

```yaml
logging:
  level:
    org.yvesguilherme.statistics: debug
```

## Error Handling

The application provides custom error handling to return specific error messages for different scenarios.

## License

This project is licensed under the MIT License.

---

<p  style="text-align: center;">Yves Guilherme 🚀</p>