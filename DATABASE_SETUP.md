# Configuración de Base de Datos Oracle

## Información de Conexión Oracle

### Conectar a Oracle con SQL*Plus

```bash
# Abrir SQL*Plus
sqlplus

# Usuario: system
# Contraseña: password
```

### Pasos para Configurar la Base de Datos

#### 1. Crear Usuario de Aplicación (Opcional)
```sql
CREATE USER bolsa_empleo IDENTIFIED BY bolsa_empleo;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO bolsa_empleo;
```

#### 2. Ejecutar el Script de Creación

Opción A: Desde SQL*Plus
```sql
@C:\ruta\al\archivo\database.sql
```

Opción B: Usar SQLPlus directamente
```bash
sqlplus system/password @database.sql
```

#### 3. Verificar las Tablas Creadas
```sql
SELECT table_name FROM user_tables;
```

Deberías ver:
```
TABLE_NAME
----------
EMPRESA
OFERENTE
ADMINISTRADOR
PUESTO
CARACTERISTICA
CV
```

#### 4. Verificar las Secuencias
```sql
SELECT sequence_name FROM user_sequences;
```

---

## Configuración de application.properties

### Opción 1: Usuario SYSTEM (Defecto)
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=system
spring.datasource.password=password
```

### Opción 2: Usuario Dedicado (Recomendado para Producción)
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=bolsa_empleo
spring.datasource.password=bolsa_empleo
```

### Opciones de Conexión

#### Conectarse a Base de Datos Remota
```properties
# Formato: jdbc:oracle:thin:@host:puerto:sid
spring.datasource.url=jdbc:oracle:thin:@192.168.1.100:1521:xe
```

#### Conectarse con TNS (Archivo tnsnames.ora)
```properties
spring.datasource.url=jdbc:oracle:thin:@MYDB
spring.datasource.username=system
spring.datasource.password=password
```

---

## Variables de Entorno (Recomendado)

### Crear Variables de Entorno del Sistema

#### Windows (CMD):
```batch
setx DB_URL jdbc:oracle:thin:@localhost:1521:xe
setx DB_USER system
setx DB_PASSWORD password
```

#### Windows (PowerShell):
```powershell
$env:DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"
$env:DB_USER = "system"
$env:DB_PASSWORD = "password"
```

#### Linux/Mac:
```bash
export DB_URL=jdbc:oracle:thin:@localhost:1521:xe
export DB_USER=system
export DB_PASSWORD=password
```

### Usar Variables en application.properties
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

---

## Archivo application-prod.properties

Crear archivo para configuración de producción:

```properties
# application-prod.properties

spring.datasource.url=jdbc:oracle:thin:@prod-server:1521:ORCL
spring.datasource.username=bolsa_empleo
spring.datasource.password=${PROD_DB_PASSWORD}
spring.datasource.hikari.maximum-pool-size=20

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

spring.thymeleaf.cache=true

logging.level.root=WARN
logging.level.com.bolsaempleo=INFO

app.upload.dir=/var/uploads/cv

server.servlet.session.timeout=30m
```

### Ejecutar con Perfil de Producción
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

---

## Solución de Problemas de Conexión

### Error: "No suitable driver found"
**Solución:**
```gradle
// En build.gradle, asegurar que existe:
runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
```

### Error: "Listener refused the connection"
**Causas posibles:**
1. Oracle Database no está ejecutándose
2. Puerto incorrecto (1521 es el estándar)
3. SID incorrecto (xe, ORCL, etc.)

**Verificar:**
```sql
-- Desde SQL*Plus
SHOW PARAMETER db_name;
```

### Error: "Usuario/Contraseña incorrectos"
**Verificar:**
```bash
# Probar conexión con SQL*Plus
sqlplus system/password@localhost:1521/xe

# Si no funciona, resetear contraseña de system
sqlplus / as sysdba
ALTER USER system IDENTIFIED BY new_password;
```

### Error: "ORA-12514: TNS:listener does not currently know of service requested"
**Solución:**
- Verificar el SID correcto (xe, ORCL, etc.)
- Usar: `jdbc:oracle:thin:@localhost:1521:xe`

---

## Driver OJDBC

### Versión Utilizada
```gradle
runtimeOnly 'com.oracle.database.jdbc:ojdbc11'
```

### Versiones Alternativas
```gradle
// Oracle 12c
runtimeOnly 'com.oracle.database.jdbc:ojdbc10'

// Oracle 19c
runtimeOnly 'com.oracle.database.jdbc:ojdbc11'

// Versión más reciente
runtimeOnly 'com.oracle.database.jdbc:ojdbc8'
```

---

## Backup y Restauración

### Crear Backup
```bash
# Windows
expdp system/password DUMPFILE=bolsa_backup.dmp LOGFILE=bolsa_log.log FULL=YES

# Linux
expdp system/password@XE DUMPFILE=bolsa_backup.dmp LOGFILE=bolsa_log.log FULL=YES
```

### Restaurar Backup
```bash
impdp system/password DUMPFILE=bolsa_backup.dmp LOGFILE=restore.log FULL=YES
```

---

## Mantenimiento de la Base de Datos

### Ver Tamaño de Tablas
```sql
SELECT table_name, num_rows, blocks
FROM user_tables
ORDER BY num_rows DESC;
```

### Limpiar Datos de Prueba
```sql
DELETE FROM CV;
DELETE FROM CARACTERISTICA;
DELETE FROM PUESTO;
DELETE FROM OFERENTE;
DELETE FROM EMPRESA;
DELETE FROM ADMINISTRADOR;
COMMIT;
```

### Resetear Secuencias
```sql
DROP SEQUENCE empresa_seq;
CREATE SEQUENCE empresa_seq START WITH 1 INCREMENT BY 1;

DROP SEQUENCE oferente_seq;
CREATE SEQUENCE oferente_seq START WITH 1 INCREMENT BY 1;

-- Repetir para todas las secuencias
```

---

## Monitoreo de Performance

### Ver Consultas Lentas
```sql
SELECT sql_text, executions, elapsed_time
FROM v$sql
ORDER BY elapsed_time DESC;
```

### Ver Conexiones Activas
```sql
SELECT username, machine, program
FROM v$session
WHERE username IS NOT NULL;
```

### Analizar Tabla
```sql
ANALYZE TABLE empresa COMPUTE STATISTICS;
ANALYZE TABLE oferente COMPUTE STATISTICS;
```

---

## Configuración de Pool de Conexiones

```properties
# HikariCP (Pool por defecto en Spring Boot)
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

---

## Verificación Final

Después de configurar, ejecutar un test:

```bash
# En la carpeta del proyecto
./gradlew test
```

Debería completarse sin errores de conexión.

---

**Última actualización:** 18 de Marzo de 2026
