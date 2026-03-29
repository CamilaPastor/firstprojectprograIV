-- ============================================================
-- Bolsa de Empleo - Script PostgreSQL
-- EIF209 Programacion IV - Universidad Nacional de Costa Rica
-- ============================================================

-- Crear tablas si no existen
CREATE TABLE IF NOT EXISTS administrador (
    id_admin     SERIAL PRIMARY KEY,
    identificacion VARCHAR(50) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo         BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS empresa (
    id_empresa    SERIAL PRIMARY KEY,
    nombre        VARCHAR(200) NOT NULL,
    localizacion  VARCHAR(300),
    correo        VARCHAR(150) NOT NULL UNIQUE,
    telefono      VARCHAR(20),
    descripcion   TEXT,
    password_hash VARCHAR(255),
    aprobado      BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo        BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS oferente (
    id_oferente    SERIAL PRIMARY KEY,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre         VARCHAR(100) NOT NULL,
    apellido       VARCHAR(100) NOT NULL,
    nacionalidad   VARCHAR(100),
    telefono       VARCHAR(20),
    correo         VARCHAR(150) NOT NULL UNIQUE,
    residencia     VARCHAR(200),
    password_hash  VARCHAR(255),
    aprobado       BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo         BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS cv (
    id             SERIAL PRIMARY KEY,
    id_oferente    INTEGER NOT NULL UNIQUE REFERENCES oferente(id_oferente),
    archivo_pdf    BYTEA NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    fecha_subida   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS caracteristica (
    id_caracteristica SERIAL PRIMARY KEY,
    nombre            VARCHAR(200) NOT NULL,
    descripcion       TEXT,
    id_padre          INTEGER REFERENCES caracteristica(id_caracteristica),
    nivel_minimo      INTEGER DEFAULT 1,
    nivel_maximo      INTEGER DEFAULT 5,
    activo            BOOLEAN DEFAULT TRUE,
    fecha_creacion    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS puesto (
    id_puesto         SERIAL PRIMARY KEY,
    id_empresa        INTEGER NOT NULL REFERENCES empresa(id_empresa),
    descripcion       TEXT NOT NULL,
    salario           DECIMAL(10,2),
    tipo_publicacion  VARCHAR(20) DEFAULT 'publico',
    activo            BOOLEAN DEFAULT TRUE,
    fecha_registro    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS puesto_caracteristica (
    id                SERIAL PRIMARY KEY,
    id_puesto         INTEGER NOT NULL REFERENCES puesto(id_puesto),
    id_caracteristica INTEGER NOT NULL REFERENCES caracteristica(id_caracteristica),
    nivel_requerido   INTEGER DEFAULT 3,
    fecha_creacion    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS oferente_caracteristica (
    id                SERIAL PRIMARY KEY,
    id_oferente       INTEGER NOT NULL REFERENCES oferente(id_oferente),
    id_caracteristica INTEGER NOT NULL REFERENCES caracteristica(id_caracteristica),
    nivel             INTEGER DEFAULT 3,
    fecha_creacion    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Datos de prueba
-- ============================================================

-- Administrador (contraseña: admin123)
INSERT INTO administrador (identificacion, password_hash, activo) VALUES
('admin', '$2a$10$Kh.1ejy2HCRpTrQAZoi2KuW5kwlNDofTWTIfcrKb6.DD9oRI8MM16', TRUE)
ON CONFLICT (identificacion) DO UPDATE SET password_hash = EXCLUDED.password_hash, activo = TRUE;

-- Empresas aprobadas (contraseña: empresa123)
INSERT INTO empresa (nombre, localizacion, correo, telefono, descripcion, password_hash, aprobado, activo) VALUES
('TechSolutions S.A.', 'San José, Costa Rica', 'info@techsolutions.cr', '+506-2234-5678', 'Empresa de consultoría en tecnología de la información', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE),
('Innovate Digital', 'San José, Costa Rica', 'contact@innovate.cr', '+506-8765-4321', 'Desarrolladora de soluciones web y móviles', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE),
('CloudServices Inc', 'Heredia, Costa Rica', 'support@cloudservices.cr', '+506-2345-6789', 'Proveedor de servicios en la nube', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE)
ON CONFLICT (correo) DO UPDATE SET password_hash = EXCLUDED.password_hash, aprobado = TRUE, activo = TRUE;

-- Oferentes aprobados (contraseña: oferente123)
INSERT INTO oferente (identificacion, nombre, apellido, nacionalidad, telefono, correo, residencia, password_hash, aprobado, activo) VALUES
('118456789', 'Juan', 'Pérez López', 'Costarricense', '+506-8765-4321', 'juan.perez@gmail.com', 'San José', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('117234567', 'María', 'González Rodríguez', 'Costarricense', '+506-8234-5678', 'maria.gonzalez@gmail.com', 'Heredia', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('119876543', 'Carlos', 'Ramírez Méndez', 'Nicaragüense', '+506-8987-6543', 'carlos.ramirez@gmail.com', 'San Pedro', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE)
ON CONFLICT (identificacion) DO UPDATE SET password_hash = EXCLUDED.password_hash, aprobado = TRUE, activo = TRUE;

-- Características raíces
INSERT INTO caracteristica (nombre, descripcion, id_padre) VALUES
('Lenguajes de Programación', 'Lenguajes de programación de software', NULL),
('Bases de Datos', 'Motores y lenguajes de bases de datos', NULL),
('Frameworks y Tecnologías', 'Frameworks, librerías y tecnologías', NULL),
('Habilidades Blandas', 'Competencias interpersonales y de gestión', NULL)
ON CONFLICT DO NOTHING;

-- Subcategorías (hijos de Lenguajes de Programación = id 1)
INSERT INTO caracteristica (nombre, descripcion, id_padre) VALUES
('Java', 'Lenguaje de programación Java', 1),
('Python', 'Lenguaje de programación Python', 1),
('JavaScript', 'Lenguaje de programación JavaScript/TypeScript', 1),
('C#', 'Lenguaje de programación C#', 1),
('PHP', 'Lenguaje de programación PHP', 1)
ON CONFLICT DO NOTHING;

-- Subcategorías (hijos de Bases de Datos = id 2)
INSERT INTO caracteristica (nombre, descripcion, id_padre) VALUES
('MySQL', 'Sistema de gestión de bases de datos MySQL', 2),
('PostgreSQL', 'Sistema de gestión de bases de datos PostgreSQL', 2),
('MongoDB', 'Base de datos NoSQL MongoDB', 2),
('Oracle', 'Sistema de gestión de bases de datos Oracle', 2)
ON CONFLICT DO NOTHING;

-- Subcategorías (hijos de Frameworks y Tecnologías = id 3)
INSERT INTO caracteristica (nombre, descripcion, id_padre) VALUES
('Spring Boot', 'Framework Java para desarrollo web', 3),
('React', 'Librería JavaScript para interfaces de usuario', 3),
('Angular', 'Framework TypeScript para aplicaciones web', 3),
('Docker', 'Plataforma de contenedores', 3)
ON CONFLICT DO NOTHING;

-- Subcategorías (hijos de Habilidades Blandas = id 4)
INSERT INTO caracteristica (nombre, descripcion, id_padre) VALUES
('Trabajo en Equipo', 'Capacidad de colaborar en equipo', 4),
('Comunicación', 'Habilidades de comunicación efectiva', 4),
('Resolución de Problemas', 'Capacidad analítica y resolución de problemas', 4)
ON CONFLICT DO NOTHING;
