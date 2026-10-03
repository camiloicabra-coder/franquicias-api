# API de Gestión de Franquicias

API REST desarrollada con **Java y Spring Boot** para la gestión de franquicias, sucursales y productos.

El proyecto permite administrar la relación:

**Franquicia → Sucursales → Productos**

e incluye operaciones para crear y actualizar recursos, gestionar inventario, eliminar productos y consultar el producto con mayor stock por sucursal.

## Tecnologías

* Java 24
* Spring Boot 4.1.1
* Spring Web MVC
* Spring Data JPA
* Hibernate
* MySQL 8
* Maven
* Swagger / OpenAPI
* JUnit
* Mockito
* MockMvc
* Docker
* Docker Compose
* Git / GitHub

## Arquitectura

El proyecto utiliza una arquitectura por capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL
```

### Capas principales

**Controller**

Expone los endpoints REST y recibe las solicitudes HTTP.

**Service**

Contiene la lógica de negocio y las validaciones relacionadas con las operaciones.

**Repository**

Utiliza Spring Data JPA para la persistencia y consulta de información.

**Entity**

Representa las entidades principales del dominio:

* Franquicia
* Sucursal
* Producto

**DTO**

Se utilizan objetos de transferencia para separar los modelos de entrada y salida de las entidades de persistencia.

**Exception**

Incluye manejo global de errores mediante `@RestControllerAdvice`.

## Modelo de datos

La relación entre las entidades es:

```text
Franquicia
    │
    └── 1:N
          │
       Sucursal
          │
          └── 1:N
                │
             Producto
```

### Franquicia

* `id`
* `nombre`

### Sucursal

* `id`
* `nombre`
* `franquicia`

### Producto

* `id`
* `nombre`
* `stock`
* `sucursal`

## Funcionalidades

La API permite:

* Crear una franquicia.
* Actualizar el nombre de una franquicia.
* Crear una sucursal asociada a una franquicia.
* Actualizar el nombre de una sucursal.
* Crear productos en una sucursal.
* Actualizar el nombre de un producto.
* Actualizar el stock de un producto.
* Eliminar productos de una sucursal.
* Consultar el producto con mayor stock de cada sucursal de una franquicia.
* Validar datos de entrada.
* Manejar recursos inexistentes mediante respuestas `404 Not Found`.

## Endpoints

### Franquicias

| Método | Endpoint                          | Descripción       |
| ------ | --------------------------------- | ----------------- |
| POST   | `/api/franquicias`                | Crear franquicia  |
| PUT    | `/api/franquicias/{franquiciaId}` | Actualizar nombre |

### Sucursales

| Método | Endpoint                                                  | Descripción       |
| ------ | --------------------------------------------------------- | ----------------- |
| POST   | `/api/franquicias/{franquiciaId}/sucursales`              | Crear sucursal    |
| PUT    | `/api/franquicias/{franquiciaId}/sucursales/{sucursalId}` | Actualizar nombre |

### Productos

| Método | Endpoint                                                    | Descripción       |
| ------ | ----------------------------------------------------------- | ----------------- |
| POST   | `/api/sucursales/{sucursalId}/productos`                    | Crear producto    |
| PUT    | `/api/sucursales/{sucursalId}/productos/{productoId}`       | Actualizar nombre |
| PATCH  | `/api/sucursales/{sucursalId}/productos/{productoId}/stock` | Actualizar stock  |
| DELETE | `/api/sucursales/{sucursalId}/productos/{productoId}`       | Eliminar producto |

### Consulta de mayor stock

```http
GET /api/franquicias/{franquiciaId}/productos/mayor-stock
```

Este endpoint devuelve el producto con mayor stock de cada sucursal perteneciente a la franquicia indicada.

Ejemplo:

```json
[
  {
    "productoId": 1,
    "producto": "Producto A",
    "stock": 100,
    "sucursalId": 2,
    "sucursal": "Sucursal Centro"
  }
]
```

## Documentación Swagger

La API cuenta con documentación interactiva mediante Swagger UI.

Con la aplicación ejecutándose localmente:

```text
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger se pueden consultar y ejecutar los endpoints disponibles.

