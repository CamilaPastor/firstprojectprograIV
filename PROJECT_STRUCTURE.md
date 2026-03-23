# Estructura Completa del Proyecto Bolsa de Empleo

## 📁 Árbol del Proyecto

```
Bolsa_Empleo/
│
├── 📄 build.gradle                          # Configuración de Gradle con dependencias
├── 📄 settings.gradle                       # Configuración de módulos
├── 📄 gradlew                              # Gradle Wrapper (Linux/Mac)
├── 📄 gradlew.bat                          # Gradle Wrapper (Windows)
├── 📄 HELP.md                              # Ayuda de Spring Boot
│
├── 📄 README.md                            # 📚 Documentación principal
├── 📄 DEVELOPMENT.md                       # 📚 Guía para desarrolladores
├── 📄 USAGE_GUIDE.md                       # 📚 Guía de uso y casos de ejemplo
├── 📄 IMPLEMENTATION_SUMMARY.md            # 📚 Resumen de implementación
├── 📄 DATABASE_SETUP.md                    # 📚 Configuración de BD
│
├── 📄 database.sql                         # 🗄️ Script SQL para Oracle
├── 📄 Bolsa_Empleo.iml                     # Configuración IDE
│
├── 📁 gradle/
│   └── 📁 wrapper/
│       ├── 📄 gradle-wrapper.jar
│       └── 📄 gradle-wrapper.properties
│
├── 📁 src/
│   │
│   ├── 📁 main/
│   │   │
│   │   ├── 📁 java/
│   │   │   └── 📁 com/bolsaempleo/
│   │   │       │
│   │   │       ├── 📁 config/                  🔧 CONFIGURACIÓN
│   │   │       │   ├── SecurityConfig.java     - Encriptación BCrypt
│   │   │       │   └── WebConfig.java          - Mapeo de recursos estáticos
│   │   │       │
│   │   │       ├── 📁 controller/              🎮 CONTROLADORES (27+ endpoints)
│   │   │       │   ├── HomeController.java
│   │   │       │   ├── EmpresaController.java
│   │   │       │   ├── OferenteController.java
│   │   │       │   ├── PuestoController.java
│   │   │       │   ├── CaracteristicaController.java
│   │   │       │   └── CVController.java
│   │   │       │
│   │   │       ├── 📁 model/                   📦 ENTIDADES
│   │   │       │   ├── Empresa.java
│   │   │       │   ├── Oferente.java
│   │   │       │   ├── Administrador.java
│   │   │       │   ├── Puesto.java
│   │   │       │   ├── Caracteristica.java
│   │   │       │   └── CV.java
│   │   │       │
│   │   │       ├── 📁 repository/              🗄️ REPOSITORIOS
│   │   │       │   ├── EmpresaRepository.java
│   │   │       │   ├── OferenteRepository.java
│   │   │       │   ├── AdministradorRepository.java
│   │   │       │   ├── PuestoRepository.java
│   │   │       │   ├── CaracteristicaRepository.java
│   │   │       │   └── CVRepository.java
│   │   │       │
│   │   │       └── 📁 service/                 💼 SERVICIOS
│   │   │           ├── EmpresaService.java
│   │   │           ├── OferenteService.java
│   │   │           ├── PuestoService.java
│   │   │           ├── CaracteristicaService.java
│   │   │           └── CVService.java
│   │   │
│   │   └── 📁 resources/
│   │       │
│   │       ├── 📄 application.properties      ⚙️ Configuración de la aplicación
│   │       │
│   │       ├── 📁 templates/                  🎨 VISTAS HTML (18 archivos)
│   │       │   ├── 📄 index.html              - Página de inicio
│   │       │   ├── 📄 acerca.html             - Acerca de
│   │       │   ├── 📄 contacto.html           - Contacto
│   │       │   │
│   │       │   ├── 📁 empresa/
│   │       │   │   ├── registro.html
│   │       │   │   ├── login.html
│   │       │   │   ├── dashboard.html
│   │       │   │   └── perfil.html
│   │       │   │
│   │       │   ├── 📁 oferente/
│   │       │   │   ├── registro.html
│   │       │   │   ├── login.html
│   │       │   │   ├── dashboard.html
│   │       │   │   └── perfil.html
│   │       │   │
│   │       │   ├── 📁 puesto/
│   │       │   │   ├── lista.html
│   │       │   │   ├── detalle.html
│   │       │   │   ├── crear.html
│   │       │   │   └── editar.html
│   │       │   │
│   │       │   ├── 📁 caracteristica/
│   │       │   │   ├── agregar.html
│   │       │   │   └── agregar-habilidad.html
│   │       │   │
│   │       │   └── 📁 cv/
│   │       │       ├── subir.html
│   │       │       └── listar.html
│   │       │
│   │       └── 📁 static/
│   │           └── 📁 css/
│   │               └── 📄 style.css            🎨 Estilos responsivos
│   │
│   └── 📁 test/
│       └── 📁 java/
│           └── 📁 com/bolsaempleo/
│               └── BolsaEmpleoApplicationTests.java
│
└── 📁 uploads/                              📂 Directorio para CVs cargados
    └── cv/                                  (Creado automáticamente)
```

