# Club Deportivo - Arquitectura de Microservicios (Evaluacion 3)

Backend del proyecto semestral **Club Deportivo**, desarrollado con Spring Boot bajo arquitectura de microservicios.

## Integrantes

| Nombre | Rol |
|--------|-----|
| _(completar con los nombres del equipo)_ | Desarrollo |

## Contexto del dominio

Sistema para gestionar un club deportivo: socios, cuotas, actividades, reservas y mas. Cada bounded context se implementa como microservicio independiente con comunicacion REST.

## Microservicios

| Servicio | Puerto | Descripcion | Estado |
|----------|--------|-------------|--------|
| `ms-socios` | 8081 | Gestion de socios del club | Implementado |
| `api-gateway` | 8080 | Enrutamiento centralizado | Pendiente |
| _(otros 8 MS)_ | — | Por definir con el equipo | Pendiente |

## ms-socios - Endpoints

Base URL local: `http://localhost:8081`

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/api/socios` | Registrar socio |
| GET | `/api/socios` | Listar todos |
| GET | `/api/socios/activos` | Listar activos |
| GET | `/api/socios/estadisticas/activos` | Contar activos |
| GET | `/api/socios/rut/{rut}` | Buscar por RUT |
| GET | `/api/socios/{id}` | Buscar por ID |
| PUT | `/api/socios/{id}` | Actualizar socio |
| PUT | `/api/socios/{id}/reactivar` | Reactivar socio |
| PATCH | `/api/socios/{id}/email` | Actualizar email |
| DELETE | `/api/socios/{id}` | Desactivar socio (soft delete) |

## Documentacion Swagger

- **UI local:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8081/api-docs](http://localhost:8081/api-docs)

## Ejecucion local

### Requisitos

- JDK 21
- Maven 3.9+
- MySQL (Laragon recomendado)

### Pasos

```bash
cd ms-socios
./mvnw spring-boot:run
```

Perfil activo por defecto: `dev` (MySQL local).

### Variables de entorno (perfil prod)

| Variable | Descripcion |
|----------|-------------|
| `MYSQL_URL` | URL JDBC de MySQL |
| `MYSQL_USER` | Usuario de BD |
| `MYSQL_PASSWORD` | Contrasena de BD |
| `SERVER_PORT` / `PORT` | Puerto del servicio (default: 8081) |

## Pruebas unitarias

```bash
cd ms-socios
./mvnw test
./mvnw verify   # incluye verificacion JaCoCo >= 80% en capa service
```

Reporte de cobertura: `ms-socios/target/site/jacoco/index.html`

## Docker

```bash
cd ms-socios
docker build -t ms-socios .
docker run -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_URL=jdbc:mysql://host.docker.internal:3306/ms_socios \
  -e MYSQL_USER=root \
  -e MYSQL_PASSWORD= \
  ms-socios
```

## API Gateway

_(se documentara en el Paso 2)_

## Herramientas colaborativas

- **GitHub:** _(enlace del repositorio)_
- **Trello:** _(enlace del tablero)_
