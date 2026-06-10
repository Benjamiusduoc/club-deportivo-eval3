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
| `ms-cuotas` | 8082 | Gestion de cuotas y pagos | Implementado |
| `ms-actividades` | 8083 | Catalogo de actividades y deportes | Implementado |
| `ms-instructores` | 8084 | Gestion de instructores | Implementado |
| `ms-configuracion` | 8085 | Parametros del club | Implementado |
| `ms-inventario` | 8086 | Inventario de implementos | Implementado |
| `ms-reservas` | 8087 | Reserva de actividades | Implementado |
| `ms-asistencia` | 8088 | Registro de asistencia | Implementado |
| `ms-notificaciones` | 8089 | Notificaciones a socios | Implementado |
| `ms-reportes` | 8090 | Reportes y estadisticas | Implementado |
| `api-gateway` | 8080 | Enrutamiento centralizado | Implementado |

## API Gateway - Rutas principales

**Punto de entrada recomendado:** `http://localhost:8080`

| Ruta en Gateway | Microservicio destino | Descripcion |
|-----------------|----------------------|-------------|
| `/api/socios/**` | `ms-socios:8081` | Operaciones de socios |
| `/api/cuotas/**` | `ms-cuotas:8082` | Gestion de cuotas y pagos |
| `/api/actividades/**` | `ms-actividades:8083` | Catalogo de actividades |
| `/api/instructores/**` | `ms-instructores:8084` | Gestion de instructores |
| `/api/configuracion/**` | `ms-configuracion:8085` | Parametros del club |
| `/api/inventario/**` | `ms-inventario:8086` | Inventario de implementos |
| `/api/reservas/**` | `ms-reservas:8087` | Reserva de actividades |
| `/api/asistencia/**` | `ms-asistencia:8088` | Registro de asistencia |
| `/api/notificaciones/**` | `ms-notificaciones:8089` | Notificaciones |
| `/api/reportes/**` | `ms-reportes:8090` | Reportes y estadisticas |

**Health check del Gateway:** `http://localhost:8080/actuator/health`

## ms-socios - Endpoints

Base URL directa (sin Gateway): `http://localhost:8081`  
Base URL via Gateway: `http://localhost:8080`

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

## ms-cuotas - Endpoints

Base URL directa (sin Gateway): `http://localhost:8082`
Base URL via Gateway: `http://localhost:8080`

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/api/cuotas` | Crear cuota |
| GET | `/api/cuotas` | Listar todas |
| GET | `/api/cuotas/{id}` | Buscar por ID |
| GET | `/api/cuotas/socio/{idSocio}` | Listar cuotas de un socio |
| GET | `/api/cuotas/estado/{estado}` | Filtrar por estado (PENDIENTE/PAGADA/VENCIDA) |
| POST | `/api/cuotas/{id}/pagar` | Pagar cuota |

## Documentacion Swagger

Cada microservicio expone su propia UI de Swagger en `{base-url}/swagger-ui.html`:

| Microservicio | URL Swagger |
|---------------|-------------|
| ms-socios | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| ms-cuotas | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| ms-actividades | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |
| ms-instructores | [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html) |
| ms-configuracion | [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html) |
| ms-inventario | [http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html) |
| ms-reservas | [http://localhost:8087/swagger-ui.html](http://localhost:8087/swagger-ui.html) |
| ms-asistencia | [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html) |
| ms-notificaciones | [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html) |
| ms-reportes | [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html) |

## Ejecucion local

### Requisitos

- JDK 21
- Maven 3.9+
- MySQL (Laragon recomendado)

### Ejecucion local (desde IDE / terminal)

Cada microservicio se ejecuta en su propia terminal:

```bash
cd ms-socios    && ./mvnw spring-boot:run   # Puerto 8081
cd ms-cuotas    && ./mvnw spring-boot:run   # Puerto 8082
cd ms-actividades  && ./mvnw spring-boot:run  # Puerto 8083
cd ms-instructores && ./mvnw spring-boot:run  # Puerto 8084
cd ms-configuracion && ./mvnw spring-boot:run # Puerto 8085
cd ms-inventario && ./mvnw spring-boot:run  # Puerto 8086
cd ms-reservas  && ./mvnw spring-boot:run   # Puerto 8087
cd ms-asistencia && ./mvnw spring-boot:run  # Puerto 8088
cd ms-notificaciones && ./mvnw spring-boot:run # Puerto 8089
cd ms-reportes  && ./mvnw spring-boot:run   # Puerto 8090
cd api-gateway  && ./mvnw spring-boot:run   # Puerto 8080
```

Perfil activo por defecto: `dev` (MySQL local).  
Las peticiones del cliente deben ir al Gateway (`:8080`), no directo al microservicio.

### Variables de entorno (perfil prod)

**Gateway:**
| Variable | Descripcion |
|----------|-------------|
| `PORT` | Puerto del Gateway (default: 8080) |
| `MS_SOCIOS_URI` | URL de ms-socios (default: `http://localhost:8081`) |
| `MS_CUOTAS_URI` | URL de ms-cuotas (default: `http://localhost:8082`) |
| `MS_ACTIVIDADES_URI` | URL de ms-actividades (default: `http://localhost:8083`) |
| `MS_INSTRUCTORES_URI` | URL de ms-instructores (default: `http://localhost:8084`) |
| `MS_CONFIGURACION_URI` | URL de ms-configuracion (default: `http://localhost:8085`) |
| `MS_INVENTARIO_URI` | URL de ms-inventario (default: `http://localhost:8086`) |
| `MS_RESERVAS_URI` | URL de ms-reservas (default: `http://localhost:8087`) |
| `MS_ASISTENCIA_URI` | URL de ms-asistencia (default: `http://localhost:8088`) |
| `MS_NOTIFICACIONES_URI` | URL de ms-notificaciones (default: `http://localhost:8089`) |
| `MS_REPORTES_URI` | URL de ms-reportes (default: `http://localhost:8090`) |

