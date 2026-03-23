# Bolsa de Empleo - Aplicación Spring Boot MVC

Una plataforma web moderna para conectar empresas con candidatos talentosos. Sistema completo de gestión de empleos con registro, creación de puestos, carga de CV y gestión de habilidades.

## 📋 Características

### Para Empresas
- ✅ Registro y login de empresa
- ✅ Crear y publicar ofertas de empleo
- ✅ Gestionar perfiles de empresa
- ✅ Agregar características/requisitos a puestos
- ✅ Ver lista de empleos activos

### Para Candidatos
- ✅ Registro y login de candidato
- ✅ Gestión de perfil profesional
- ✅ Agregar habilidades y características
- ✅ Carga y gestión de CV (múltiples archivos)
- ✅ Establecer CV principal
- ✅ Buscar empleos por ubicación

### General
- ✅ Arquitectura MVC limpia
- ✅ Base de datos Oracle
- ✅ Interfaz sin JavaScript (HTML + Thymeleaf)
- ✅ Sistema de seguridad con contraseñas encriptadas (BCrypt)
- ✅ Responsive design

## 🏗️ Arquitectura del Proyecto

```
Bolsa_Empleo/
├── src/
│   ├── main/
│   │   ├── java/com/bolsaempleo/
│   │   │   ├── config/              # Configuración de la aplicación
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/          # Controladores MVC
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── EmpresaController.java
│   │   │   │   ├── OferenteController.java
│   │   │   │   ├── PuestoController.java
│   │   │   │   ├── CaracteristicaController.java
│   │   │   │   └── CVController.java
│   │   │   ├── model/               # Modelos de entidades
│   │   │   │   ├── Empresa.java
│   │   │   │   ├── Oferente.java
│   │   │   │   ├── Administrador.java
│   │   │   │   ├── Puesto.java
│   │   │   │   ├── Caracteristica.java
│   │   │   │   └── CV.java
│   │   │   ├── repository/          # Repositorios de acceso a datos
│   │   │   │   ├── EmpresaRepository.java
│   │   │   │   ├── OferenteRepository.java
│   │   │   │   ├── PuestoRepository.java
│   │   │   │   ├── CaracteristicaRepository.java
│   │   │   │   └── CVRepository.java
│   │   │   └── service/             # Lógica de negocio
│   │   │       ├── EmpresaService.java
│   │   │       ├── OferenteService.java
│   │   │       ├── PuestoService.java
│   │   │       ├── CaracteristicaService.java
│   │   │       └── CVService.java
│   │   ├── resources/
│   │   │   ├── templates/           # Vistas Thymeleaf
│   │   │   │   ├── index.html
│   │   │   │   ├── acerca.html
│   │   │   │   ├── contacto.html
│   │   │   │   ├── empresa/
│   │   │   │   │   ├── registro.html
│   │   │   │   │   ├── login.html
│   │   │   │   │   ├── dashboard.html
│   │   │   │   │   └── perfil.html
│   │   │   │   ├── oferente/
│   │   │   │   │   ├── registro.html
│   │   │   │   │   ├── login.html
│   │   │   │   │   ├── dashboard.html
│   │   │   │   │   └── perfil.html
│   │   │   │   ├── puesto/
│   │   │   │   │   ├── lista.html
│   │   │   │   │   ├── detalle.html
│   │   │   │   │   ├── crear.html
│   │   │   │   │   └── editar.html
│   │   │   │   ├── caracteristica/
│   │   │   │   │   ├── agregar.html
│   │   │   │   │   └── agregar-habilidad.html
│   │   │   │   └── cv/
│   │   │   │       ├── subir.html
│   │   │   │       └── listar.html
│   │   │   ├── static/
│   │   │   │   └── css/
│   │   │   │       └── style.css
│   │   │   └── application.properties
│   └── test/                        # Tests unitarios
└── build.gradle                     # Configuración de Gradle
```

## 🔧 Tecnologías Utilizadas

- **Framework:** Spring Boot 4.0.3
- **Java:** JDK 17
- **Base de Datos:** Oracle Database
- **Template Engine:** Thymeleaf
- **ORM:** Spring Data JPA (Hibernate)
- **Seguridad:** Spring Security con BCrypt
- **Build Tool:** Gradle
- **Frontend:** HTML5 + CSS3 (Sin JavaScript)