---

## 🔗 Relaciones de Entidades

```
┌──────────────────────────────────────────────────────────────┐
│                    DIAGRAMA ER SIMPLIFICADO                  │
└──────────────────────────────────────────────────────────────┘

    ┌─────────────────┐
    │    EMPRESA      │
    ├─────────────────┤
    │ EMPRESA_ID  [PK]│──────┐
    │ NOMBRE          │      │
    │ EMAIL           │      │ 1:N
    │ PASSWORD        │      │
    │ RFC             │      │
    └─────────────────┘      │
                             │
                             ↓
                    ┌─────────────────┐
                    │     PUESTO      │
                    ├─────────────────┤
                    │ PUESTO_ID   [PK]│
                    │ TITULO          │
                    │ DESCRIPCION     │
                    │ EMPRESA_ID  [FK]│
                    │ ACTIVO          │
                    └─────────────────┘
                             ↑
                             │ 1:N
                             │
        ┌────────────────────┴────────────────────┐
        │                                         │
        ▼                                         ▼
┌──────────────────────┐           ┌──────────────────────┐
│  CARACTERISTICA      │           │   CARACTERISTICA     │
├──────────────────────┤           ├──────────────────────┤
│CARACTERISTICA_ID [PK]│           │CARACTERISTICA_ID [PK]│
│ NOMBRE               │           │ NOMBRE               │
│ TIPO                 │           │ TIPO                 │
│ NIVEL                │           │ NIVEL                │
│ PUESTO_ID       [FK] │           │ OFERENTE_ID     [FK] │
└──────────────────────┘           └──────────────────────┘
                                            ↑
                                            │ 1:N
                                            │
                                   ┌─────────────────┐
                                   │   OFERENTE      │
                                   ├─────────────────┤
                                   │OFERENTE_ID  [PK]│──────┐
                                   │ NOMBRE          │      │
                                   │ EMAIL           │      │
                                   │ PASSWORD        │      │ 1:N
                                   │ PROFESION       │      │
                                   └─────────────────┘      │
                                                            │
                                                            ↓
                                                   ┌─────────────────┐
                                                   │       CV        │
                                                   ├─────────────────┤
                                                   │ CV_ID       [PK]│
                                                   │ NOMBRE_ARCHIVO  │
                                                   │ RUTA_ARCHIVO    │
                                                   │ OFERENTE_ID [FK]│
                                                   │ PRINCIPAL       │
                                                   └─────────────────┘
```

---

## 📊 Estadísticas de Archivos

| Categoría | Cantidad | Líneas |
|-----------|----------|---------|
| **Entidades Java** | 6 | 350 |
| **Repositorios** | 6 | 150 |
| **Servicios** | 5 | 800 |
| **Controladores** | 6 | 900 |
| **Vistas HTML** | 18 | 2,500 |
| **CSS** | 1 | 800 |
| **Configuración** | 2 | 150 |
| **SQL** | 1 | 200 |
| **Documentación** | 5 | 3,000 |
| **TOTAL** | **50+** | **~9,000** |

---

## 🔀 Flujo de Datos

