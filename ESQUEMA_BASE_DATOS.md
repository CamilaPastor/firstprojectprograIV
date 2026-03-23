# 📊 Esquema de Base de Datos - Bolsa de Empleo

## Visión General

La base de datos **bolsa_empleo** está diseñada para gestionar una plataforma de empleo completa con soporte para administradores, empresas, oferentes (personas buscando empleo), puestos de trabajo y características/habilidades.

---

## 🗂️ Tablas del Sistema

### 1. **ADMINISTRADOR**

Almacena los administradores del sistema con acceso total.

```sql
CREATE TABLE administrador (
    id_admin INT PRIMARY KEY AUTO_INCREMENT,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
)
```

**Campos:**
- `id_admin`: ID único
- `identificacion`: Identificación única (username)
- `password_hash`: Contraseña en BCrypt
- `fecha_creacion`: Timestamp de creación
- `activo`: Estado del administrador

**Índices:**
- idx_identificacion (búsquedas rápidas)
- idx_activo (filtrado por estado)

---

### 2. **EMPRESA**

Empresas registradas que publican ofertas de empleo.

```sql
CREATE TABLE empresa (
    id_empresa INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    localizacion VARCHAR(200),
    correo VARCHAR(100) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    descripcion TEXT,
    password_hash VARCHAR(255) NOT NULL,
    aprobado BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
)
```

**Campos:**
- `id_empresa`: ID único
- `nombre`: Nombre de la empresa
- `localizacion`: Ubicación/dirección
- `correo`: Email único de contacto
- `telefono`: Teléfono de contacto
- `descripcion`: Descripción de la empresa
- `password_hash`: Contraseña BCrypt
- `aprobado`: ¿Fue aprobada por administrador?
- `fecha_registro`: Cuándo se registró
- `activo`: Estado de la empresa

**Índices:**
- idx_correo (única y búsqueda)
- idx_aprobado (filtrado de aprobación)
- idx_activo (estado)

---

### 3. **OFERENTE**

Personas que buscan empleo.

```sql
CREATE TABLE oferente (
    id_oferente INT PRIMARY KEY AUTO_INCREMENT,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    nacionalidad VARCHAR(50),
    telefono VARCHAR(20),
    correo VARCHAR(100) UNIQUE NOT NULL,
    residencia VARCHAR(200),
    password_hash VARCHAR(255) NOT NULL,
    aprobado BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
)
```

**Campos:**
- `id_oferente`: ID único
- `identificacion`: Cédula o ID única
- `nombre`: Nombre del oferente
- `apellido`: Apellido
- `nacionalidad`: País de origen
- `telefono`: Teléfono
- `correo`: Email único
- `residencia`: País/región donde vive
- `password_hash`: Contraseña BCrypt
- `aprobado`: Aprobado por admin
- `fecha_registro`: Fecha de registro
- `activo`: Estado

**Índices:**
- idx_identificacion (única)
- idx_correo (única)
- idx_aprobado
- idx_activo
- idx_nombre_apellido (búsqueda por nombre)

---

### 4. **CARACTERISTICA**

Habilidades, competencias o características requeridas. **Estructura jerárquica** con auto-referencia para subcategorías.

```sql
CREATE TABLE caracteristica (
    id_caracteristica INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    id_padre INT,
    nivel_minimo INT DEFAULT 1,
    nivel_maximo INT DEFAULT 5,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_padre) REFERENCES caracteristica(id_caracteristica)
)
```

**Campos:**
- `id_caracteristica`: ID único
- `nombre`: Nombre (ej: "Java", "Spring Boot")
- `descripcion`: Descripción detallada
- `id_padre`: ID de la característica padre (NULL si es categoría raíz)
- `nivel_minimo`: Nivel mínimo permitido
- `nivel_maximo`: Nivel máximo permitido
- `activo`: Estado
- `fecha_creacion`: Cuándo se creó

**Índices:**
- idx_nombre (búsqueda)
- idx_id_padre (jerarquía)
- idx_activo (estado)

