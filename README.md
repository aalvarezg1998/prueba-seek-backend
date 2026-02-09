# Task Manager API — SEEK Backend

API REST para gestión de tareas (Task Manager) del challenge SEEK. Backend en **Spring Boot 3.2** con **Java 21**, autenticación JWT y persistencia en PostgreSQL.

---

## Requisitos previos

- **Java 21** (JDK)
- **Maven 3.6+**
- **PostgreSQL** (para ejecución local; en tests se usa H2 en memoria)
- Opcional: **Docker** para ejecutar con contenedores

---

## Dependencias principales

| Dependencia | Uso |
|-------------|-----|
| Spring Boot 3.2.5 | Web, JPA, Security, Validation |
| PostgreSQL | Base de datos en desarrollo/producción |
| Flyway | Migraciones de base de datos |
| JJWT 0.12.5 | Tokens JWT |
| SpringDoc OpenAPI 2.5.0 | Documentación Swagger / OpenAPI |
| Lombok | Reducción de boilerplate |
| H2 | Base de datos en memoria para tests |

Todas las dependencias se gestionan con **Maven** (`pom.xml`).

---

## Cómo iniciar el proyecto

### 1. Clonar y entrar al directorio

```bash
git clone https://github.com/aalvarezg1998/prueba-seek-backend.git
cd prueba-seek-backend
```

### 2. Base de datos

Asegúrate de tener PostgreSQL corriendo y (opcional) una base de datos creada:

```bash
# Ejemplo: crear base de datos
createdb taskmanager
```

Las variables por defecto en `application.yml` son:

- Host: `localhost`, Puerto: `5432`
- Base de datos: `taskmanager`
- Usuario: `postgres`, Contraseña: `postgres`

Puedes sobrescribirlas con variables de entorno (ver [Configuración](#configuración)).

### 3. Ejecutar con Maven

```bash
# Compilar y ejecutar
./mvnw spring-boot:run

# En Windows
mvnw.cmd spring-boot:run
```

La API quedará disponible en **http://localhost:8081**.

### 4. Ejecutar con JAR

```bash
./mvnw clean package -DskipTests
java -jar target/aalvarezg-1.0.0.jar
```

### 5. Ejecutar con Docker

```bash
# Construir imagen
docker build -t seek-backend .

# Ejecutar (necesitas PostgreSQL accesible; ej. con docker-compose o variable DB_*)
docker run -p 8080:8080 -e DB_HOST=host.docker.internal -e DB_PORT=5432 -e DB_NAME=taskmanager -e DB_USERNAME=postgres -e DB_PASSWORD=postgres seek-backend
```

El Dockerfile expone el puerto **8080** (en `application.yml` por defecto la app usa 8081; en producción se suele usar `PORT`).

---

## Configuración

Variables de entorno relevantes:

| Variable | Descripción | Por defecto |
|----------|-------------|-------------|
| `DB_HOST` | Host de PostgreSQL | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |
| `DB_NAME` | Nombre de la base de datos | `taskmanager` |
| `DB_USERNAME` | Usuario de la BD | `postgres` |
| `DB_PASSWORD` | Contraseña de la BD | `postgres` |
| `JWT_SECRET` | Clave secreta para firmar JWT | (valor por defecto en `application.yml`) |
| `PORT` | Puerto del servidor (prod) | `5000` en prod |
| `CORS_ORIGINS` | Orígenes CORS permitidos | En dev: `http://localhost:3000`, `http://localhost:5173` |

Perfiles de Spring:

- **Por defecto**: `application.yml` (puerto 8081, DB local).
- **Test**: `application-test.yml` (H2 en memoria, sin Flyway).
- **Producción**: `application-prod.yml` (usa `PORT`, `DB_*`, `JWT_SECRET`, `CORS_ORIGINS`).

---

## Ejecutar tests

```bash
# Todos los tests
./mvnw test

# En Windows
mvnw.cmd test
```

Los tests usan el perfil **test** y base de datos **H2 en memoria**; no hace falta tener PostgreSQL levantado. Incluyen pruebas de use cases (Login, Register, CRUD de tareas) y de controladores REST (Auth, Task).

---

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal (Ports & Adapters)** / **Clean Architecture**:

- **Dominio**: modelos y excepciones de negocio sin dependencias de frameworks (`User`, `Task`, `TaskStatus`, excepciones de dominio).
- **Aplicación (casos de uso)**: puertos de entrada (input ports), DTOs de aplicación y servicios que orquestan la lógica.
- **Infraestructura**: adaptadores REST (controladores), persistencia JPA (repositorios, entidades), seguridad (JWT, BCrypt), y configuración (CORS, OpenAPI, manejo global de excepciones).

Estructura por módulos:

- **`authentication`**: registro, login y generación de token JWT.
- **`task`**: CRUD de tareas asociadas al usuario autenticado.
- **`shared`**: excepciones de dominio, CORS, OpenAPI, `GlobalExceptionHandler`.

Los **puertos de salida** (repositorios, hasher de contraseñas, proveedor de tokens) están definidos en dominio/aplicación e implementados en infraestructura. La API expone REST y documentación OpenAPI (Swagger).

---

## Documentación de la API (Swagger)

Con la aplicación en marcha:

- **Swagger UI**: http://localhost:8081/swagger-ui.html  
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs  

(Reemplaza el puerto si usas otro, por ejemplo 5000 en producción.)

---

## Despliegue

- **Heroku / plataformas con Procfile**: el `Procfile` ejecuta el JAR en el puerto `5000` (`web: java -jar target/aalvarezg-1.0.0.jar --server.port=5000`). Asegúrate de compilar antes (`mvn clean package`) o que el pipeline genere el JAR.
- **AWS Elastic Beanstalk**: la carpeta `.ebextensions` contiene configuración de entorno; usar perfil `prod` y variables `DB_*`, `JWT_SECRET`, `PORT`, `CORS_ORIGINS`.
- **Docker**: usar el `Dockerfile` incluido; en producción inyectar las variables de entorno necesarias para BD y JWT.

---

## Resumen de comandos útiles

| Comando | Descripción |
|---------|-------------|
| `./mvnw spring-boot:run` | Arrancar la aplicación en local |
| `./mvnw test` | Ejecutar todos los tests |
| `./mvnw clean package` | Generar JAR (en `target/aalvarezg-1.0.0.jar`) |
| `./mvnw clean package -DskipTests` | Generar JAR sin ejecutar tests |

---

## Licencia y autor

Proyecto desarrollado en el contexto del challenge SEEK (Task Manager API).