**Por microservicio:**
| Variable | Descripcion |
|----------|-------------|
| `MYSQL_URL` | URL JDBC de MySQL |
| `MYSQL_USER` | Usuario de BD |
| `MYSQL_PASSWORD` | Contrasena de BD |
| `SERVER_PORT` | Puerto del servicio |
| `MS_SOCIOS_URL` | URL de ms-socios para WebClient (ms-cuotas, ms-reservas, ms-asistencia, ms-notificaciones, ms-reportes) |
| `MS_ACTIVIDADES_URL` | URL de ms-actividades para WebClient (ms-reservas) |
| `MS_INSTRUCTORES_URL` | URL de ms-instructores para WebClient (ms-reservas) |
| `MS_RESERVAS_URL` | URL de ms-reservas para WebClient (ms-asistencia) |
| `MS_CUOTAS_URL` | URL de ms-cuotas para WebClient (ms-reportes) |

## Pruebas unitarias

Cada microservicio incluye tests con JUnit + Mockito + JaCoCo (cobertura >= 80% en capa service):

```bash
cd ms-socios     && ./mvnw verify
cd ms-cuotas     && ./mvnw verify
cd ms-actividades   && ./mvnw verify
cd ms-instructores  && ./mvnw verify
cd ms-configuracion && ./mvnw verify
cd ms-inventario    && ./mvnw verify
cd ms-reservas      && ./mvnw verify
cd ms-asistencia    && ./mvnw verify
cd ms-notificaciones && ./mvnw verify
cd ms-reportes      && ./mvnw verify
```

Reportes de cobertura en `{ms}/target/site/jacoco/index.html`

## Docker

### Todo el stack con docker-compose (recomendado)

```bash
docker-compose up --build
```

Levanta: MySQL + todos los microservicios + API Gateway.  
Probar: `http://localhost:8080/api/socios` o `http://localhost:8080/api/cuotas`

### Solo ms-socios

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

### Solo ms-cuotas

```bash
cd ms-cuotas
docker build -t ms-cuotas .
docker run -p 8082:8082 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_URL=jdbc:mysql://host.docker.internal:3306/ms_cuotas \
  -e MYSQL_USER=root \
  -e MYSQL_PASSWORD= \
  -e MS_SOCIOS_URL=http://host.docker.internal:8081 \
  ms-cuotas
```

### Solo api-gateway

```bash
cd api-gateway
docker build -t api-gateway .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MS_SOCIOS_URI=http://host.docker.internal:8081 \
  -e MS_CUOTAS_URI=http://host.docker.internal:8082 \
  api-gateway
```

## Herramientas colaborativas

- **GitHub:** https://github.com/Benjamiusduoc/club-deportivo-eval3
- **Trello:** _(enlace del tablero)_