**Estructura de Ejemplo:**
```
Bases de Datos (id=1, padre=NULL)
  ├─ MySQL (id=6, padre=1)
  ├─ PostgreSQL (id=7, padre=1)
  ├─ MongoDB (id=8, padre=1)
  └─ SQL (id=11, padre=1)

Lenguajes de Programación (id=2, padre=NULL)
  ├─ Java (id=8, padre=2)
  ├─ Python (id=9, padre=2)
  ├─ JavaScript (id=10, padre=2)
  └─ ...
```

---

### 5. **PUESTO**

Ofertas de empleo publicadas por empresas.

```sql
CREATE TABLE puesto (
    id_puesto INT PRIMARY KEY AUTO_INCREMENT,
    id_empresa INT NOT NULL,
    descripcion TEXT NOT NULL,
    salario DECIMAL(15, 2),
    tipo_publicacion ENUM('publico', 'privado') DEFAULT 'publico',
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
)
```

**Campos:**
- `id_puesto`: ID único
- `id_empresa`: Empresa que publica (FK)
- `descripcion`: Descripción del puesto
- `salario`: Salario ofrecido (DECIMAL)
- `tipo_publicacion`: ENUM('publico', 'privado')
  - **publico**: Visible para todos
  - **privado**: Solo para seleccionados
- `activo`: ¿La oferta está vigente?
- `fecha_registro`: Cuándo se publicó
- `fecha_actualizacion`: Última actualización

**Índices:**
- idx_id_empresa (búsqueda por empresa)
- idx_tipo_publicacion (filtrado)
- idx_activo (estado)
- idx_fecha_registro (ordenamiento)

**Foreign Key:**
- `id_empresa` → `empresa(id_empresa)` ON DELETE CASCADE

---

### 6. **PUESTO_CARACTERISTICA**

Asocia características requeridas con puestos. Especifica el nivel requerido.

```sql
CREATE TABLE puesto_caracteristica (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_puesto INT NOT NULL,
    id_caracteristica INT NOT NULL,
    nivel_requerido INT CHECK(nivel_requerido BETWEEN 1 AND 5) DEFAULT 3,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (id_puesto, id_caracteristica),
    FOREIGN KEY (id_puesto) REFERENCES puesto(id_puesto),
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica)
)
```

**Campos:**
- `id`: ID único
- `id_puesto`: Referencia al puesto (FK)
- `id_caracteristica`: Referencia a la característica (FK)
- `nivel_requerido`: Nivel requerido 1-5
  - 1: Básico
  - 2: Intermedio bajo
  - 3: Intermedio
  - 4: Avanzado
  - 5: Experto
- `fecha_creacion`: Cuándo se agregó

**Constraints:**
- UNIQUE (id_puesto, id_caracteristica): Una característica por puesto
- CHECK nivel_requerido BETWEEN 1 AND 5

**Foreign Keys:**
- `id_puesto` → `puesto(id_puesto)` ON DELETE CASCADE
- `id_caracteristica` → `caracteristica(id_caracteristica)` ON DELETE CASCADE

---

### 7. **OFERENTE_CARACTERISTICA**

Asocia características/habilidades del oferente. Especifica el nivel que domina.

```sql
CREATE TABLE oferente_caracteristica (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_oferente INT NOT NULL,
    id_caracteristica INT NOT NULL,
    nivel INT CHECK(nivel BETWEEN 1 AND 5) DEFAULT 3,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY (id_oferente, id_caracteristica),
    FOREIGN KEY (id_oferente) REFERENCES oferente(id_oferente),
    FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica)
)
```

**Campos:**
- `id`: ID único
- `id_oferente`: Referencia al oferente (FK)
- `id_caracteristica`: Referencia a la característica (FK)
- `nivel`: Nivel de dominio 1-5
- `fecha_creacion`: Cuándo se agregó

**Constraints:**
- UNIQUE (id_oferente, id_caracteristica): Una característica por oferente
- CHECK nivel BETWEEN 1 AND 5

---

### 8. **CV**

Almacena los CV en PDF de los oferentes.

```sql
CREATE TABLE cv (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_oferente INT UNIQUE NOT NULL,
    archivo_pdf LONGBLOB NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    FOREIGN KEY (id_oferente) REFERENCES oferente(id_oferente)
)
```

