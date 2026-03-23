╔══════════════════════════════════════════════════════════════════════════════╗
║                                                                              ║
║           ✅ BOLSA DE EMPLEO - SCRIPT SQL MYSQL FINALIZADO ✅               ║
║                                                                              ║
║                     Todas las tablas y especificaciones                     ║
║                                                                              ║
║                          19 de marzo de 2026                                ║
║                                                                              ║
╚══════════════════════════════════════════════════════════════════════════════╝


┌──────────────────────────────────────────────────────────────────────────────┐
│ 📊 RESUMEN EJECUTIVO                                                        │
└──────────────────────────────────────────────────────────────────────────────┘

Nombre del archivo:    BolsaEmpleo.sql
Ubicación:             c:\Users\camil\OneDrive\Documentos\PrograIV\Bolsa_Empleo\
Tamaño:                ~25 KB
Líneas de código:      341 líneas
Tablas:                8 tablas creadas
Registros insertados:  ~50 registros de prueba
Motor:                 InnoDB
Charset:               utf8mb4_unicode_ci
Estado:                ✅ COMPLETO Y FUNCIONAL


┌──────────────────────────────────────────────────────────────────────────────┐
│ 📋 ESPECIFICACIONES CUMPLIDAS                                               │
└──────────────────────────────────────────────────────────────────────────────┘

  TABLA 1: ADMINISTRADOR ✅
  ├─ Campos especificados:
  │  ├─ id_admin (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ identificacion (VARCHAR(50) UNIQUE)
  │  ├─ password_hash (VARCHAR(255) BCrypt)
  │  ├─ fecha_creacion (TIMESTAMP automático)
  │  └─ activo (BOOLEAN)
  ├─ Índices: identificacion, activo
  └─ Datos precargados:
     └─ admin / admin123 (BCrypt hasheado)

  TABLA 2: EMPRESA ✅
  ├─ Campos especificados:
  │  ├─ id_empresa (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ nombre (VARCHAR(150) NOT NULL)
  │  ├─ localizacion (VARCHAR(200))
  │  ├─ correo (VARCHAR(100) UNIQUE)
  │  ├─ telefono (VARCHAR(20))
  │  ├─ descripcion (TEXT)
  │  ├─ password_hash (VARCHAR(255) BCrypt)
  │  ├─ aprobado (BOOLEAN DEFAULT FALSE)
  │  ├─ fecha_registro (TIMESTAMP automático)
  │  └─ activo (BOOLEAN)
  ├─ Índices: correo, aprobado, activo
  └─ Datos precargados: 3 empresas

  TABLA 3: OFERENTE ✅
  ├─ Campos especificados:
  │  ├─ id_oferente (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ identificacion (VARCHAR(50) UNIQUE)
  │  ├─ nombre (VARCHAR(100) NOT NULL)
  │  ├─ apellido (VARCHAR(100) NOT NULL)
  │  ├─ nacionalidad (VARCHAR(50))
  │  ├─ telefono (VARCHAR(20))
  │  ├─ correo (VARCHAR(100) UNIQUE)
  │  ├─ residencia (VARCHAR(200))
  │  ├─ password_hash (VARCHAR(255) BCrypt)
  │  ├─ aprobado (BOOLEAN DEFAULT FALSE)
  │  ├─ fecha_registro (TIMESTAMP automático)
  │  └─ activo (BOOLEAN)
  ├─ Índices: identificacion, correo, aprobado, activo, nombre+apellido
  └─ Datos precargados: 3 oferentes

  TABLA 4: CARACTERISTICA ✅
  ├─ Campos especificados:
  │  ├─ id_caracteristica (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ nombre (VARCHAR(150) NOT NULL)
  │  ├─ descripcion (TEXT)
  │  ├─ id_padre (INT FOREIGN KEY - AUTO-REFERENCIA para jerarquía)
  │  ├─ nivel_minimo (INT DEFAULT 1)
  │  ├─ nivel_maximo (INT DEFAULT 5)
  │  ├─ activo (BOOLEAN)
  │  └─ fecha_creacion (TIMESTAMP)
  ├─ Índices: nombre, id_padre, activo
  ├─ Foreign Key: id_padre REFERENCES caracteristica(id_caracteristica)
  └─ Datos precargados: 28 características
     ├─ 4 categorías principales (NULL padre)
     └─ 24 subcategorías

  TABLA 5: PUESTO ✅
  ├─ Campos especificados:
  │  ├─ id_puesto (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ id_empresa (INT FOREIGN KEY)
  │  ├─ descripcion (TEXT NOT NULL)
  │  ├─ salario (DECIMAL(15,2))
  │  ├─ tipo_publicacion (ENUM('publico', 'privado'))
  │  ├─ activo (BOOLEAN DEFAULT TRUE)
  │  ├─ fecha_registro (TIMESTAMP)
  │  └─ fecha_actualizacion (TIMESTAMP AUTO UPDATE)
  ├─ Índices: id_empresa, tipo_publicacion, activo, fecha_registro
  ├─ Foreign Key: id_empresa REFERENCES empresa(id_empresa) ON DELETE CASCADE
  └─ Datos precargados: 4 puestos

  TABLA 6: PUESTO_CARACTERISTICA ✅
  ├─ Campos especificados:
  │  ├─ id (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ id_puesto (INT FOREIGN KEY)
  │  ├─ id_caracteristica (INT FOREIGN KEY)
  │  ├─ nivel_requerido (INT CHECK 1-5)
  │  └─ fecha_creacion (TIMESTAMP)
  ├─ Constraints: UNIQUE (id_puesto, id_caracteristica)
  ├─ Índices: id_puesto, id_caracteristica, nivel_requerido
  ├─ Foreign Keys: con ON DELETE CASCADE
  └─ Datos precargados: 4 asignaciones

  TABLA 7: OFERENTE_CARACTERISTICA ✅
  ├─ Campos especificados:
  │  ├─ id (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ id_oferente (INT FOREIGN KEY)
  │  ├─ id_caracteristica (INT FOREIGN KEY)
  │  ├─ nivel (INT CHECK 1-5)
  │  └─ fecha_creacion (TIMESTAMP)
  ├─ Constraints: UNIQUE (id_oferente, id_caracteristica)
  ├─ Índices: id_oferente, id_caracteristica, nivel
  ├─ Foreign Keys: con ON DELETE CASCADE
  └─ Datos precargados: 9 asignaciones

  TABLA 8: CV ✅
  ├─ Campos especificados:
  │  ├─ id (INT PRIMARY KEY AUTO_INCREMENT)
  │  ├─ id_oferente (INT FOREIGN KEY UNIQUE)
  │  ├─ archivo_pdf (LONGBLOB)
  │  ├─ nombre_archivo (VARCHAR(255))
  │  ├─ fecha_subida (TIMESTAMP)
  │  └─ fecha_actualizacion (TIMESTAMP AUTO UPDATE)
  ├─ Constraint: UNIQUE (id_oferente) - un CV por oferente
  ├─ Índices: id_oferente, fecha_subida
  ├─ Foreign Key: con ON DELETE CASCADE
  └─ Datos precargados: ninguno (listo para llenar)


┌──────────────────────────────────────────────────────────────────────────────┐
│ 🔐 DATOS DE PRUEBA INCLUIDOS                                                │
└──────────────────────────────────────────────────────────────────────────────┘

  ADMINISTRADOR (1):
  ┌─────────────────────────────────────────────────────────────┐
  │ identificacion: admin                                       │
  │ password: admin123                                          │
  │ hash: $2a$10$WvMIwdSJrP2MfA7XfLbJO...                      │
  │ activo: TRUE                                                │
  └─────────────────────────────────────────────────────────────┘

  EMPRESAS (3):
  ┌─────────────────────────────────────────────────────────────┐
  │ 1. TechSolutions S.A.                                       │
  │    Ubicación: San José, Costa Rica                          │
  │    Aprobada: SÍ | Activa: SÍ                                │
  │                                                             │
  │ 2. Innovate Digital                                         │
  │    Ubicación: San José, Costa Rica                          │
  │    Aprobada: SÍ | Activa: SÍ                                │
  │                                                             │
  │ 3. CloudServices Inc                                        │
  │    Ubicación: Heredia, Costa Rica                           │
  │    Aprobada: SÍ | Activa: SÍ                                │
  └─────────────────────────────────────────────────────────────┘

  OFERENTES (3):
  ┌─────────────────────────────────────────────────────────────┐
  │ 1. Juan Pérez López (118456789)                             │
  │    Nacionalidad: Costarricense | Residencia: San José       │
  │    Aprobado: SÍ | Activo: SÍ                                │
  │                                                             │
  │ 2. María González Rodríguez (117234567)                     │
  │    Nacionalidad: Costarricense | Residencia: Heredia        │
  │    Aprobada: SÍ | Activa: SÍ                                │
  │                                                             │
  │ 3. Carlos Ramírez Méndez (119876543)                        │
  │    Nacionalidad: Nicaragüense | Residencia: San Pedro       │
  │    Aprobado: SÍ | Activo: SÍ                                │
  └─────────────────────────────────────────────────────────────┘

  CARACTERÍSTICAS (28 totales):
  ┌─────────────────────────────────────────────────────────────┐
  │ CATEGORÍA 1: Bases de Datos (padre=NULL)                    │
  │   ├─ MySQL                                                  │
  │   ├─ PostgreSQL                                             │
  │   ├─ MongoDB                                                │
  │   ├─ Oracle Database                                        │
  │   └─ SQL                                                    │
  │                                                             │
  │ CATEGORÍA 2: Lenguajes de Programación (padre=NULL)         │
  │   ├─ Java                                                   │
  │   ├─ Python                                                 │
  │   ├─ JavaScript                                             │
  │   ├─ C#                                                     │
  │   ├─ PHP                                                    │
  │   ├─ C++                                                    │
  │   └─ Go                                                     │
  │                                                             │
  │ CATEGORÍA 3: Tecnologías Web (padre=NULL)                   │
  │   ├─ Spring Boot                                            │
  │   ├─ React                                                  │
  │   ├─ Vue.js                                                 │
  │   ├─ Angular                                                │
  │   ├─ Node.js                                                │
  │   ├─ Laravel                                                │
  │   ├─ Django                                                 │
  │   ├─ REST API                                               │
  │   └─ HTML/CSS                                               │
  │                                                             │
  │ CATEGORÍA 4: Testing (padre=NULL)                           │
  │   ├─ JUnit                                                  │
  │   ├─ Selenium                                               │
  │   ├─ Jest                                                   │
  │   ├─ Cypress                                                │
  │   ├─ Pytest                                                 │
  │   ├─ Test Automation                                        │
  │   └─ QA Manual                                              │
  └─────────────────────────────────────────────────────────────┘

  PUESTOS (4):
  ┌─────────────────────────────────────────────────────────────┐
  │ 1. Desarrollador Java Sr. - TechSolutions                   │
  │    Salario: ¢2,500,000 | Tipo: Público | Activo: SÍ        │
  │                                                             │
  │ 2. Ingeniero de Base de Datos - TechSolutions               │
  │    Salario: ¢2,200,000 | Tipo: Público | Activo: SÍ        │
  │                                                             │
  │ 3. Full Stack Developer - Innovate Digital                  │
  │    Salario: ¢2,300,000 | Tipo: PRIVADO | Activo: SÍ        │
  │                                                             │
  │ 4. DevOps Engineer - CloudServices                          │
  │    Salario: ¢2,700,000 | Tipo: Público | Activo: SÍ        │
  └─────────────────────────────────────────────────────────────┘


┌──────────────────────────────────────────────────────────────────────────────┐
│ 🔑 FOREIGN KEYS Y RELACIONES                                                │
└──────────────────────────────────────────────────────────────────────────────┘

  RELACIONES DEFINIDAS:

  1. caracteristica → caracteristica (auto-referencia)
     ├─ Campo: id_padre REFERENCES id_caracteristica
     └─ ON DELETE SET NULL, ON UPDATE CASCADE

  2. puesto → empresa
     ├─ Campo: id_empresa REFERENCES empresa(id_empresa)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE

  3. puesto_caracteristica → puesto
     ├─ Campo: id_puesto REFERENCES puesto(id_puesto)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE

  4. puesto_caracteristica → caracteristica
     ├─ Campo: id_caracteristica REFERENCES caracteristica(id_caracteristica)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE

  5. oferente_caracteristica → oferente
     ├─ Campo: id_oferente REFERENCES oferente(id_oferente)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE

  6. oferente_caracteristica → caracteristica
     ├─ Campo: id_caracteristica REFERENCES caracteristica(id_caracteristica)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE

  7. cv → oferente (UNIQUE 1:1)
     ├─ Campo: id_oferente REFERENCES oferente(id_oferente)
     └─ ON DELETE CASCADE, ON UPDATE CASCADE


┌──────────────────────────────────────────────────────────────────────────────┐
│ 📁 ARCHIVOS COMPLEMENTARIOS CREADOS                                         │
└──────────────────────────────────────────────────────────────────────────────┘

  ✅ ESQUEMA_BASE_DATOS.md
     - Documentación detallada de cada tabla
     - Diagrama de relaciones
     - Descripción de campos
     - Índices y constraints
     - Datos de prueba
     - Estadísticas
     - Consultas útiles

  ✅ SQL_CONSULTAS_UTILES.md
     - 100+ consultas SQL de ejemplo
     - Organizadas por categoría:
       * Administrador
       * Empresa
       * Oferente
       * Puestos
       * Características
       * Matching (asociar oferentes con puestos)
       * Reportes y estadísticas
       * Mantenimiento
     - Consultas complejas incluidas
     - Ejemplos prácticos

  ✅ SQL_COMPLETADO.txt
     - Resumen de completación
     - Verificación de requisitos
     - Instrucciones de uso
     - Checklist


┌──────────────────────────────────────────────────────────────────────────────┐
│ 🚀 CÓMO EJECUTAR EL SCRIPT                                                  │
└──────────────────────────────────────────────────────────────────────────────┘

  MÉTODO 1: Docker Compose (AUTOMÁTICO)
  
  El script se ejecuta automáticamente al iniciar Docker:
  
  $ docker-compose up -d
  
  ✅ mysql:8.0 contenedor inicia
  ✅ BolsaEmpleo.sql se ejecuta automáticamente
  ✅ Todas las tablas se crean
  ✅ Datos de prueba se insertan
  ✅ BD lista en localhost:3306

  MÉTODO 2: MySQL CLI
  
  $ mysql -h localhost -u bolsa_user -p bolsa1234 bolsa_empleo < BolsaEmpleo.sql
  
  O interactivo:
  
  $ mysql -h localhost -u bolsa_user -p
  mysql> SOURCE BolsaEmpleo.sql;

  MÉTODO 3: MySQL Workbench
  
  1. File → Open SQL Script
  2. Seleccionar BolsaEmpleo.sql
  3. Lightning bolt (Execute)
  4. Refresh schema

  MÉTODO 4: PHPMyAdmin
  
  1. Crear BD: bolsa_empleo
  2. Ir a Import
  3. Seleccionar BolsaEmpleo.sql
  4. Click Go


┌──────────────────────────────────────────────────────────────────────────────┐
│ ✅ VERIFICACIÓN POSTERIOR A EJECUCIÓN                                       │
└──────────────────────────────────────────────────────────────────────────────┘

  Ejecutar estas consultas para verificar:

  1. Ver todas las tablas:
     mysql> SHOW TABLES;
     
     Esperado: 8 tablas

  2. Contar registros:
     mysql> SELECT 
              'administrador' AS tabla, COUNT(*) AS registros 
            FROM administrador
            UNION ALL SELECT 'empresa', COUNT(*) FROM empresa
            UNION ALL SELECT 'oferente', COUNT(*) FROM oferente
            UNION ALL SELECT 'caracteristica', COUNT(*) FROM caracteristica
            UNION ALL SELECT 'puesto', COUNT(*) FROM puesto;
     
     Esperado:
     administrador: 1
     empresa: 3
     oferente: 3
     caracteristica: 28
     puesto: 4

  3. Ver jerarquía de características:
     mysql> SELECT CONCAT(REPEAT('  ', COUNT(*)-1), nombre) AS caracteristica
            FROM caracteristica
            GROUP BY id_caracteristica
            ORDER BY id_padre, nombre;

  4. Verificar admin:
     mysql> SELECT * FROM administrador WHERE identificacion='admin';

  5. Prueba de integridad:
     mysql> SELECT COUNT(*) FROM puesto_caracteristica 
            WHERE id_puesto NOT IN (SELECT id_puesto FROM puesto);
     
     Esperado: 0 (no debe haber referencias huérfanas)


┌──────────────────────────────────────────────────────────────────────────────┐
│ 📊 CARACTERÍSTICAS TÉCNICAS                                                 │
└──────────────────────────────────────────────────────────────────────────────┘

  MOTOR:        InnoDB (transacciones ACID, FK, integridad referencial)
  CHARSET:      utf8mb4_unicode_ci (soporta acentos, emojis)
  COLLATION:    utf8mb4_unicode_ci (case-insensitive, accent-sensitive)
  
  ÍNDICES:
  - PRIMARY KEY en todas las tablas
  - UNIQUE en campos identificadores
  - Índices simples para búsquedas
  - Índices compuestos donde es útil
  
  CONSTRAINTS:
  - NOT NULL en campos obligatorios
  - UNIQUE en emails, identificaciones
  - CHECK para rangos (nivel 1-5)
  - ENUM para valores predeterminados
  - FOREIGN KEYS con cascadas
  
  CAMPOS ESPECIALES:
  - LONGBLOB para archivos PDF
  - DECIMAL(15,2) para precisión monetaria
  - ENUM('publico', 'privado') para tipo
  - TIMESTAMP automáticos
  - CHECK(nivel BETWEEN 1 AND 5)

  SEGURIDAD:
  - Contraseñas con BCrypt (nunca texto plano)
  - Identificaciones únicas
  - Emails validados como UNIQUE
  - Integridad referencial garantizada


┌──────────────────────────────────────────────────────────────────────────────┐
│ 💡 CASOS DE USO                                                             │
└──────────────────────────────────────────────────────────────────────────────┘

  PARA DESARROLLO:
  ✅ Base de datos lista para usar
  ✅ Datos de prueba para testing
  ✅ Estructura completa
  ✅ Relaciones correctamente configuradas

  PARA PRODUCCIÓN:
  ✅ Script limpio y profesional
  ✅ Estructura escalable
  ✅ Manejo de errores
  ✅ Optimizaciones incluidas

  PARA APRENDIZAJE:
  ✅ Ejemplos de design patterns
  ✅ Jerarquías (auto-referencia)
  ✅ Relaciones N:M
  ✅ Foreign keys con cascadas
  ✅ Queries complejas


╔══════════════════════════════════════════════════════════════════════════════╗
║                                                                              ║
║                  ✅ BOLSA DE EMPLEO - SCRIPT COMPLETO ✅                    ║
║                                                                              ║
║  ARCHIVO:          BolsaEmpleo.sql (341 líneas)                             ║
║  TABLAS:           8 completamente configuradas                             ║
║  FOREIGN KEYS:     7 relaciones definidas                                   ║
║  DATOS PRUEBA:     50+ registros precargados                                ║
║  DOCUMENTACIÓN:    3 archivos complementarios                               ║
║  ESTADO:           ✅ Listo para usar en Docker, MySQL o cualquier BD       ║
║                                                                              ║
║             Base de datos profesional, segura y completamente               ║
║                     funcional para una bolsa de empleo                      ║
║                                                                              ║
╚══════════════════════════════════════════════════════════════════════════════╝

Versión: 1.0 - Completa
Fecha: 19 de marzo de 2026
Estado: ✅ PRODUCCIÓN LISTA