## 📦 Dependencias Principales

```gradle
- spring-boot-starter-web: Desarrollo web MVC
- spring-boot-starter-data-jpa: Acceso a datos con JPA
- spring-boot-starter-thymeleaf: Templates HTML
- spring-boot-starter-validation: Validación de datos
- spring-boot-starter-security: Seguridad de la aplicación
- ojdbc11: Driver JDBC para Oracle
- lombok: Reducción de código boilerplate
```

## 🗄️ Esquema de Base de Datos

### Tabla EMPRESA
```sql
CREATE TABLE EMPRESA (
    EMPRESA_ID NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(100) NOT NULL,
    EMAIL VARCHAR2(100) UNIQUE NOT NULL,
    PASSWORD VARCHAR2(255) NOT NULL,
    RAZON_SOCIAL VARCHAR2(150),
    RFC VARCHAR2(13) UNIQUE,
    TELEFONO VARCHAR2(20),
    DIRECCION VARCHAR2(255),
    CIUDAD VARCHAR2(100),
    ESTADO VARCHAR2(100),
    DESCRIPCION CLOB,
    SITIO_WEB VARCHAR2(200),
    FECHA_REGISTRO TIMESTAMP,
    ACTIVA NUMBER(1)
);
```

### Tabla OFERENTE
```sql
CREATE TABLE OFERENTE (
    OFERENTE_ID NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(100) NOT NULL,
    APELLIDO VARCHAR2(100) NOT NULL,
    EMAIL VARCHAR2(100) UNIQUE NOT NULL,
    PASSWORD VARCHAR2(255) NOT NULL,
    TELEFONO VARCHAR2(20),
    DIRECCION VARCHAR2(255),
    CIUDAD VARCHAR2(100),
    ESTADO VARCHAR2(100),
    PAIS VARCHAR2(100),
    FECHA_NACIMIENTO DATE,
    PROFESION VARCHAR2(150),
    EXPERIENCIA_ANIOS NUMBER(2),
    RESUMEN_PROFESIONAL CLOB,
    FECHA_REGISTRO TIMESTAMP,
    ACTIVO NUMBER(1)
);
```

### Tabla PUESTO
```sql
CREATE TABLE PUESTO (
    PUESTO_ID NUMBER PRIMARY KEY,
    TITULO VARCHAR2(150) NOT NULL,
    DESCRIPCION CLOB,
    REQUISITOS CLOB,
    SALARIO_MINIMO NUMBER(10,2),
    SALARIO_MAXIMO NUMBER(10,2),
    MONEDA VARCHAR2(10),
    TIPO_CONTRATO VARCHAR2(50),
    EXPERIENCIA_REQUERIDA VARCHAR2(50),
    NIVEL_EDUCACION VARCHAR2(100),
    UBICACION VARCHAR2(200),
    CIUDAD VARCHAR2(100),
    ESTADO VARCHAR2(100),
    FECHA_PUBLICACION TIMESTAMP,
    FECHA_CIERRE TIMESTAMP,
    ACTIVO NUMBER(1),
    EMPRESA_ID NUMBER NOT NULL,
    FOREIGN KEY (EMPRESA_ID) REFERENCES EMPRESA(EMPRESA_ID)
);
```

### Tabla CARACTERISTICA
```sql
CREATE TABLE CARACTERISTICA (
    CARACTERISTICA_ID NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(100) NOT NULL,
    TIPO VARCHAR2(50),
    NIVEL VARCHAR2(50),
    DESCRIPCION CLOB,
    PUESTO_ID NUMBER,
    OFERENTE_ID NUMBER,
    FOREIGN KEY (PUESTO_ID) REFERENCES PUESTO(PUESTO_ID),
    FOREIGN KEY (OFERENTE_ID) REFERENCES OFERENTE(OFERENTE_ID)
);
```

### Tabla CV
```sql
CREATE TABLE CV (
    CV_ID NUMBER PRIMARY KEY,
    NOMBRE_ARCHIVO VARCHAR2(255) NOT NULL,
    RUTA_ARCHIVO VARCHAR2(512) NOT NULL,
    TIPO_ARCHIVO VARCHAR2(50),
    TAMANIO NUMBER,
    FECHA_CARGA TIMESTAMP,
    PRINCIPAL NUMBER(1),
    ACTIVO NUMBER(1),
    OFERENTE_ID NUMBER NOT NULL,
    FOREIGN KEY (OFERENTE_ID) REFERENCES OFERENTE(OFERENTE_ID)
);
```

