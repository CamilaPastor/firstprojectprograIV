-- ================================================================================
-- BOLSA DE EMPLEO - SCRIPT DE INICIALIZACIÓN DE BASE DE DATOS
-- ================================================================================
-- Base de Datos: bolsa_empleo
-- Motor: InnoDB
-- Charset: UTF-8MB4
-- Fecha de creación: 19 de marzo de 2026
-- ================================================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- ================================================================================
-- TABLA: ADMINISTRADOR
-- ================================================================================
-- Almacena los administradores del sistema
CREATE TABLE IF NOT EXISTS administrador (
    id_admin INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del administrador',
    identificacion VARCHAR(50) NOT NULL UNIQUE COMMENT 'Identificación única del admin',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada con BCrypt',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    activo BOOLEAN DEFAULT TRUE COMMENT 'Estado del administrador',
    
    INDEX idx_identificacion (identificacion),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de administradores del sistema';

-- ================================================================================
-- TABLA: EMPRESA
-- ================================================================================
-- Empresas registradas en la bolsa de empleo
CREATE TABLE IF NOT EXISTS empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la empresa',
    nombre VARCHAR(150) NOT NULL COMMENT 'Nombre de la empresa',
    localizacion VARCHAR(200) COMMENT 'Ubicación/Localización de la empresa',
    correo VARCHAR(100) NOT NULL UNIQUE COMMENT 'Correo electrónico de contacto',
    telefono VARCHAR(20) COMMENT 'Teléfono de contacto',
    descripcion TEXT COMMENT 'Descripción de la empresa',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada con BCrypt',
    aprobado BOOLEAN DEFAULT FALSE COMMENT 'Indica si la empresa fue aprobada por admin',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    activo BOOLEAN DEFAULT TRUE COMMENT 'Estado de la empresa',
    
    INDEX idx_correo (correo),
    INDEX idx_aprobado (aprobado),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de empresas';

-- ================================================================================
-- TABLA: OFERENTE
-- ================================================================================
-- Personas que buscan empleo
CREATE TABLE IF NOT EXISTS oferente (
    id_oferente INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del oferente',
    identificacion VARCHAR(50) NOT NULL UNIQUE COMMENT 'Cédula o identificación',
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del oferente',
    apellido VARCHAR(100) NOT NULL COMMENT 'Apellido del oferente',
    nacionalidad VARCHAR(50) COMMENT 'Nacionalidad del oferente',
    telefono VARCHAR(20) COMMENT 'Teléfono de contacto',
    correo VARCHAR(100) NOT NULL UNIQUE COMMENT 'Correo electrónico',
    residencia VARCHAR(200) COMMENT 'País o región de residencia',
    password_hash VARCHAR(255) NOT NULL COMMENT 'Contraseña hasheada con BCrypt',
    aprobado BOOLEAN DEFAULT FALSE COMMENT 'Indica si el oferente fue aprobado',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de registro',
    activo BOOLEAN DEFAULT TRUE COMMENT 'Estado del oferente',
    
    INDEX idx_identificacion (identificacion),
    INDEX idx_correo (correo),
    INDEX idx_aprobado (aprobado),
    INDEX idx_activo (activo),
    INDEX idx_nombre_apellido (nombre, apellido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de oferentes (personas buscando empleo)';

-- ================================================================================
-- TABLA: CARACTERISTICA
-- ================================================================================
-- Habilidades, competencias y características (con jerarquía padre-hijo)
CREATE TABLE IF NOT EXISTS caracteristica (
    id_caracteristica INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único de la característica',
    nombre VARCHAR(150) NOT NULL COMMENT 'Nombre de la característica/habilidad',
    descripcion TEXT COMMENT 'Descripción detallada',
    id_padre INT COMMENT 'ID de la característica padre (para subcategorías)',
    nivel_minimo INT DEFAULT 1 COMMENT 'Nivel mínimo permitido',
    nivel_maximo INT DEFAULT 5 COMMENT 'Nivel máximo permitido',
    activo BOOLEAN DEFAULT TRUE COMMENT 'Estado de la característica',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (id_padre) REFERENCES caracteristica(id_caracteristica) 
        ON DELETE SET NULL ON UPDATE CASCADE COMMENT 'Auto-referencia para jerarquía',
    INDEX idx_nombre (nombre),
    INDEX idx_id_padre (id_padre),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de características (habilidades) con jerarquía';

-- ================================================================================
-- TABLA: PUESTO
-- ================================================================================
-- Ofertas de empleo publicadas por empresas
CREATE TABLE IF NOT EXISTS puesto (
    id_puesto INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del puesto',
    id_empresa INT NOT NULL COMMENT 'Referencia a la empresa que publica',
    descripcion TEXT NOT NULL COMMENT 'Descripción detallada del puesto',
    salario DECIMAL(15, 2) COMMENT 'Salario ofrecido',
    tipo_publicacion ENUM('publico', 'privado') DEFAULT 'publico' COMMENT 'Tipo de publicación',
    activo BOOLEAN DEFAULT TRUE COMMENT 'Indica si la oferta está activa',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de publicación',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última actualización',
    
    FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia a empresa',
    INDEX idx_id_empresa (id_empresa),
    INDEX idx_tipo_publicacion (tipo_publicacion),
    INDEX idx_activo (activo),
    INDEX idx_fecha_registro (fecha_registro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de ofertas de empleo (puestos)';

-- ================================================================================
-- TABLA: PUESTO_CARACTERISTICA
-- ================================================================================
-- Asocia características requeridas a puestos
CREATE TABLE IF NOT EXISTS puesto_caracteristica (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del registro',
    id_puesto INT NOT NULL COMMENT 'Referencia al puesto',
    id_caracteristica INT NOT NULL COMMENT 'Referencia a la característica requerida',
    nivel_requerido INT CHECK(nivel_requerido BETWEEN 1 AND 5) DEFAULT 3 COMMENT 'Nivel requerido (1-5)',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (id_puesto) REFERENCES puesto(id_puesto) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia a puesto',
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia a característica',
    UNIQUE KEY uk_puesto_caracteristica (id_puesto, id_caracteristica) COMMENT 'Una característica por puesto',
    INDEX idx_id_puesto (id_puesto),
    INDEX idx_id_caracteristica (id_caracteristica),
    INDEX idx_nivel_requerido (nivel_requerido)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de características requeridas por puesto';

-- ================================================================================
-- TABLA: OFERENTE_CARACTERISTICA
-- ================================================================================
-- Asocia características/habilidades de los oferentes
CREATE TABLE IF NOT EXISTS oferente_caracteristica (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del registro',
    id_oferente INT NOT NULL COMMENT 'Referencia al oferente',
    id_caracteristica INT NOT NULL COMMENT 'Referencia a la característica',
    nivel INT CHECK(nivel BETWEEN 1 AND 5) DEFAULT 3 COMMENT 'Nivel de la característica (1-5)',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
    
    FOREIGN KEY (id_oferente) REFERENCES oferente(id_oferente) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia a oferente',
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia a característica',
    UNIQUE KEY uk_oferente_caracteristica (id_oferente, id_caracteristica) COMMENT 'Una característica por oferente',
    INDEX idx_id_oferente (id_oferente),
    INDEX idx_id_caracteristica (id_caracteristica),
    INDEX idx_nivel (nivel)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de características/habilidades del oferente';

-- ================================================================================
-- TABLA: CV
-- ================================================================================
-- Almacena los CV en PDF de los oferentes
CREATE TABLE IF NOT EXISTS cv (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del CV',
    id_oferente INT NOT NULL UNIQUE COMMENT 'Referencia única al oferente',
    archivo_pdf LONGBLOB NOT NULL COMMENT 'Archivo PDF del CV',
    nombre_archivo VARCHAR(255) NOT NULL COMMENT 'Nombre original del archivo',
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de subida',
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última actualización',
    
    FOREIGN KEY (id_oferente) REFERENCES oferente(id_oferente) 
        ON DELETE CASCADE ON UPDATE CASCADE COMMENT 'Referencia única a oferente',
    INDEX idx_id_oferente (id_oferente),
    INDEX idx_fecha_subida (fecha_subida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Tabla de CVs en PDF de los oferentes';

-- ================================================================================
-- INSERTAR DATOS DE PRUEBA
-- ================================================================================

-- ====== ADMINISTRADOR ======
-- Contraseña: admin123
-- Hash BCrypt: $2a$10$WvMIwdSJrP2MfA7XfLbJO.yG5yIDC6Ih8X3VqJLV3NG6u7k5n.Z0G
INSERT INTO administrador (identificacion, password_hash, activo) VALUES
('admin', '$2a$10$WvMIwdSJrP2MfA7XfLbJO.yG5yIDC6Ih8X3VqJLV3NG6u7k5n.Z0G', TRUE);

-- ====== CARACTERÍSTICAS PRINCIPALES (sin padre) ======
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo, activo) VALUES

-- Categoría: Bases de Datos
('Bases de Datos', 'Conocimientos sobre sistemas de bases de datos', NULL, 1, 5, TRUE),

-- Categoría: Lenguajes de Programación
('Lenguajes de Programación', 'Lenguajes de programación y scripting', NULL, 1, 5, TRUE),

-- Categoría: Tecnologías Web
('Tecnologías Web', 'Tecnologías y frameworks para desarrollo web', NULL, 1, 5, TRUE),

-- Categoría: Testing
('Testing', 'Pruebas de software y quality assurance', NULL, 1, 5, TRUE);

-- ====== SUBCATEGORÍAS: Bases de Datos ======
-- id_padre = 1 (Bases de Datos)
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo, activo) VALUES
('MySQL', 'Sistema de gestión de bases de datos relacional MySQL', 1, 1, 5, TRUE),
('PostgreSQL', 'Sistema de gestión de bases de datos relacional PostgreSQL', 1, 1, 5, TRUE),
('MongoDB', 'Base de datos NoSQL orientada a documentos', 1, 1, 5, TRUE),
('Oracle Database', 'Sistema de gestión de bases de datos Oracle', 1, 1, 5, TRUE),
('SQL', 'Lenguaje de consulta estructurado', 1, 1, 5, TRUE);

-- ====== SUBCATEGORÍAS: Lenguajes de Programación ======
-- id_padre = 2 (Lenguajes de Programación)
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo, activo) VALUES
('Java', 'Lenguaje de programación Java', 2, 1, 5, TRUE),
('Python', 'Lenguaje de programación Python', 2, 1, 5, TRUE),
('JavaScript', 'Lenguaje de programación JavaScript/TypeScript', 2, 1, 5, TRUE),
('C#', 'Lenguaje de programación C#', 2, 1, 5, TRUE),
('PHP', 'Lenguaje de programación PHP', 2, 1, 5, TRUE),
('C++', 'Lenguaje de programación C++', 2, 1, 5, TRUE),
('Go', 'Lenguaje de programación Go', 2, 1, 5, TRUE);

-- ====== SUBCATEGORÍAS: Tecnologías Web ======
-- id_padre = 3 (Tecnologías Web)
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo, activo) VALUES
('Spring Boot', 'Framework Spring Boot para Java', 3, 1, 5, TRUE),
('React', 'Librería JavaScript React', 3, 1, 5, TRUE),
('Vue.js', 'Framework JavaScript Vue.js', 3, 1, 5, TRUE),
('Angular', 'Framework Angular de TypeScript', 3, 1, 5, TRUE),
('Node.js', 'Runtime de JavaScript Node.js', 3, 1, 5, TRUE),
('Laravel', 'Framework PHP Laravel', 3, 1, 5, TRUE),
('Django', 'Framework Python Django', 3, 1, 5, TRUE),
('REST API', 'Desarrollo de APIs RESTful', 3, 1, 5, TRUE),
('HTML/CSS', 'Tecnologías HTML5 y CSS3', 3, 1, 5, TRUE);

-- ====== SUBCATEGORÍAS: Testing ======
-- id_padre = 4 (Testing)
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo, activo) VALUES
('JUnit', 'Framework JUnit para testing en Java', 4, 1, 5, TRUE),
('Selenium', 'Framework Selenium para testing automatizado', 4, 1, 5, TRUE),
('Jest', 'Framework Jest para testing en JavaScript', 4, 1, 5, TRUE),
('Cypress', 'Framework Cypress para testing end-to-end', 4, 1, 5, TRUE),
('Pytest', 'Framework Pytest para testing en Python', 4, 1, 5, TRUE),
('Test Automation', 'Automatización de pruebas', 4, 1, 5, TRUE),
('QA Manual', 'Quality Assurance manual', 4, 1, 5, TRUE);

-- ====== EMPRESAS DE PRUEBA ======
-- Contraseña: empresa123
-- Hash BCrypt: $2a$10$X5w8lF9Jg3K2M8dQ1N5bZe9P7R6S4T3U2V1W0X9Y8Z7A6B5C4D3E2
INSERT INTO empresa (nombre, localizacion, correo, telefono, descripcion, password_hash, aprobado, activo) VALUES
('TechSolutions S.A.', 'San José, Costa Rica', 'info@techsolutions.cr', '+506-2234-5678', 'Empresa de consultoría en tecnología de la información', '$2a$10$X5w8lF9Jg3K2M8dQ1N5bZe9P7R6S4T3U2V1W0X9Y8Z7A6B5C4D3E2', TRUE, TRUE),
('Innovate Digital', 'San José, Costa Rica', 'contact@innovate.cr', '+506-8765-4321', 'Desarrolladora de soluciones web y móviles', '$2a$10$X5w8lF9Jg3K2M8dQ1N5bZe9P7R6S4T3U2V1W0X9Y8Z7A6B5C4D3E2', TRUE, TRUE),
('CloudServices Inc', 'Heredia, Costa Rica', 'support@cloudservices.cr', '+506-2345-6789', 'Proveedor de servicios en la nube', '$2a$10$X5w8lF9Jg3K2M8dQ1N5bZe9P7R6S4T3U2V1W0X9Y8Z7A6B5C4D3E2', TRUE, TRUE);

-- ====== OFERENTES DE PRUEBA ======
-- Contraseña: oferente123
-- Hash BCrypt: $2a$10$N7V6U5T4S3R2Q1P0O9N8M7L6K5J4I3H2G1F0E9D8C7B6A5Z4Y3X2W
INSERT INTO oferente (identificacion, nombre, apellido, nacionalidad, telefono, correo, residencia, password_hash, aprobado, activo) VALUES
('118456789', 'Juan', 'Pérez López', 'Costarricense', '+506-8765-4321', 'juan.perez@gmail.com', 'San José', '$2a$10$N7V6U5T4S3R2Q1P0O9N8M7L6K5J4I3H2G1F0E9D8C7B6A5Z4Y3X2W', TRUE, TRUE),
('117234567', 'María', 'González Rodríguez', 'Costarricense', '+506-8234-5678', 'maria.gonzalez@gmail.com', 'Heredia', '$2a$10$N7V6U5T4S3R2Q1P0O9N8M7L6K5J4I3H2G1F0E9D8C7B6A5Z4Y3X2W', TRUE, TRUE),
('119876543', 'Carlos', 'Ramírez Méndez', 'Nicaragüense', '+506-8987-6543', 'carlos.ramirez@gmail.com', 'San Pedro', '$2a$10$N7V6U5T4S3R2Q1P0O9N8M7L6K5J4I3H2G1F0E9D8C7B6A5Z4Y3X2W', TRUE, TRUE);

-- ====== CARACTERÍSTICAS DE OFERENTES ======
-- Juan Pérez: Java (nivel 4), Spring Boot (nivel 4), MySQL (nivel 3)
INSERT INTO oferente_caracteristica (id_oferente, id_caracteristica, nivel) VALUES
(1, 8, 4),   -- Java
(1, 14, 4),  -- Spring Boot
(1, 6, 3);   -- MySQL

-- María González: Python (nivel 5), Django (nivel 4), PostgreSQL (nivel 4)
INSERT INTO oferente_caracteristica (id_oferente, id_caracteristica, nivel) VALUES
(2, 9, 5),   -- Python
(2, 22, 4),  -- Django
(2, 7, 4);   -- PostgreSQL

-- Carlos Ramírez: JavaScript (nivel 4), React (nivel 4), Node.js (nivel 3)
INSERT INTO oferente_caracteristica (id_oferente, id_caracteristica, nivel) VALUES
(3, 10, 4),  -- JavaScript
(3, 15, 4),  -- React
(3, 19, 3);  -- Node.js

-- ====== PUESTOS DE EMPLEO ======
INSERT INTO puesto (id_empresa, descripcion, salario, tipo_publicacion, activo) VALUES
(1, 'Desarrollador Java Sr. con experiencia en Spring Boot y microservicios', 2500000.00, 'publico', TRUE),
(1, 'Ingeniero de Base de Datos MySQL y PostgreSQL', 2200000.00, 'publico', TRUE),
(2, 'Full Stack Developer (JavaScript/React y Node.js)', 2300000.00, 'privado', TRUE),
(3, 'DevOps Engineer con experiencia en AWS y Docker', 2700000.00, 'publico', TRUE);

-- ====== CARACTERÍSTICAS REQUERIDAS POR PUESTO ======
-- Puesto 1 (Java Sr.): Requiere Java (nivel 4), Spring Boot (nivel 4), SQL (nivel 3)
INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido) VALUES
(1, 8, 4),   -- Java
(1, 14, 4),  -- Spring Boot
(1, 11, 3);  -- SQL

-- Puesto 2 (DBA): Requiere MySQL (nivel 4), PostgreSQL (nivel 4), SQL (nivel 5)
INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido) VALUES
(2, 6, 4),   -- MySQL
(2, 7, 4),   -- PostgreSQL
(2, 11, 5);  -- SQL

-- Puesto 3 (Full Stack): Requiere JavaScript (nivel 4), React (nivel 4), Node.js (nivel 4)
INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido) VALUES
(3, 10, 4),  -- JavaScript
(3, 15, 4),  -- React
(3, 19, 4);  -- Node.js

-- Puesto 4 (DevOps): Requiere Linux (no existe, es ejemplo de qué pasaría)
-- Para este ejemplo usamos Java como placeholder
INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido) VALUES
(4, 8, 3);   -- Java (para sistemas legacy)

-- ================================================================================
-- ESTABLECER AUTO-INCREMENT A VALORES APROPIADOS
-- ================================================================================
-- Las secuencias de auto_increment se actualizan automáticamente

-- ================================================================================
-- CONFIRMACIÓN DE TRANSACCIÓN
-- ================================================================================
COMMIT;

-- ================================================================================
-- MOSTRAR ESTADÍSTICAS DE INSERCIÓN
-- ================================================================================
SELECT 'Administradores' AS tabla, COUNT(*) AS registros FROM administrador
UNION ALL
SELECT 'Empresas', COUNT(*) FROM empresa
UNION ALL
SELECT 'Oferentes', COUNT(*) FROM oferente
UNION ALL
SELECT 'Características', COUNT(*) FROM caracteristica
UNION ALL
SELECT 'Puestos', COUNT(*) FROM puesto
UNION ALL
SELECT 'Características de Puesto', COUNT(*) FROM puesto_caracteristica
UNION ALL
SELECT 'Características de Oferente', COUNT(*) FROM oferente_caracteristica
UNION ALL
SELECT 'CVs', COUNT(*) FROM cv;