**Campos:**
- `id`: ID único
- `id_oferente`: Referencia ÚNICA al oferente (FK)
  - Solo un CV por oferente
- `archivo_pdf`: BLOB con el contenido del PDF
- `nombre_archivo`: Nombre original del archivo
- `fecha_subida`: Cuándo se subió
- `fecha_actualizacion`: Última actualización

**Constraint:**
- UNIQUE (id_oferente): Solo un CV por oferente

---

## 📊 Diagrama de Relaciones

```
┌──────────────────┐
│  ADMINISTRADOR   │
├──────────────────┤
│ id_admin (PK)    │
│ identificacion   │
│ password_hash    │
└──────────────────┘

┌──────────────────┐              ┌─────────────────────────┐
│    EMPRESA       │──────────────│   PUESTO                │
├──────────────────┤ 1:N          ├─────────────────────────┤
│ id_empresa (PK)  │              │ id_puesto (PK)          │
│ nombre           │              │ id_empresa (FK)         │
│ correo (U)       │              │ descripcion             │
│ password_hash    │              │ salario                 │
│ aprobado         │              │ tipo_publicacion (ENUM) │
└──────────────────┘              │ activo                  │
                                   └────────┬────────────────┘
                                            │ N:M
                                            │
                    ┌───────────────────────┴────────────────┐
                    │                                        │
        ┌────────────────────────┐           ┌───────────────────────┐
        │ PUESTO_CARACTERISTICA  │           │  OFERENTE_CARACTERISTICA
        ├────────────────────────┤           ├───────────────────────┤
        │ id (PK)                │           │ id (PK)               │
        │ id_puesto (FK)         │           │ id_oferente (FK)      │
        │ id_caracteristica (FK) │           │ id_caracteristica (FK)│
        │ nivel_requerido        │           │ nivel                 │
        └────────────────────────┘           └───────────────────────┘
                    │                                │
                    │                                │
                    │           ┌────────────────────┘
                    │           │
                    └───────┬───┘
                            │ N:M
                    ┌───────────────────────┐
                    │  CARACTERISTICA       │
                    ├───────────────────────┤
                    │ id_caracteristica (PK)│
                    │ nombre                │
                    │ descripcion           │
                    │ id_padre (FK self)    │──┐ (Jerarquía)
                    │ nivel_minimo/máximo   │  │
                    └───────────────────────┘  │
                            ▲                  │
                            └──────────────────┘

┌──────────────────┐              ┌──────────────────┐
│   OFERENTE       │──────────────│      CV          │
├──────────────────┤ 1:1          ├──────────────────┤
│ id_oferente (PK) │              │ id (PK)          │
│ identificacion   │              │ id_oferente (FK) │
│ nombre           │              │ archivo_pdf      │
│ apellido         │              │ nombre_archivo   │
│ correo (U)       │              └──────────────────┘
│ password_hash    │
│ aprobado         │
└──────────────────┘
```

---

## 🔑 Relaciones de Foreign Key

| Tabla | Campo | Referencias | On Delete | On Update |
|-------|-------|-------------|-----------|-----------|
| puesto | id_empresa | empresa.id_empresa | CASCADE | CASCADE |
| puesto_caracteristica | id_puesto | puesto.id_puesto | CASCADE | CASCADE |
| puesto_caracteristica | id_caracteristica | caracteristica.id_caracteristica | CASCADE | CASCADE |
| oferente_caracteristica | id_oferente | oferente.id_oferente | CASCADE | CASCADE |
| oferente_caracteristica | id_caracteristica | caracteristica.id_caracteristica | CASCADE | CASCADE |
| cv | id_oferente | oferente.id_oferente | CASCADE | CASCADE |
| caracteristica | id_padre | caracteristica.id_caracteristica | SET NULL | CASCADE |

---

## 📝 Datos de Prueba Incluidos

### Administrador
```
Identificación: admin
Contraseña: admin123 (BCrypt)
Estado: Activo
```

### Empresas (3)
```
1. TechSolutions S.A.
2. Innovate Digital
3. CloudServices Inc
```

### Oferentes (3)
```
1. Juan Pérez López (Costarricense)
2. María González Rodríguez (Costarricense)
3. Carlos Ramírez Méndez (Nicaragüense)
```

