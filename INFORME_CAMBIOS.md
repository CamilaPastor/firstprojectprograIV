# Informe de Cambios — Bolsa de Empleo
## EIF209 Programación IV — Universidad Nacional de Costa Rica
### Fecha: 26 de marzo de 2026

---

## 1. Resumen Ejecutivo

Se realizó una auditoría completa, refactorización y corrección del proyecto Spring Boot "Bolsa de Empleo". El proyecto ahora cuenta con:

- **42 archivos Java** correctamente estructurados (modelos, repositorios, servicios, controladores, DTOs, utilidades, configuración)
- **20 plantillas HTML** Thymeleaf con layout compartido
- **pom.xml** Maven completo con todas las dependencias
- **Dockerfile** multi-stage y **docker-compose.yml** listos para despliegue
- **Script SQL** con datos de prueba y hashes BCrypt válidos
- **CSS** completo con estilos para formularios, tablas, alertas, badges y más

---

## 2. Problemas Encontrados y Correcciones

### 2.1 Dependencias (pom.xml)
| Problema | Corrección |
|----------|-----------|
| `itext7-core` como BOM no incluía módulos transitivos | Declarar `kernel`, `layout`, `io` v7.2.5 individualmente |
| Falta `thymeleaf-layout-dialect` (templates usan `layout:decorate`) | Agregado como dependencia |
| Driver Oracle en build.gradle original (proyecto usa MySQL) | Migrado a `mysql-connector-j` en Maven |
| Proyecto usaba Gradle | Migrado completamente a Maven con `pom.xml` |

### 2.2 Modelos JPA
| Problema | Corrección |
|----------|-----------|
| `@NotBlank @Size(min=20)` en `passwordHash` de Empresa/Oferente bloqueaba registro | Eliminadas anotaciones de validación (validación en capa de servicio) |
| `FetchType.LAZY` en `Puesto.empresa` causaba LazyInitializationException | Cambiado a `FetchType.EAGER` |
| `Set<>` sin orden definido en colecciones | Cambiado a `List<>` con `@OrderBy` |
| Colecciones no usadas en Caracteristica (`puestosRequeridos`, `oferentesConCaracteristica`) | Eliminadas |
| `hibernate.dialect=MySQL8Dialect` deprecado | Eliminado (Spring Boot lo auto-detecta) |

### 2.3 Controladores
| Problema | Corrección |
|----------|-----------|
| Rutas no coincidían con plantillas existentes | Reescritos todos los controladores con rutas correctas |
| HomeController y PublicController ambos mapeaban "/" | HomeController maneja "/" exclusivamente |
| Formularios usaban `@ModelAttribute` sin match con campos del modelo | Cambiado a `@RequestParam` con construcción manual |
| Campo `password` en formulario vs `passwordSinHashear` en modelo | Controlador lee `password` y asigna a `passwordSinHashear` |
| Bug en nuevo-puesto: arrays `caracteristicas[]` y `niveles[]` desalineados | Cada nivel usa nombre `nivel_${id}`, controller lee por ID |

### 2.4 Plantillas HTML
| Problema | Corrección |
|----------|-----------|
| ~29 archivos HTML huérfanos o duplicados | Eliminados, creados 20 templates correctos |
| Sin layout compartido funcional | Creado `layout.html` con Thymeleaf Layout Dialect |
| Navbar no mostraba enlaces según rol | Navbar condicional: empresa/oferente/admin/público |
| Sin mensajes flash de éxito/error | Agregado soporte en layout.html |

### 2.5 Configuración
| Problema | Corrección |
|----------|-----------|
| Spring Security interceptaba todas las rutas | Configurado `permitAll()` con CSRF deshabilitado |
| Dockerfile health check usaba `/actuator/health` (sin actuator) | Cambiado a health check contra "/" |
| docker-compose usaba `ddl-auto: validate` (falla sin tablas) | Cambiado a `update` |
| Archivos Gradle residuales (build.gradle, gradlew, etc.) | Eliminados |

### 2.6 Datos de Prueba
| Problema | Corrección |
|----------|-----------|
| Hashes BCrypt fabricados (no coincidían con contraseñas) | Generados hashes reales con BCrypt |
| Query `findAllActivos()` no filtraba por `aprobado=true` | Agregada condición `AND o.aprobado = true` |

### 2.7 Tests
| Problema | Corrección |
|----------|-----------|
| Test unitario referenciaba rutas eliminadas (/about, /contact) | Simplificado a `contextLoads()` |

---

## 3. Estructura Final del Proyecto