```
                    USUARIO EXTERNO
                          ↓
                     HTTP REQUEST
                          ↓
        ┌──────────────────────────────────┐
        │    SPRING DISPATCHER SERVLET     │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    CONTROLLER (URL MAPPING)      │
        │  - EmpresaController             │
        │  - OferenteController            │
        │  - PuestoController              │
        │  - etc.                          │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    SERVICE (BUSINESS LOGIC)      │
        │  - EmpresaService                │
        │  - OferenteService               │
        │  - PuestoService                 │
        │  - etc.                          │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    REPOSITORY (DATA ACCESS)      │
        │  - JpaRepository                 │
        │  - Query methods                 │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    HIBERNATE / JPA               │
        │  - ORM Mapping                   │
        │  - SQL Generation                │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    ORACLE JDBC DRIVER            │
        │  - Connection Pool               │
        │  - SQL Execution                 │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    ORACLE DATABASE               │
        │  - Tables                        │
        │  - Sequences                     │
        │  - Indexes                       │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    RESPONSE PROCESSING           │
        │  - Service converts to Model     │
        │  - Controller passes to View     │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    THYMELEAF TEMPLATE ENGINE     │
        │  - Render HTML                   │
        │  - Variable interpolation        │
        │  - Control structures            │
        └──────────────────────────────────┘
                          ↓
        ┌──────────────────────────────────┐
        │    HTML RESPONSE                 │
        │  + CSS Styling                   │
        │  + Client-side elements          │
        └──────────────────────────────────┘
                          ↓
                   HTTP RESPONSE
                          ↓
                    NAVEGADOR
```

---

## 🛠️ Herramientas y Tecnologías

```
┌─────────────────────────────────────────────────────────────┐
│              STACK TECNOLÓGICO                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ BACKEND:                                                    │
│  • Java 17                                                 │
│  • Spring Boot 4.0.3                                       │
│  • Spring Data JPA                                         │
│  • Spring Security (BCrypt)                                │
│  • Hibernate ORM                                           │
│  • Lombok                                                  │
│                                                             │
│ DATABASE:                                                   │
│  • Oracle Database (11g+)                                  │
│  • OJDBC 11 Driver                                         │
│                                                             │
│ FRONTEND:                                                   │
│  • HTML5                                                   │
│  • CSS3 (Responsive)                                       │
│  • Thymeleaf 3.0+                                          │
│                                                             │
│ BUILD TOOL:                                                │
│  • Gradle 7.0+                                             │
│                                                             │
│ IDE RECOMENDADOS:                                           │
│  • IntelliJ IDEA (Community/Ultimate)                      │
│  • Eclipse IDE                                             │
│  • VS Code (con extensiones)                               │
│                                                             │
│ TESTING:                                                    │
│  • JUnit 5                                                 │
│  • Mockito                                                 │
│  • Spring Boot Test                                        │
│                                                             │
│ VERSION CONTROL:                                            │
│  • Git                                                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 Dependencias Principal (build.gradle)

```gradle
dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'           ✓
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'      ✓
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'     ✓
    implementation 'org.springframework.boot:spring-boot-starter-validation'    ✓
    implementation 'org.springframework.boot:spring-boot-starter-security'      ✓
    
    // Database Driver
    runtimeOnly 'com.oracle.database.jdbc:ojdbc11'                            ✓
    
    // Utilities
    compileOnly 'org.projectlombok:lombok'                                     ✓
    annotationProcessor 'org.projectlombok:lombok'                             ✓
    
    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'      ✓
}
```

---

## 🎯 Puntos de Entrada Principales

### 1. Aplicación
```java
com.bolsaempleo.BolsaEmpleoApplication.main()
```

### 2. Controladores Base
- `http://localhost:8080/` → HomeController
- `http://localhost:8080/empresa/...` → EmpresaController
- `http://localhost:8080/oferente/...` → OferenteController
- `http://localhost:8080/puesto/...` → PuestoController
- `http://localhost:8080/caracteristica/...` → CaracteristicaController
- `http://localhost:8080/cv/...` → CVController

### 3. Configuración
- `application.properties` → Propiedades globales
- `SecurityConfig.java` → Configuración de seguridad
- `WebConfig.java` → Configuración web

---

## 🚀 Scripts útiles

```bash
# Compilar proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Correr la aplicación
./gradlew bootRun

# Generar JAR
./gradlew bootJar

# Limpiar
./gradlew clean

# Ver dependencias
./gradlew dependencies
```

---

## 📝 Notas Importantes

1. **Contraseñas:** Todas están encriptadas con BCrypt (no guardar en texto plano)
2. **Archivos:** Los CVs se guardan en `uploads/cv/` (crear directorio si no existe)
3. **Sesiones:** Implementadas con parámetros URL (mejorable con Sessions)
4. **Base de Datos:** Oracle 11g+ requerido (no compatibles versiones anteriores)

---

**Actualizado:** 18 de Marzo de 2026
**Versión:** 1.0.0
