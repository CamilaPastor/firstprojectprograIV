# Bolsa de Empleo - Proyecto II

Sistema de bolsa de empleo construido como **REST API en Spring Boot 3.2.3** + **SPA en React 18 (Vite)** + **PostgreSQL 16**. Autenticacion con **JWT**.

Curso: Programacion IV (EIF209)
Integrantes: Jose Pablo Leon Arroyo, Josue Guevara Lostalo, Camila Pastor Castro.

## Arquitectura

```
firstprojectprograIV/
├── src/main/java/cr/una/bolsaempleo/        Backend Spring Boot
│   ├── controller/                          REST controllers bajo /api/**
│   ├── service/ + service/impl/             Logica de negocio
│   ├── repository/                          Spring Data JPA
│   ├── model/                               Entities JPA
│   ├── dto/                                 DTOs request/response
│   ├── security/                            JwtUtil, JwtAuthenticationFilter, AuthenticatedUser
│   └── config/                              SecurityConfig (JWT stateless), DataInitializer, GlobalExceptionHandler
├── src/main/resources/
│   ├── application.properties               Configuracion (puerto 8081, JWT, datasource)
│   └── static/                              Build de React (vacio en dev; poblado en prod)
├── frontend/                                React 18 + Vite SPA
│   ├── src/pages/                           Vistas (publicas, admin, empresa, oferente)
│   ├── src/components/                      Layout, PrivateRoute, CaracteristicaTree, etc.
│   ├── src/context/                         AuthContext (manejo de JWT en localStorage)
│   ├── src/lib/api.js                       Wrapper de Fetch con Bearer token
│   └── vite.config.js                       Proxy /api -> :8081, build out -> ../src/main/resources/static
├── Dockerfile                               Multi-stage: node build -> maven build -> JRE
└── docker-compose.yml                       PostgreSQL + app
```

### Endpoints principales

- `POST /api/auth/login` - login (devuelve token JWT y datos del usuario)
- `POST /api/auth/registro/empresa` - registro publico de empresa (queda pendiente de aprobacion)
- `POST /api/auth/registro/oferente` - registro publico de oferente (queda pendiente de aprobacion)
- `GET /api/public/puestos/recientes` - 5 puestos publicos mas recientes
- `GET /api/public/puestos/buscar?ids=1&ids=2` - buscar por caracteristicas
- `GET /api/public/caracteristicas/arbol` - arbol jerarquico de caracteristicas
- `GET|POST /api/admin/**` - acciones de administrador (requiere ROLE_ADMIN)
- `GET|POST /api/empresa/**` - acciones de empresa (requiere ROLE_EMPRESA)
- `GET|POST /api/oferente/**` - acciones de oferente (requiere ROLE_OFERENTE)

El token JWT se envia en el header `Authorization: Bearer <token>`.

### Usuarios sembrados (DataInitializer / BolsaEmpleo.sql)

| Rol      | Usuario / correo               | Contrasena    | Estado     |
|----------|--------------------------------|---------------|------------|
| Admin    | admin                          | admin123      | activo     |
| Empresa  | info@techsolutions.cr          | empresa123    | aprobada   |
| Empresa  | contact@innovate.cr            | empresa123    | aprobada   |
| Empresa  | support@cloudservices.cr       | empresa123    | aprobada   |
| Empresa  | hr@datacorp.cr                 | empresa123    | pendiente  |
| Empresa  | hola@startuplabs.cr            | empresa123    | pendiente  |
| Oferente | juan.perez@gmail.com           | oferente123   | aprobado   |
| Oferente | maria.gonzalez@gmail.com       | oferente123   | aprobado   |
| Oferente | carlos.ramirez@gmail.com       | oferente123   | aprobado   |
| Oferente | ana.vargas@gmail.com           | oferente123   | aprobado   |
| Oferente | luis.castro@gmail.com          | oferente123   | aprobado   |
| Oferente | sofia.mora@gmail.com           | oferente123   | pendiente  |
| Oferente | diego.hernandez@gmail.com      | oferente123   | pendiente  |

Datos de prueba sembrados: 5 empresas, 7 oferentes, arbol de 20 caracteristicas (4 raices),
9 puestos (publicos y privados, activos e inactivos), 21 habilidades y 2 CV en PDF.
Las cuentas pendientes sirven para probar el flujo de aprobacion del administrador.
Los puestos privados solo aparecen al iniciar sesion como oferente.

---

## Requisitos

- Java 17+
- Maven 3.9+ (o usar el `./mvnw` incluido)
- Node 20+ y npm (solo para desarrollo del frontend)
- PostgreSQL 16 (o Docker)

---

## Levantar en desarrollo

En desarrollo se corren dos procesos: el backend Spring Boot en `:8081` y el dev server de Vite en `:5173` que hace proxy de `/api` al backend.

### 1) Base de datos

Opcion A (Docker, solo Postgres):
```bash
docker compose up -d postgres
```

Opcion B (Postgres local): crear base `bolsa_empleo` y usuario `bolsa_user / bolsa1234`, y cargar el script `BolsaEmpleo.sql`.

### 2) Backend

```bash
./mvnw spring-boot:run
```

Queda escuchando en http://localhost:8081 y expone solo la REST API.

### 3) Frontend

```bash
cd frontend
npm install
npm run dev
```

Abre http://localhost:5173. Vite hace proxy de `/api/**` al backend en `:8081` con hot reload.

---

## Levantar en produccion (un solo servidor)

El build de React se compila a estatico y se copia dentro de `src/main/resources/static`, de modo que Spring Boot sirve la SPA y la REST API desde el mismo JAR.

### Con Docker (recomendado)

```bash
docker compose up --build
```

El servicio `app` corre el multi-stage Dockerfile:
1. Compila el frontend con Node 20 (`vite build`).
2. Copia el build a `src/main/resources/static`.
3. Compila el JAR con Maven.
4. Empaqueta con `eclipse-temurin:17-jre-alpine`.

Aplicacion disponible en http://localhost:8082.

### Build manual

```bash
cd frontend
npm install
npm run build         # genera ../src/main/resources/static
cd ..
./mvnw clean package -DskipTests
java -jar target/bolsa-empleo-1.0.0.jar
```

Aplicacion disponible en http://localhost:8081.

---

## Configuracion

Variables principales en `src/main/resources/application.properties` (sobreescribibles por variables de entorno Spring):

| Propiedad                  | Default                                                           |
|----------------------------|-------------------------------------------------------------------|
| `server.port`              | `8081` (en Docker queda en `8080` dentro del contenedor)          |
| `spring.datasource.url`    | `jdbc:postgresql://localhost:5432/bolsa_empleo`                   |
| `app.jwt.secret`           | clave HMAC de >=256 bits (cambiar en produccion)                  |
| `app.jwt.expiration-ms`    | `86400000` (24 horas)                                             |
| `app.cors.allowed-origins` | `http://localhost:5173,http://127.0.0.1:5173`                     |

---

## Funcionalidades

- Parte publica con los 5 puestos publicos mas recientes y detalle al hacer hover.
- Busqueda publica por caracteristicas con arbol jerarquico de checkboxes.
- Registro publico de empresa y oferente, ambos pendientes de aprobacion del administrador.
- Login con JWT, redireccion por rol (`admin`, `empresa`, `oferente`).
- Administrador: aprobar empresas y oferentes, CRUD del arbol de caracteristicas, reportes mensuales en PDF.
- Empresa: publicar puestos, ver candidatos ordenados por porcentaje de cumplimiento, descargar CVs.
- Oferente: gestionar habilidades por nivel, subir/reemplazar CV en PDF.