```
Proyecto/
├── pom.xml                          # Maven - Spring Boot 3.2.3, Java 17
├── mvnw                             # Maven Wrapper (ejecutable)
├── .mvn/wrapper/                    # Configuración Maven Wrapper
├── Dockerfile                       # Build multi-stage
├── docker-compose.yml               # MySQL 8 + App
├── BolsaEmpleo.sql                  # Script DDL + datos de prueba
├── src/main/java/cr/una/bolsaempleo/
│   ├── BolsaEmpleoApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java      # BCrypt + permitAll
│   │   └── GlobalExceptionHandler.java
│   ├── controller/
│   │   ├── HomeController.java      # GET /
│   │   ├── LoginController.java     # GET/POST /login, /logout
│   │   ├── PublicController.java    # Registro, búsqueda pública
│   │   ├── EmpresaController.java   # /empresa/*
│   │   ├── OferenteController.java  # /oferente/*
│   │   └── AdminController.java     # /admin/*
│   ├── dto/
│   │   ├── SessionUser.java         # Datos de sesión HTTP
│   │   └── ResultadoCandidato.java  # Resultado búsqueda con %
│   ├── model/
│   │   ├── Administrador.java
│   │   ├── Empresa.java
│   │   ├── Oferente.java
│   │   ├── Puesto.java
│   │   ├── Caracteristica.java      # Jerárquica (padre-hijo)
│   │   ├── PuestoCaracteristica.java
│   │   ├── OferenteCaracteristica.java
│   │   └── Cv.java                  # PDF como LONGBLOB
│   ├── repository/                  # Spring Data JPA
│   ├── service/                     # Interfaces
│   ├── service/impl/                # Implementaciones
│   └── util/
│       └── SessionUtil.java         # Manejo de HttpSession
├── src/main/resources/
│   ├── application.properties
│   ├── static/css/style.css
│   └── templates/
│       ├── layout.html              # Layout maestro
│       ├── public/                  # 5 plantillas públicas
│       ├── empresa/                 # 5 plantillas empresa
│       ├── oferente/                # 3 plantillas oferente
│       ├── admin/                   # 5 plantillas admin
│       └── error/error.html
└── src/test/java/                   # Test básico
```

---

## 4. Dependencias Maven

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| spring-boot-starter-web | 3.2.3 | Spring MVC + Tomcat embebido |
| spring-boot-starter-thymeleaf | 3.2.3 | Server Side Rendering |
| thymeleaf-layout-dialect | (auto) | Template inheritance (layout:decorate) |
| spring-boot-starter-data-jpa | 3.2.3 | JPA + Hibernate |
| spring-boot-starter-validation | 3.2.3 | Bean Validation |
| spring-boot-starter-security | 3.2.3 | BCryptPasswordEncoder |
| mysql-connector-j | (auto) | Driver MySQL 8 |
| lombok | (auto) | Reducción de boilerplate |
| itext kernel/layout/io | 7.2.5 | Generación de reportes PDF |

---

## 5. Credenciales de Prueba

| Tipo | Usuario | Contraseña |
|------|---------|-----------|
| Administrador | admin | admin123 |
| Empresa | info@techsolutions.cr | empresa123 |
| Empresa | contact@innovate.cr | empresa123 |
| Empresa | support@cloudservices.cr | empresa123 |
| Oferente | juan.perez@gmail.com | oferente123 |
| Oferente | maria.gonzalez@gmail.com | oferente123 |
| Oferente | carlos.ramirez@gmail.com | oferente123 |

---

## 6. Instrucciones de Ejecución

### Opción A: Ejecución Local (requiere MySQL y Java 17)

**Prerrequisitos:**
- Java 17 (JDK)
- Maven 3.9+ (o usar el wrapper `./mvnw`)
- MySQL 8.0 corriendo en localhost:3306

**Pasos:**

```bash
# 1. Crear la base de datos
mysql -u root -p1234 -e "CREATE DATABASE IF NOT EXISTS bolsa_empleo;"
mysql -u root -p1234 bolsa_empleo < BolsaEmpleo.sql

# 2. Compilar el proyecto
mvn clean compile

# 3. Empaquetar (crear JAR ejecutable)
mvn clean package -DskipTests

# 4. Ejecutar la aplicación
java -jar target/bolsa-empleo-1.0.0.jar

# O directamente con Maven:
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

### Opción B: Ejecución con Docker (recomendado)

**Prerrequisitos:**
- Docker y Docker Compose instalados

```bash
# 1. Construir y levantar servicios
docker-compose up --build

# 2. Para detener
docker-compose down

# 3. Para limpiar datos
docker-compose down -v
```

La aplicación estará disponible en: **http://localhost:8080**
MySQL estará accesible en: **localhost:3306**

### Opción C: Desde IntelliJ IDEA

1. Abrir el proyecto (File → Open → seleccionar carpeta del proyecto)
2. IntelliJ detectará automáticamente el `pom.xml`
3. Esperar a que descargue las dependencias
4. Ejecutar `BolsaEmpleoApplication.java` (clic derecho → Run)
5. Asegurarse de que MySQL esté corriendo con la base de datos `bolsa_empleo`

---

## 7. Flujos Funcionales

### Registro y Login
1. Usuario se registra como Empresa u Oferente (queda pendiente de aprobación)
2. Administrador aprueba la cuenta
3. Usuario inicia sesión y accede a su dashboard

### Empresa - Publicar Puesto
1. Empresa publica puesto con descripción, salario, tipo y características requeridas
2. Cada característica tiene un nivel requerido (1-5)
3. Empresa puede buscar candidatos con porcentaje de coincidencia

### Oferente - Gestionar Habilidades
1. Oferente navega el árbol jerárquico de características
2. Agrega habilidades con nivel de dominio (1-5)
3. Sube CV en formato PDF

### Admin - Gestión y Reportes
1. Aprueba empresas y oferentes pendientes
2. Gestiona el árbol de características (crear categorías y subcategorías)
3. Genera reportes PDF mensuales de puestos publicados