## 🚀 Instalación y Configuración

### Requisitos Previos
- JDK 17 o superior
- Oracle Database (versión 11g o superior)
- Gradle 7.0 o superior
- IDE recomendado: IntelliJ IDEA o Eclipse

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   cd Bolsa_Empleo
   ```

2. **Configurar la Base de Datos en `application.properties`**
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
   spring.datasource.username=system
   spring.datasource.password=password
   ```

3. **Crear las tablas en la base de datos**
   - Ejecutar los scripts SQL proporcionados
   - O dejar que Hibernate las cree con `ddl-auto=update`

4. **Compilar el proyecto**
   ```bash
   gradlew build
   ```

5. **Ejecutar la aplicación**
   ```bash
   gradlew bootRun
   ```
   O ejecutar directamente en tu IDE

6. **Acceder a la aplicación**
   ```
   http://localhost:8080
   ```

## 📱 Uso de la Aplicación

### Para Empresas

1. **Registro**
   - Ir a `/empresa/registro`
   - Llenar el formulario con datos de la empresa
   - Enviar

2. **Login**
   - Ir a `/empresa/login`
   - Ingresar email y contraseña

3. **Crear Oferta de Empleo**
   - Desde el dashboard: "Crear Puesto"
   - Llenar formulario con detalles del puesto
   - Publicar

4. **Agregar Requisitos**
   - En la vista de detalle del puesto
   - Agregar habilidades/características requeridas

### Para Candidatos

1. **Registro**
   - Ir a `/oferente/registro`
   - Llenar formulario con datos personales
   - Registrarse

2. **Login**
   - Ir a `/oferente/login`
   - Ingresar email y contraseña

3. **Actualizar Perfil**
   - Desde el dashboard: "Ver Perfil"
   - Actualizar información profesional

4. **Agregar Habilidades**
   - En el perfil: "Agregar Habilidad"
   - Especificar nombre, tipo y nivel

5. **Subir CV**
   - En el perfil: "Subir CV"
   - Seleccionar archivo (PDF, DOC, DOCX)
   - Establecer como principal si lo desea

### Para Todos

1. **Ver Empleos**
   - Ir a `/puesto/lista`
   - Ver todos los empleos disponibles

2. **Buscar por Ubicación**
   - Usar el formulario de búsqueda
   - Ingresar ciudad

3. **Ver Detalle de Empleo**
   - Hacer clic en cualquier oferta
   - Ver descripción, requisitos y habilidades

## 🔐 Seguridad

- **Contraseñas:** Encriptadas con BCrypt
- **Validación:** Datos validados en cliente y servidor
- **URLs:** Protegidas por Spring Security (implementación personalizada en controladores)
- **Sesiones:** Gestión básica mediante parámetros de URL (mejorable con Spring Security sessions)

## 📝 Notas Importantes

1. **Gestión de Sesiones:** El sistema actual usa parámetros URL para mantener la sesión. Para producción, implementar gestión de sesiones completa con Spring Security

2. **Carga de Archivos:** Los CVs se guardan en el directorio configurado en `application.properties` (propiedad `app.upload.dir`)

3. **Validación:** Implementar anotaciones de validación adicionales (@NotNull, @Email, etc.) en las entidades

4. **Logging:** Configurar logs apropiados en servicios y controladores

5. **Manejo de Excepciones:** Mejorar con @ExceptionHandler global

## 🛠️ Mejoras Futuras

- [ ] Implementar Spring Security sessions completo
- [ ] Agregar notificaciones por email
- [ ] Sistema de postulaciones/aplicaciones
- [ ] Dashboard avanzado de estadísticas
- [ ] Búsqueda avanzada con filtros
- [ ] Sistema de evaluaciones y calificaciones
- [ ] Integración con redes sociales
- [ ] Reportes y análisis
- [ ] API REST adicional
- [ ] Pruebas unitarias e integración

## 📞 Soporte

Para cualquier pregunta o problema, contactar a:
- Email: soporte@bolsaempleo.com
- Teléfono: +55 1234-5678

## 📄 Licencia

Este proyecto está bajo licencia MIT.

---

**Versión:** 1.0.0  
**Última actualización:** 18 de Marzo de 2026
