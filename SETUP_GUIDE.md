# 🎓 Bolsa de Empleo - Job Board System

Sistema de Bolsa de Empleo desarrollado con **Spring Boot 3.2.3** y **Oracle Database**.

## 📋 Requisitos Previos

- **Java 17+**
- **Oracle Database 19c+** (XE o superior)
- **Gradle 8.0+**
- **Maven** (opcional)

## 🔧 Configuración Inicial

### 1. Configurar Base de Datos Oracle

#### Crear la tabla EMPRESA
```sql
CREATE SEQUENCE empresa_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE EMPRESA (
    ID_EMPRESA NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(150) NOT NULL,
    LOCALIZACION VARCHAR2(255),
    CORREO VARCHAR2(100) NOT NULL UNIQUE,
    TELEFONO VARCHAR2(20),
    DESCRIPCION CLOB,
    PASSWORD_HASH VARCHAR2(255) NOT NULL,
    APROBADO NUMBER(1) DEFAULT 0 NOT NULL,
    FECHA_REGISTRO TIMESTAMP,
    
    CONSTRAINT chk_empresa_aprobado CHECK (APROBADO IN (0, 1))
);

CREATE INDEX idx_empresa_correo ON EMPRESA(CORREO);
CREATE INDEX idx_empresa_aprobado ON EMPRESA(APROBADO);
CREATE INDEX idx_empresa_localizacion ON EMPRESA(LOCALIZACION);
```

#### Crear la tabla OFERENTE
```sql
CREATE SEQUENCE oferente_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE OFERENTE (
    ID_OFERENTE NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(100) NOT NULL,
    APELLIDOS VARCHAR2(100) NOT NULL,
    CORREO VARCHAR2(100) NOT NULL UNIQUE,
    TELEFONO VARCHAR2(20),
    PASSWORD_HASH VARCHAR2(255) NOT NULL,
    LOCALIZACION VARCHAR2(255),
    PROFESION VARCHAR2(100),
    EXPERIENCIA NUMBER,
    DESCRIPCION CLOB,
    FECHA_REGISTRO TIMESTAMP,
    
    CONSTRAINT chk_oferente_exp CHECK (EXPERIENCIA >= 0)
);

CREATE INDEX idx_oferente_correo ON OFERENTE(CORREO);
CREATE INDEX idx_oferente_profesion ON OFERENTE(PROFESION);
CREATE INDEX idx_oferente_localizacion ON OFERENTE(LOCALIZACION);
```

### 2. Configurar application.properties

Editar `src/main/resources/application.properties`:

```properties
# Database Configuration
db.host=localhost
db.port=1521
db.sid=XE
db.username=system
db.password=tu_contraseña_aqui

# Server Configuration
server.port=8080
spring.application.name=Bolsa_Empleo
```

### 3. Construir y Ejecutar

#### Usando Gradle
```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun
```

#### Usando Maven
```bash
# Compilar
mvn clean package

# Ejecutar
mvn spring-boot:run
```

## 🌐 Acceder a la Aplicación

Una vez iniciada, accede a:
- **http://localhost:8080** - Página principal
- **http://localhost:8080/login** - Panel de login

## 👥 Usuarios de Prueba

### Empresa
- Email: `info@tech.com`
- Password: `SecurePass123`

### Oferente
- Email: `juan@email.com`
- Password: `MyPassword456`

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/bolsaempleo/
│   │   ├── config/
│   │   │   ├── DatabaseConfig.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── WebMvcConfig.java
│   │   ├── controller/
│   │   │   ├── LoginController.java
│   │   │   ├── EmpresaController.java
│   │   │   └── OferenteController.java
│   │   ├── model/
│   │   │   ├── Empresa.java
│   │   │   ├── Oferente.java
│   │   │   └── *Validator.java
│   │   ├── repository/
│   │   │   ├── EmpresaRepositoryJdbc.java
│   │   │   └── OferenteRepositoryJdbc.java
│   │   ├── service/
│   │   │   ├── EmpresaService.java
│   │   │   └── OferenteService.java
│   │   ├── util/
│   │   │   ├── OracleDbConnection.java
│   │   │   ├── PasswordHashUtil.java
│   │   │   └── OracleConnectionPool.java
│   │   └── BolsaEmpleoApplication.java
│   └── resources/
│       ├── application.properties
│       ├── templates/
│       │   ├── login/
│       │   ├── empresa/
│       │   ├── oferente/
│       │   └── error/
│       └── static/css/
│           └── style.css
└── test/
```

## 🔑 Características Implementadas

✅ **Autenticación**
- Login unificado para Empresa y Oferente
- Validación de credenciales
- Gestión de sesiones seguras

✅ **Modelos**
- Empresa (Reclutadores)
- Oferente (Candidatos)

✅ **Controladores**
- LoginController (6 endpoints)
- EmpresaController (8 endpoints)
- OferenteController (5 endpoints)

✅ **Servicios**
- EmpresaService
- OferenteService
- Validaciones y lógica de negocio

✅ **Vistas**
- Formularios de login/registro
- Dashboards personalizados
- Perfiles de usuario

✅ **Seguridad**
- SHA-256 + Salt para contraseñas
- Validación de entrada
- CSRF protection
- Session management

## 📖 Documentación

Consulta los siguientes archivos para más detalles:
- `EMPRESA_MODEL.md` - Documentación del modelo Empresa
- `LOGIN_CONTROLLER.md` - Documentación del controlador de login
- `EMPRESA_CONTROLLER.md` - Documentación del controlador de empresa
- `EMPRESA_SERVICE_JDBC.md` - Documentación de servicios

## 🐛 Troubleshooting

### Error: OracleDbConnection not found
- Verificar que DatabaseConfig.java exista en `/config`
- Asegurar que la clase está anotada con `@Configuration`

### Error: Service not found
- Verificar que las clases de servicio tengan `@Service`
- Verificar que los repositorios tengan `@Repository`

### Error: Database connection failed
- Verificar credenciales en `application.properties`
- Asegurar que Oracle Database está corriendo
- Verificar que el puerto 1521 esté abierto

### Error: javax.servlet.http.HttpSession not found
- Actualizar a Spring Boot 3.2.3 (usa jakarta.servlet.http.HttpSession)
- Cambiar imports de `javax.servlet` a `jakarta.servlet`

## 📝 Cambios Realizados para Arreglar Errores

1. ✅ Agregado `@Service` a EmpresaService y OferenteService
2. ✅ Agregado `@Repository` a repositorios JDBC
3. ✅ Agregado `@RequiredArgsConstructor` para inyección automática
4. ✅ Creado DatabaseConfig.java para inyección de OracleDbConnection
5. ✅ Actualizado build.gradle con versiones correctas
6. ✅ Agregado GlobalExceptionHandler para manejo de errores
7. ✅ Creado CSS base para estilos globales
8. ✅ Agregado WebMvcConfig para recursos estáticos
9. ✅ Actualizado application.properties con configuración JDBC

## 🚀 Próximos Pasos

- [ ] Implementar sistema de ofertas de empleo
- [ ] Crear CV uploader
- [ ] Implementar sistema de aplicaciones
- [ ] Agregar notificaciones por email
- [ ] Implementar búsqueda avanzada
- [ ] Panel de administrador

## 📄 Licencia

Proyecto educativo - Instituto de Educación Superior

## 👨‍💼 Autor

Desarrollado como parte del curso de Programación IV

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.0.0 (Corregida)