## Ejecución con Docker

El proyecto incluye:

* `Dockerfile`
* `docker-compose.yml`

### Requisitos

* Docker Desktop instalado y ejecutándose.

### Levantar la aplicación

Desde la raíz del proyecto:

```bash
docker compose up --build
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

MySQL se expone en el puerto:

```text
3307
```

Dentro de la red de Docker, la aplicación se conecta a MySQL mediante:

```text
mysql:3306
```

### Ejecutar en segundo plano

```bash
docker compose up -d
```

### Detener los contenedores

```bash
docker compose down
```

## Ejecución local

También es posible ejecutar el proyecto directamente con Maven.

### Requisitos

* JDK 24
* MySQL 8
* Maven Wrapper incluido en el proyecto.

Crear la base de datos:

```sql
CREATE DATABASE franquicias_db;
```

La configuración por defecto utiliza:

```text
URL: jdbc:mysql://localhost:3306/franquicias_db
Usuario: root
Password: vacío
```

Estas propiedades pueden modificarse mediante variables de entorno:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

### Ejecutar la aplicación

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
./mvnw spring-boot:run
```

## Pruebas

El proyecto cuenta con pruebas unitarias y pruebas de integración.

Para ejecutar toda la suite:

Windows:

```powershell
.\mvnw.cmd test
```

Linux / macOS:

```bash
./mvnw test
```

Resultado validado durante el desarrollo:

```text
Tests run: 32
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Las pruebas cubren:

* Creación de franquicias.
* Validación de datos.
* Actualización de franquicias.
* Creación y actualización de sucursales.
* Creación de productos.
* Validación de stock.
* Actualización de stock.
* Actualización de productos.
* Eliminación de productos.
* Validación de recursos inexistentes.
* Restricción de acceso a recursos pertenecientes a otra sucursal o franquicia.
* Consulta de productos con mayor stock.

## Validaciones y manejo de errores

La API utiliza Bean Validation para validar los datos recibidos.

Entre las validaciones implementadas:

* El nombre de franquicia, sucursal y producto es obligatorio.
* El stock es obligatorio.
* El stock no puede ser negativo.

Los errores de validación generan respuestas `400 Bad Request`.

Los recursos inexistentes generan respuestas `404 Not Found`.

El manejo global de excepciones se implementa mediante:

```text
@RestControllerAdvice
```

## Estructura del proyecto

```text
src/
├── main/
│   ├── java/com/nequi/franquicias/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exception/
│   │   ├── repository/
│   │   └── service/
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/com/nequi/franquicias/
        ├── controller/
        └── service/

Dockerfile
docker-compose.yml
pom.xml
mvnw
mvnw.cmd
```

## Flujo principal

```text
Crear Franquicia
      ↓
Crear Sucursal
      ↓
Crear Producto
      ↓
Actualizar Stock
      ↓
Consultar Mayor Stock
```

Ejemplo:

```text
Franquicia Demo
      │
      └── Sucursal Centro
              │
              ├── Producto A → 100 unidades
              └── Producto B → 50 unidades
```

La consulta de mayor stock devolverá:

```text
Producto A → 100 unidades
```

## Decisiones técnicas

### Persistencia

Se utiliza **MySQL** como motor de persistencia y **Spring Data JPA** para el acceso a datos.

### Validación

Se utiliza Jakarta Bean Validation para validar los datos de entrada antes de procesarlos.

### Manejo de errores

Se centraliza mediante un `GlobalExceptionHandler`, evitando duplicar lógica de manejo de excepciones en los controladores.

### Contenedores

Docker Compose permite ejecutar la aplicación y MySQL como servicios independientes, facilitando la configuración y ejecución del proyecto.

## Autor

**Camilo Cabra**

Ingeniero de Sistemas

GitHub: https://github.com/camiloicabra-coder