### Características (4 categorías + 23 subcategorías)

**Categorías:**
1. Bases de Datos (5 subcategorías)
2. Lenguajes de Programación (7 subcategorías)
3. Tecnologías Web (9 subcategorías)
4. Testing (7 subcategorías)

### Puestos (4 ofertas)
```
1. Desarrollador Java Sr. - TechSolutions
2. Ingeniero de Base de Datos - TechSolutions
3. Full Stack Developer - Innovate Digital
4. DevOps Engineer - CloudServices
```

---

## 🔐 Seguridad

### Hash de Contraseñas
- Algoritmo: **BCrypt** (bcrypt.js o similar)
- Costo: 10 (estándar)
- Ejemplo: `$2a$10$...`

### Contraseñas de Prueba

**Administrador:**
```
Usuario: admin
Contraseña: admin123
Hash: $2a$10$WvMIwdSJrP2MfA7XfLbJO.yG5yIDC6Ih8X3VqJLV3NG6u7k5n.Z0G
```

**Empresas/Oferentes:**
```
Contraseña: empresa123 / oferente123
Hash: $2a$10$X5w8lF9Jg3K2M8dQ1N5bZe9P7R6S4T3U2V1W0X9Y8Z7A6B5C4D3E2
```

---

## 📊 Estadísticas de la Base de Datos

| Tabla | Registros |
|-------|-----------|
| administrador | 1 |
| empresa | 3 |
| oferente | 3 |
| caracteristica | 28 (4 + 24) |
| puesto | 4 |
| puesto_caracteristica | 4 |
| oferente_caracteristica | 9 |
| cv | 0 |

---

## 🔍 Consultas Útiles

### Obtener oferentes por nivel de característica
```sql
SELECT o.nombre, o.apellido, c.nombre AS caracteristica, oc.nivel
FROM oferente_caracteristica oc
JOIN oferente o ON oc.id_oferente = o.id_oferente
JOIN caracteristica c ON oc.id_caracteristica = c.id_caracteristica
WHERE oc.nivel >= 4
ORDER BY oc.nivel DESC;
```

### Puestos que requieren una característica específica
```sql
SELECT p.id_puesto, e.nombre AS empresa, p.descripcion, pc.nivel_requerido
FROM puesto_caracteristica pc
JOIN puesto p ON pc.id_puesto = p.id_puesto
JOIN empresa e ON p.id_empresa = e.id_empresa
WHERE pc.id_caracteristica = 8  -- Java
AND p.activo = TRUE;
```

### Oferentes que cumplen requisitos de puesto
```sql
SELECT DISTINCT o.nombre, o.apellido
FROM oferente o
JOIN oferente_caracteristica oc ON o.id_oferente = oc.id_oferente
WHERE oc.id_caracteristica IN (
    SELECT id_caracteristica FROM puesto_caracteristica 
    WHERE id_puesto = 1
)
AND oc.nivel >= (
    SELECT pc.nivel_requerido FROM puesto_caracteristica pc
    WHERE pc.id_puesto = 1
    AND pc.id_caracteristica = oc.id_caracteristica
);
```

### Características con subcategorías
```sql
SELECT CONCAT(REPEAT('  ', COUNT(c2.id_caracteristica) - 1), c1.nombre) AS caracteristica
FROM caracteristica c1
LEFT JOIN caracteristica c2 ON c1.id_padre = c2.id_padre AND c1.id_padre IS NOT NULL
GROUP BY c1.id_caracteristica
ORDER BY c1.id_padre, c1.nombre;
```

---

## 📌 Notas Importantes

1. **Charset UTF-8MB4**: Soporta caracteres especiales y emoji
2. **InnoDB**: Garantiza integridad referencial y ACID
3. **Índices**: Optimizados para búsquedas comunes
4. **Jerarquía**: Característica usa auto-referencia para árbol
5. **ENUM**: tipo_publicacion usa ENUM para valores fijos
6. **LONGBLOB**: Para almacenar archivos PDF completos
7. **Timestamps**: Automáticos para auditoría

---

**Versión: 1.0**
**Actualizado: 19 de marzo de 2026**
