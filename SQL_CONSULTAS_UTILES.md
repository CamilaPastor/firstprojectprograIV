# 💾 Referencia de Consultas SQL - Bolsa de Empleo

## Índice
1. [Consultas de Administrador](#administrador)
2. [Consultas de Empresa](#empresa)
3. [Consultas de Oferente](#oferente)
4. [Consultas de Puestos](#puestos)
5. [Consultas de Características](#características)
6. [Consultas de Matching](#matching)
7. [Consultas de Reportes](#reportes)
8. [Mantenimiento](#mantenimiento)

---

## 🔐 Administrador

### Login del Administrador
```sql
SELECT id_admin, identificacion FROM administrador
WHERE identificacion = 'admin' 
AND password_hash = '$2a$10$...' 
AND activo = TRUE;
```

### Listar todos los administradores
```sql
SELECT id_admin, identificacion, fecha_creacion, activo
FROM administrador
ORDER BY fecha_creacion DESC;
```

### Crear nuevo administrador
```sql
INSERT INTO administrador (identificacion, password_hash, activo)
VALUES ('admin2', '[HASH_BCRYPT]', TRUE);
```

### Cambiar contraseña de administrador
```sql
UPDATE administrador 
SET password_hash = '[NEW_HASH]'
WHERE id_admin = 1
AND password_hash = '[OLD_HASH]';
```

### Desactivar administrador
```sql
UPDATE administrador 
SET activo = FALSE
WHERE id_admin = 1;
```

---

## 🏢 Empresa

### Login de Empresa
```sql
SELECT id_empresa, nombre, aprobado FROM empresa
WHERE correo = 'info@empresa.cr' 
AND password_hash = '[HASH_BCRYPT]' 
AND activo = TRUE;
```

### Listar empresas aprobadas
```sql
SELECT id_empresa, nombre, localizacion, correo, telefono, fecha_registro
FROM empresa
WHERE aprobado = TRUE AND activo = TRUE
ORDER BY nombre ASC;
```

### Listar empresas pendientes de aprobación
```sql
SELECT id_empresa, nombre, correo, fecha_registro
FROM empresa
WHERE aprobado = FALSE AND activo = TRUE
ORDER BY fecha_registro ASC;
```

### Obtener información detallada de una empresa
```sql
SELECT e.id_empresa, e.nombre, e.localizacion, e.correo, e.telefono,
       e.descripcion, e.aprobado, e.fecha_registro, e.activo,
       COUNT(p.id_puesto) AS total_puestos,
       COUNT(DISTINCT CASE WHEN p.activo = TRUE THEN p.id_puesto END) AS puestos_activos
FROM empresa e
LEFT JOIN puesto p ON e.id_empresa = p.id_empresa
WHERE e.id_empresa = 1
GROUP BY e.id_empresa;
```

### Registrar nueva empresa
```sql
INSERT INTO empresa (nombre, localizacion, correo, telefono, descripcion, password_hash)
VALUES ('Nueva Empresa', 'San José', 'contact@nueva.cr', '+506-2000-0000', 
        'Descripción de la empresa', '[HASH_BCRYPT]');
```

### Aprobar empresa
```sql
UPDATE empresa 
SET aprobado = TRUE
WHERE id_empresa = 2;
```

### Actualizar información de empresa
```sql
UPDATE empresa 
SET nombre = 'Nuevo Nombre', descripcion = 'Nueva descripción'
WHERE id_empresa = 2 AND aprobado = FALSE;
```

### Desactivar empresa
```sql
UPDATE empresa 
SET activo = FALSE
WHERE id_empresa = 2;

-- Nota: Las cascadas harán que los puestos se desactiven también
```

### Contar puestos por empresa
```sql
SELECT e.nombre, COUNT(p.id_puesto) AS total_puestos
FROM empresa e
LEFT JOIN puesto p ON e.id_empresa = p.id_empresa
WHERE e.aprobado = TRUE AND e.activo = TRUE
GROUP BY e.id_empresa, e.nombre
ORDER BY total_puestos DESC;
```

---

## 👤 Oferente

### Login de Oferente
```sql
SELECT id_oferente, nombre, apellido, aprobado FROM oferente
WHERE correo = 'juan@email.com' 
AND password_hash = '[HASH_BCRYPT]' 
AND activo = TRUE;
```

### Obtener perfil de oferente
```sql
SELECT id_oferente, identificacion, nombre, apellido, nacionalidad,
       telefono, correo, residencia, aprobado, fecha_registro
FROM oferente
WHERE id_oferente = 1 AND activo = TRUE;
```

### Listar oferentes activos
```sql
SELECT id_oferente, CONCAT(nombre, ' ', apellido) AS nombre_completo,
       identificacion, correo, nacionalidad, residencia
FROM oferente
WHERE activo = TRUE AND aprobado = TRUE
ORDER BY nombre, apellido;
```

### Oferentes pendientes de aprobación
```sql
SELECT id_oferente, CONCAT(nombre, ' ', apellido) AS nombre_completo,
       identificacion, correo, fecha_registro
FROM oferente
WHERE aprobado = FALSE AND activo = TRUE
ORDER BY fecha_registro ASC;
```

### Registrar nuevo oferente
```sql
INSERT INTO oferente (identificacion, nombre, apellido, nacionalidad, 
                     telefono, correo, residencia, password_hash)
VALUES ('118765432', 'Carlos', 'López', 'Costarricense', '+506-8765-4321',
        'carlos@email.com', 'San José', '[HASH_BCRYPT]');
```

### Actualizar información de oferente
```sql
UPDATE oferente 
SET telefono = '+506-8765-4321', residencia = 'Heredia'
WHERE id_oferente = 1;
```

### Aprobar oferente
```sql
UPDATE oferente 
SET aprobado = TRUE
WHERE id_oferente = 3;
```

### Cambiar contraseña de oferente
```sql
UPDATE oferente 
SET password_hash = '[NEW_HASH]'
WHERE id_oferente = 1 AND password_hash = '[OLD_HASH]';
```

### Contar oferentes por nacionalidad
```sql
SELECT nacionalidad, COUNT(*) AS total
FROM oferente
WHERE activo = TRUE AND aprobado = TRUE
GROUP BY nacionalidad
ORDER BY total DESC;
```

---

## 📋 Puestos

### Listar puestos públicos activos
```sql
SELECT p.id_puesto, e.nombre AS empresa, p.descripcion, p.salario,
       p.tipo_publicacion, p.fecha_registro
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
WHERE p.tipo_publicacion = 'publico' 
AND p.activo = TRUE 
AND e.activo = TRUE
ORDER BY p.fecha_registro DESC;
```

### Obtener detalles completos de un puesto
```sql
SELECT p.id_puesto, e.nombre AS empresa, e.correo AS correo_empresa,
       p.descripcion, p.salario, p.tipo_publicacion, p.activo, p.fecha_registro,
       GROUP_CONCAT(DISTINCT c.nombre SEPARATOR ', ') AS caracteristicas_requeridas
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
LEFT JOIN puesto_caracteristica pc ON p.id_puesto = pc.id_puesto
LEFT JOIN caracteristica c ON pc.id_caracteristica = c.id_caracteristica
WHERE p.id_puesto = 1
GROUP BY p.id_puesto;
```

### Puestos activos por empresa
```sql
SELECT e.nombre AS empresa, COUNT(p.id_puesto) AS puestos_activos
FROM empresa e
LEFT JOIN puesto p ON e.id_empresa = p.id_empresa AND p.activo = TRUE
WHERE e.activo = TRUE
GROUP BY e.id_empresa, e.nombre
HAVING puestos_activos > 0
ORDER BY puestos_activos DESC;
```

### Publicar nuevo puesto
```sql
INSERT INTO puesto (id_empresa, descripcion, salario, tipo_publicacion)
VALUES (1, 'Desarrollador Java con experiencia en Spring Boot',
        2500000.00, 'publico');
```

### Agregar características requeridas al puesto (ejemplo)
```sql
INSERT INTO puesto_caracteristica (id_puesto, id_caracteristica, nivel_requerido)
VALUES (1, 8, 4),   -- Java nivel 4
       (1, 14, 4),  -- Spring Boot nivel 4
       (1, 11, 3);  -- SQL nivel 3
```

### Desactivar puesto
```sql
UPDATE puesto 
SET activo = FALSE
WHERE id_puesto = 1;
```

### Actualizar salario de puesto
```sql
UPDATE puesto 
SET salario = 2800000.00
WHERE id_puesto = 1;
```

### Puestos ordenados por salario
```sql
SELECT id_puesto, e.nombre AS empresa, descripcion, salario
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
WHERE p.activo = TRUE
ORDER BY p.salario DESC;
```

### Buscar puestos por keyword
```sql
SELECT id_puesto, e.nombre AS empresa, descripcion, salario
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
WHERE p.activo = TRUE 
AND (p.descripcion LIKE '%Java%' OR p.descripcion LIKE '%Spring%')
ORDER BY p.fecha_registro DESC;
```

---

## 🎯 Características

### Listar todas las categorías principales
```sql
SELECT id_caracteristica, nombre, descripcion, nivel_minimo, nivel_maximo
FROM caracteristica
WHERE id_padre IS NULL 
AND activo = TRUE
ORDER BY nombre;
```

### Listar subcategorías de una categoría
```sql
SELECT id_caracteristica, nombre, descripcion
FROM caracteristica
WHERE id_padre = 1 
AND activo = TRUE
ORDER BY nombre;
```

### Árbol completo de características
```sql
SELECT CONCAT(REPEAT('  ', 
       (SELECT COUNT(*) FROM caracteristica c2 
        WHERE c2.id_padre = c1.id_padre AND c2.id_padre IS NOT NULL) - 1), 
       c1.nombre) AS caracteristica,
       c1.id_caracteristica, c1.id_padre
FROM caracteristica c1
ORDER BY c1.id_padre, c1.nombre;
```

### Obtener detalles de una característica con subcategorías
```sql
SELECT id_caracteristica, nombre, descripcion, id_padre, nivel_minimo, nivel_maximo
FROM caracteristica
WHERE id_padre = 2 OR id_caracteristica = 2
ORDER BY id_padre DESC, nombre;
```

### Agregar nueva característica principal
```sql
INSERT INTO caracteristica (nombre, descripcion, nivel_minimo, nivel_maximo)
VALUES ('DevOps/Cloud', 'Conocimientos en DevOps y computación en la nube', 1, 5);
```

### Agregar subcategoría
```sql
INSERT INTO caracteristica (nombre, descripcion, id_padre, nivel_minimo, nivel_maximo)
VALUES ('AWS', 'Amazon Web Services', 5, 1, 5);
-- Donde 5 es el id del padre (DevOps/Cloud)
```

### Contar características por nivel
```sql
SELECT nivel, COUNT(*) AS total
FROM oferente_caracteristica
WHERE id_oferente = 1
GROUP BY nivel
ORDER BY nivel DESC;
```

---

## 🎯 Matching (Relacionar Oferentes con Puestos)

### Oferentes que cumplen con TODAS las características de un puesto
```sql
SELECT DISTINCT o.id_oferente, CONCAT(o.nombre, ' ', o.apellido) AS nombre,
       o.correo, o.telefono
FROM oferente o
WHERE o.activo = TRUE AND o.aprobado = TRUE
AND NOT EXISTS (
    SELECT 1 FROM puesto_caracteristica pc
    WHERE pc.id_puesto = 1
    AND NOT EXISTS (
        SELECT 1 FROM oferente_caracteristica oc
        WHERE oc.id_oferente = o.id_oferente
        AND oc.id_caracteristica = pc.id_caracteristica
        AND oc.nivel >= pc.nivel_requerido
    )
);
```

### Matching score: Oferentes más calificados para un puesto
```sql
SELECT o.id_oferente, CONCAT(o.nombre, ' ', o.apellido) AS nombre,
       COUNT(CASE WHEN oc.nivel >= pc.nivel_requerido THEN 1 END) AS caracteristicas_cumplidas,
       COUNT(DISTINCT pc.id_caracteristica) AS total_requeridas,
       ROUND(100 * COUNT(CASE WHEN oc.nivel >= pc.nivel_requerido THEN 1 END) / 
             COUNT(DISTINCT pc.id_caracteristica), 2) AS porcentaje_match
FROM oferente o
CROSS JOIN puesto_caracteristica pc
LEFT JOIN oferente_caracteristica oc ON o.id_oferente = oc.id_oferente 
                                    AND oc.id_caracteristica = pc.id_caracteristica
WHERE pc.id_puesto = 1
AND o.activo = TRUE AND o.aprobado = TRUE
GROUP BY o.id_oferente, o.nombre, o.apellido
ORDER BY porcentaje_match DESC, caracteristicas_cumplidas DESC;
```

### Puestos más adecuados para un oferente
```sql
SELECT p.id_puesto, e.nombre AS empresa, p.descripcion, p.salario,
       COUNT(CASE WHEN oc.id_caracteristica IS NOT NULL THEN 1 END) AS caracteristicas_posee,
       COUNT(DISTINCT pc.id_caracteristica) AS total_requeridas,
       ROUND(100 * COUNT(CASE WHEN oc.id_caracteristica IS NOT NULL THEN 1 END) / 
             COUNT(DISTINCT pc.id_caracteristica), 2) AS porcentaje_match
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
JOIN puesto_caracteristica pc ON p.id_puesto = pc.id_puesto
LEFT JOIN oferente_caracteristica oc ON oc.id_oferente = 1 
                                    AND oc.id_caracteristica = pc.id_caracteristica
WHERE p.activo = TRUE AND e.activo = TRUE
GROUP BY p.id_puesto
ORDER BY porcentaje_match DESC;
```

### Puestos que buscan específicamente una característica que tiene el oferente
```sql
SELECT DISTINCT p.id_puesto, e.nombre AS empresa, p.descripcion, p.salario
FROM puesto p
JOIN empresa e ON p.id_empresa = e.id_empresa
JOIN puesto_caracteristica pc ON p.id_puesto = pc.id_puesto
JOIN oferente_caracteristica oc ON pc.id_caracteristica = oc.id_caracteristica
WHERE oc.id_oferente = 1
AND p.activo = TRUE
AND e.activo = TRUE
ORDER BY p.fecha_registro DESC;
```

---

## 📊 Reportes

### Dashboard: Estadísticas generales
```sql
SELECT 
    (SELECT COUNT(*) FROM empresa WHERE aprobado = TRUE AND activo = TRUE) AS empresas_activas,
    (SELECT COUNT(*) FROM oferente WHERE aprobado = TRUE AND activo = TRUE) AS oferentes_activos,
    (SELECT COUNT(*) FROM puesto WHERE activo = TRUE) AS puestos_activos,
    (SELECT COUNT(DISTINCT id_oferente) FROM oferente_caracteristica) AS oferentes_con_habilidades,
    (SELECT COUNT(*) FROM caracteristica WHERE id_padre IS NULL) AS categorias;
```

### Reporte: Empresas y sus puestos
```sql
SELECT e.id_empresa, e.nombre, e.correo,
       COUNT(p.id_puesto) AS total_puestos,
       COUNT(CASE WHEN p.activo = TRUE THEN 1 END) AS puestos_activos,
       MAX(p.salario) AS salario_maximo,
       MIN(p.salario) AS salario_minimo,
       AVG(p.salario) AS salario_promedio
FROM empresa e
LEFT JOIN puesto p ON e.id_empresa = p.id_empresa
WHERE e.aprobado = TRUE AND e.activo = TRUE
GROUP BY e.id_empresa, e.nombre, e.correo
ORDER BY total_puestos DESC;
```

### Reporte: Top características más buscadas
```sql
SELECT c.nombre, COUNT(pc.id_puesto) AS veces_requerida,
       AVG(pc.nivel_requerido) AS nivel_promedio
FROM caracteristica c
JOIN puesto_caracteristica pc ON c.id_caracteristica = pc.id_caracteristica
WHERE pc.id_puesto IN (SELECT id_puesto FROM puesto WHERE activo = TRUE)
GROUP BY c.id_caracteristica, c.nombre
ORDER BY veces_requerida DESC
LIMIT 10;
```

### Reporte: Top características más ofrecidas
```sql
SELECT c.nombre, COUNT(oc.id_oferente) AS oferentes_con_caracteristica,
       AVG(oc.nivel) AS nivel_promedio
FROM caracteristica c
JOIN oferente_caracteristica oc ON c.id_caracteristica = oc.id_caracteristica
WHERE oc.id_oferente IN (SELECT id_oferente FROM oferente WHERE activo = TRUE AND aprobado = TRUE)
GROUP BY c.id_caracteristica, c.nombre
ORDER BY oferentes_con_caracteristica DESC
LIMIT 10;
```

### Reporte: Actividad reciente
```sql
SELECT 'Nuevo puesto' AS tipo, p.fecha_registro AS fecha, 
       CONCAT('Puesto: ', SUBSTRING(p.descripcion, 1, 50)) AS descripcion
FROM puesto p
WHERE p.fecha_registro > DATE_SUB(NOW(), INTERVAL 7 DAY)

UNION ALL

SELECT 'Nuevo oferente', o.fecha_registro,
       CONCAT(o.nombre, ' ', o.apellido)
FROM oferente o
WHERE o.fecha_registro > DATE_SUB(NOW(), INTERVAL 7 DAY)

UNION ALL

SELECT 'Nueva empresa', e.fecha_registro,
       e.nombre
FROM empresa e
WHERE e.fecha_registro > DATE_SUB(NOW(), INTERVAL 7 DAY)

ORDER BY fecha DESC;
```

---

## 🛠️ Mantenimiento

### Backup de estructura
```sql
-- Generar script de creación de tabla
SHOW CREATE TABLE puesto\G
```

### Limpiar datos de prueba
```sql
DELETE FROM cv;
DELETE FROM oferente_caracteristica;
DELETE FROM puesto_caracteristica;
DELETE FROM puesto;
DELETE FROM oferente;
DELETE FROM empresa;
DELETE FROM caracteristica WHERE id_padre IS NOT NULL;
DELETE FROM caracteristica WHERE id_padre IS NULL;
DELETE FROM administrador WHERE identificacion != 'admin';
```

### Verificar integridad referencial
```sql
-- Puestos huérfanos (no deberían existir con ON DELETE CASCADE)
SELECT p.id_puesto FROM puesto p
LEFT JOIN empresa e ON p.id_empresa = e.id_empresa
WHERE e.id_empresa IS NULL;

-- Características huérfanas
SELECT oc.id FROM oferente_caracteristica oc
LEFT JOIN oferente o ON oc.id_oferente = o.id_oferente
WHERE o.id_oferente IS NULL;

-- CVs huérfanos
SELECT cv.id FROM cv
LEFT JOIN oferente o ON cv.id_oferente = o.id_oferente
WHERE o.id_oferente IS NULL;
```

### Actualizar timestamps
```sql
UPDATE puesto 
SET fecha_actualizacion = CURRENT_TIMESTAMP
WHERE id_puesto = 1;
```

### Estadísticas de tablas
```sql
SELECT table_name, table_rows, data_length, index_length
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'bolsa_empleo'
ORDER BY table_rows DESC;
```

---

**Última actualización: 19 de marzo de 2026**
