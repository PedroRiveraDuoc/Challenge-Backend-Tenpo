# Challenge Backend Tenpo

API REST para cálculos con porcentaje dinámico.

## Requisitos

- Java 21
- Maven
- Docker y Docker Compose

## Estructura del Proyecto

```
src/main/java/com/example/challenge/
├── ChallengeBackendApplication.java
├── config/
│   ├── CacheConfig.java
│   ├── AsyncConfig.java
│   └── OpenApiConfig.java
├── controller/
│   ├── CalculationController.java
│   └── LogController.java
├── service/
│   ├── CalculationService.java
│   ├── PercentageClient.java
│   └── LogService.java
├── repository/
│   └── ApiCallLogRepository.java
├── model/
│   └── ApiCallLog.java
├── dto/
│   ├── CalculationRequest.java
│   ├── CalculationResponse.java
│   └── ApiCallLogDto.java
└── exception/
    ├── PercentageUnavailableException.java
    ├── RestExceptionHandler.java
    └── ErrorResponse.java
```

## Características

- Cálculo dinámico con porcentaje externo
- Caché de porcentajes (30 minutos)
- Logging asíncrono de llamadas API
- Documentación OpenAPI/Swagger
- Validación de datos
- Manejo centralizado de excepciones
- Contenedorización con Docker

## Ejecución Local

### Opción 1: Ejecución con Docker Compose (Opción recomendada)

1. Compilar el proyecto:
```bash
mvn clean package
```

2. Ejecutar con Docker Compose:
```bash
docker-compose up
```

La aplicación estará disponible en:
- API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
- Base de datos PostgreSQL: localhost:5433
- Mock del servicio de porcentaje: localhost:1080

### Opción 2: Ejecución sin Docker

1. Configurar PostgreSQL:
   - Crear base de datos: `challenge`
   - Usuario: `postgres`
   - Contraseña: `postgres`
   - Puerto: `5432`

2. Configurar el servicio de porcentaje:
   - URL: `http://localhost:1080/percentage`
   - O modificar `application.yml` para usar otro servicio

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

## Endpoints

### POST /api/v1/calculate
Calcula el resultado de dos números con un porcentaje dinámico.

Request:
```json
{
    "num1": 10.0,
    "num2": 20.0
}
```

Response:
```json
{
    "result": 33.0,
    "percentage": 0.10
}
```

### GET /api/v1/logs
Obtiene el historial de llamadas a la API.

Parámetros:
- page: número de página (default: 0)
- size: tamaño de página (default: 10)

Response:
```json
{
    "content": [
        {
            "id": "550e8400-e29b-41d4-a716-446655440000",
            "timestamp": "2024-03-14T10:30:00",
            "endpoint": "/calculate",
            "parametersJson": "{\"num1\":10.0,\"num2\":20.0}",
            "responseJson": "{\"result\":33.0,\"percentage\":0.10}",
            "status": "SUCCESS"
        }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
}
```

## Configuración

### Variables de Entorno
- `SPRING_PROFILES_ACTIVE`: perfil activo (dev, prod)
- `SPRING_DATASOURCE_URL`: URL de la base de datos
- `SPRING_DATASOURCE_USERNAME`: usuario de la base de datos
- `SPRING_DATASOURCE_PASSWORD`: contraseña de la base de datos
- `PERCENTAGE_SERVICE_URL`: URL del servicio de porcentaje

### Caché
- Tipo: Caffeine
- Tamaño máximo: 1000 entradas
- Tiempo de expiración: 30 minutos

### Logging
- Nivel root: INFO
- Nivel aplicación: DEBUG
- Nivel caché: TRACE
- Nivel web: INFO

### SonarCloud
- Coverage
- 83.7%
![image](https://github.com/user-attachments/assets/7ba3fca9-10f4-41fd-bffa-fd355ca61ed3)
