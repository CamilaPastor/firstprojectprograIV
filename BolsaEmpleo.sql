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
    salario           DECIMAL(15,2),
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

INSERT INTO administrador (identificacion, password_hash, activo) VALUES
('admin', '$2a$10$Kh.1ejy2HCRpTrQAZoi2KuW5kwlNDofTWTIfcrKb6.DD9oRI8MM16', TRUE)
ON CONFLICT (identificacion) DO UPDATE SET password_hash = EXCLUDED.password_hash, activo = TRUE;

INSERT INTO empresa (nombre, localizacion, correo, telefono, descripcion, password_hash, aprobado, activo) VALUES
('TechSolutions S.A.', 'San José, Costa Rica', 'info@techsolutions.cr', '+506-2234-5678', 'Empresa de consultoría en tecnología de la información', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE),
('Innovate Digital', 'San José, Costa Rica', 'contact@innovate.cr', '+506-8765-4321', 'Desarrolladora de soluciones web y móviles', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE),
('CloudServices Inc', 'Heredia, Costa Rica', 'support@cloudservices.cr', '+506-2345-6789', 'Proveedor de servicios en la nube', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', TRUE, TRUE),
('DataCorp Analytics', 'Cartago, Costa Rica', 'hr@datacorp.cr', '+506-2456-7890', 'Consultora de análisis de datos e inteligencia de negocios', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', FALSE, TRUE),
('StartUp Labs', 'Alajuela, Costa Rica', 'hola@startuplabs.cr', '+506-2567-8901', 'Incubadora de startups tecnológicas', '$2a$10$EE56h0SxDzVo8rwuvPSmle1ffR6a/U1MP7Zkkjl2ybWrYEjWhiH06', FALSE, TRUE)
ON CONFLICT (correo) DO NOTHING;

INSERT INTO oferente (identificacion, nombre, apellido, nacionalidad, telefono, correo, residencia, password_hash, aprobado, activo) VALUES
('118456789', 'Juan', 'Pérez López', 'Costarricense', '+506-8765-4321', 'juan.perez@gmail.com', 'San José', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('117234567', 'María', 'González Rodríguez', 'Costarricense', '+506-8234-5678', 'maria.gonzalez@gmail.com', 'Heredia', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('119876543', 'Carlos', 'Ramírez Méndez', 'Nicaragüense', '+506-8987-6543', 'carlos.ramirez@gmail.com', 'San Pedro', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('116543210', 'Ana', 'Vargas Solano', 'Costarricense', '+506-8123-4567', 'ana.vargas@gmail.com', 'Curridabat', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('115678901', 'Luis', 'Castro Jiménez', 'Costarricense', '+506-8456-7890', 'luis.castro@gmail.com', 'Alajuela', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', TRUE, TRUE),
('114321098', 'Sofía', 'Mora Chacón', 'Costarricense', '+506-8678-9012', 'sofia.mora@gmail.com', 'Cartago', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', FALSE, TRUE),
('113210987', 'Diego', 'Hernández Rojas', 'Panameño', '+506-8890-1234', 'diego.hernandez@gmail.com', 'Escazú', '$2a$10$HXpwgRKnksZW0au9bjJFRuVLuV4ZcQG2OBm3QQ.pzv8hCFGrqeDTC', FALSE, TRUE)
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO caracteristica (nombre, descripcion, id_padre)
SELECT v.nombre, v.descripcion, NULL
FROM (VALUES
    ('Lenguajes de Programación', 'Lenguajes de programación de software'),
    ('Bases de Datos', 'Motores y lenguajes de bases de datos'),
    ('Frameworks y Tecnologías', 'Frameworks, librerías y tecnologías'),
    ('Habilidades Blandas', 'Competencias interpersonales y de gestión')
) AS v(nombre, descripcion)
WHERE NOT EXISTS (SELECT 1 FROM caracteristica c WHERE c.nombre = v.nombre AND c.id_padre IS NULL);

INSERT INTO caracteristica (nombre, descripcion, id_padre)
SELECT v.nombre, v.descripcion, p.id_caracteristica
FROM (VALUES
    ('Java', 'Lenguaje de programación Java', 'Lenguajes de Programación'),
    ('Python', 'Lenguaje de programación Python', 'Lenguajes de Programación'),
    ('JavaScript', 'Lenguaje de programación JavaScript/TypeScript', 'Lenguajes de Programación'),
    ('C#', 'Lenguaje de programación C#', 'Lenguajes de Programación'),
    ('PHP', 'Lenguaje de programación PHP', 'Lenguajes de Programación'),
    ('MySQL', 'Sistema de gestión de bases de datos MySQL', 'Bases de Datos'),
    ('PostgreSQL', 'Sistema de gestión de bases de datos PostgreSQL', 'Bases de Datos'),
    ('MongoDB', 'Base de datos NoSQL MongoDB', 'Bases de Datos'),
    ('Oracle', 'Sistema de gestión de bases de datos Oracle', 'Bases de Datos'),
    ('Spring Boot', 'Framework Java para desarrollo web', 'Frameworks y Tecnologías'),
    ('React', 'Librería JavaScript para interfaces de usuario', 'Frameworks y Tecnologías'),
    ('Angular', 'Framework TypeScript para aplicaciones web', 'Frameworks y Tecnologías'),
    ('Docker', 'Plataforma de contenedores', 'Frameworks y Tecnologías'),
    ('Trabajo en Equipo', 'Capacidad de colaborar en equipo', 'Habilidades Blandas'),
    ('Comunicación', 'Habilidades de comunicación efectiva', 'Habilidades Blandas'),
    ('Resolución de Problemas', 'Capacidad analítica y resolución de problemas', 'Habilidades Blandas')
) AS v(nombre, descripcion, padre)
JOIN caracteristica p ON p.nombre = v.padre AND p.id_padre IS NULL
WHERE NOT EXISTS (SELECT 1 FROM caracteristica c WHERE c.nombre = v.nombre);

DO $$
BEGIN
    IF (SELECT COUNT(*) FROM puesto) = 0 THEN
        INSERT INTO puesto (id_empresa, descripcion, salario, tipo_publicacion, activo)
        SELECT e.id_empresa, v.descripcion, v.salario, v.tipo, v.activo
        FROM (VALUES
            ('info@techsolutions.cr', E'Desarrollador Backend Java Senior\nDiseño y desarrollo de servicios REST y microservicios.', 1500000, 'publico', TRUE),
            ('info@techsolutions.cr', E'Ingeniero de Datos\nConstrucción de pipelines de datos y modelado analítico.', 1300000, 'privado', TRUE),
            ('info@techsolutions.cr', E'Practicante de QA\nApoyo en pruebas manuales y automatizadas.', 450000, 'publico', FALSE),
            ('contact@innovate.cr', E'Desarrollador Frontend React\nDesarrollo de interfaces modernas y responsivas.', 1200000, 'publico', TRUE),
            ('contact@innovate.cr', E'Full Stack Developer\nDesarrollo de extremo a extremo en proyectos web.', 1400000, 'privado', TRUE),
            ('contact@innovate.cr', E'Diseñador UI\nDiseño de componentes e interacción de usuario.', 900000, 'publico', FALSE),
            ('support@cloudservices.cr', E'DevOps Engineer\nAutomatización de despliegues e infraestructura como código.', 1600000, 'publico', TRUE),
            ('support@cloudservices.cr', E'Administrador de Base de Datos\nGestión, tuning y respaldo de bases de datos.', 1350000, 'privado', TRUE),
            ('support@cloudservices.cr', E'Arquitecto de Soluciones Cloud\nDiseño de arquitecturas escalables en la nube.', 2000000, 'publico', TRUE)
        ) AS v(correo, descripcion, salario, tipo, activo)
        JOIN empresa e ON e.correo = v.correo;

        INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido)
        SELECT p.id_puesto, c.id_caracteristica, v.nivel
        FROM (VALUES
            ('Desarrollador Backend Java Senior', 'Java', 4),
            ('Desarrollador Backend Java Senior', 'Spring Boot', 4),
            ('Desarrollador Backend Java Senior', 'PostgreSQL', 3),
            ('Desarrollador Backend Java Senior', 'Trabajo en Equipo', 3),
            ('Ingeniero de Datos', 'Python', 4),
            ('Ingeniero de Datos', 'PostgreSQL', 4),
            ('Ingeniero de Datos', 'MongoDB', 3),
            ('Practicante de QA', 'Java', 2),
            ('Practicante de QA', 'Resolución de Problemas', 3),
            ('Desarrollador Frontend React', 'JavaScript', 4),
            ('Desarrollador Frontend React', 'React', 4),
            ('Desarrollador Frontend React', 'Comunicación', 3),
            ('Full Stack Developer', 'Java', 3),
            ('Full Stack Developer', 'React', 3),
            ('Full Stack Developer', 'Spring Boot', 3),
            ('Full Stack Developer', 'Trabajo en Equipo', 4),
            ('Diseñador UI', 'JavaScript', 2),
            ('Diseñador UI', 'Comunicación', 4),
            ('DevOps Engineer', 'Docker', 4),
            ('DevOps Engineer', 'PostgreSQL', 3),
            ('DevOps Engineer', 'Python', 3),
            ('Administrador de Base de Datos', 'PostgreSQL', 5),
            ('Administrador de Base de Datos', 'MySQL', 3),
            ('Administrador de Base de Datos', 'Oracle', 3),
            ('Arquitecto de Soluciones Cloud', 'Docker', 5),
            ('Arquitecto de Soluciones Cloud', 'Spring Boot', 4),
            ('Arquitecto de Soluciones Cloud', 'Trabajo en Equipo', 5),
            ('Arquitecto de Soluciones Cloud', 'Comunicación', 4)
        ) AS v(titulo, caracteristica, nivel)
        JOIN puesto p ON p.descripcion LIKE v.titulo || '%'
        JOIN caracteristica c ON c.nombre = v.caracteristica;
    END IF;
END $$;

DO $$
BEGIN
    IF (SELECT COUNT(*) FROM oferente_caracteristica) = 0 THEN
        INSERT INTO oferente_caracteristica (id_oferente, id_caracteristica, nivel)
        SELECT o.id_oferente, c.id_caracteristica, v.nivel
        FROM (VALUES
            ('118456789', 'Java', 5),
            ('118456789', 'Spring Boot', 4),
            ('118456789', 'PostgreSQL', 4),
            ('118456789', 'Docker', 3),
            ('118456789', 'Trabajo en Equipo', 4),
            ('117234567', 'JavaScript', 5),
            ('117234567', 'React', 5),
            ('117234567', 'Comunicación', 4),
            ('117234567', 'Trabajo en Equipo', 3),
            ('119876543', 'Python', 4),
            ('119876543', 'PostgreSQL', 5),
            ('119876543', 'MongoDB', 3),
            ('119876543', 'Docker', 4),
            ('116543210', 'Java', 3),
            ('116543210', 'React', 4),
            ('116543210', 'Spring Boot', 3),
            ('116543210', 'Trabajo en Equipo', 4),
            ('115678901', 'Docker', 5),
            ('115678901', 'PostgreSQL', 4),
            ('115678901', 'Python', 4),
            ('115678901', 'Comunicación', 3)
        ) AS v(identificacion, caracteristica, nivel)
        JOIN oferente o ON o.identificacion = v.identificacion
        JOIN caracteristica c ON c.nombre = v.caracteristica;
    END IF;
END $$;
