# Guía de Docker - Bolsa de Empleo

## 📋 Requisitos Previos

- Docker Desktop instalado (https://www.docker.com/products/docker-desktop)
- Docker Compose 1.29+ (incluido en Docker Desktop)
- 2GB de memoria RAM disponible

### Verificar instalación

```bash
docker --version
docker-compose --version
```

## 🏗️ Arquitectura Docker

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                       │
│                    (bolsa_network)                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────────────┐         ┌──────────────────┐    │
│  │   MySQL 8.0      │         │  Spring Boot     │    │
│  │                  │         │  App:8080        │    │
│  │  localhost:3306  │◄────────│                  │    │
│  │                  │         │                  │    │
│  └──────────────────┘         └──────────────────┘    │
│   bolsa_empleo_mysql           bolsa_empleo_app       │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## 🚀 Primeros Pasos

### 1. Construir la imagen (Opcional)

```bash
docker-compose build
```

Esta etapa:
- Compila el código fuente con Maven
- Genera el JAR ejecutable
- Crea la imagen Docker final

### 2. Iniciar los servicios

```bash
docker-compose up -d
```

Flags útiles:
- `-d` : Ejecutar en background
- `--build` : Reconstruir imágenes
- `-f docker-compose.yml` : Especificar archivo compose

### 3. Verificar estados

```bash
# Ver servicios en ejecución
docker-compose ps

# Ver logs
docker-compose logs -f

# Ver logs específicos de un servicio
docker-compose logs -f app
docker-compose logs -f mysql
```

### 4. Acceder a la aplicación

- 🌐 http://localhost:8080

### 5. Detener servicios

```bash
# Detener sin eliminar
docker-compose stop

# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar todo (incluido volúmenes)
docker-compose down -v
```

## 📊 Servicios en Detalle

### MySQL (Base de Datos)

**Imagen:** `mysql:8.0`
**Puertos:** `3306:3306`
**Salud:** Verificada con `mysqladmin ping`

Características:
- Autenticación: usuario `bolsa_user` / contraseña `bolsa1234`
- Base de datos: `bolsa_empleo`
- Volumen persistente: `mysql_data`
- Script de inicialización: `BolsaEmpleo.sql`
- Charset: UTF-8

Conectar desde host:

```bash
mysql -h localhost -u bolsa_user -p bolsa1234 bolsa_empleo
```

O desde dentro del contenedor:

```bash
docker-compose exec mysql mysql -u bolsa_user -p bolsa1234 bolsa_empleo
```

### Spring Boot App

**Build:** Dockerfile (multi-stage)
**Puertos:** `8080:8080`
**Salud:** Verificada con GET /

Características:
- Compilación: Maven 3.9 con Java 17
- Runtime: eclipse-temurin:17-jre-alpine
- Usuario no-root: `appuser`
- Memory: -Xmx512m -Xms256m

Conectar a logs:

```bash
docker-compose logs -f app
```

## 🔧 Comandos Útiles

### Ver toda la información

```bash
docker-compose ps              # Estado de servicios
docker-compose config          # Ver configuración actual
docker-compose events          # Ver eventos en tiempo real
```

### Ejecutar comandos en contenedores

```bash
# Ejecutar comando en MySQL
docker-compose exec mysql mysql -u bolsa_user -p bolsa1234

# Ejecutar comando en la app
docker-compose exec app sh

# Ver procesos en contenedor
docker-compose exec app ps aux
```

### Limpiar recursos

```bash
# Eliminar contenedores detenidos
docker-compose down

# Eliminar volúmenes también
docker-compose down -v

# Limpiar imágenes sin usar
docker image prune -a

# Limpiar todo (contenedores, imágenes, volúmenes, networks)
docker system prune -a --volumes
```

### Reconstruir después de cambios

```bash
# Opción 1: Desde cero
docker-compose down -v
docker-compose build --no-cache
docker-compose up -d

# Opción 2: Solo reconstruir app
docker-compose build app
docker-compose up -d app
```

## 📁 Estructura de Archivos

```
Bolsa_Empleo/
├── Dockerfile                  # Build multi-stage
├── docker-compose.yml          # Orquestación de servicios
├── .dockerignore              # Archivos a excluir del build
├── BolsaEmpleo.sql            # Script de inicialización
├── pom.xml                    # Configuración Maven
└── src/
    ├── main/
    │   ├── java/
    │   └── resources/
    │       └── application.properties
    └── test/
```

## 🔐 Variables de Entorno

### MySQL

```properties
MYSQL_ROOT_PASSWORD=root_password
MYSQL_DATABASE=bolsa_empleo
MYSQL_USER=bolsa_user
MYSQL_PASSWORD=bolsa1234
MYSQL_ROOT_HOST=%
```

### Spring Boot App

```properties
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/bolsa_empleo
SPRING_DATASOURCE_USERNAME=bolsa_user
SPRING_DATASOURCE_PASSWORD=bolsa1234
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_PROFILES_ACTIVE=docker
JAVA_OPTS=-Xmx512m -Xms256m
```

## 🔍 Health Checks

Ambos servicios tienen health checks configurados:

**MySQL:**
```bash
mysqladmin ping -h localhost -u bolsa_user -pbolsa1234
```

**App:**
```bash
wget --no-verbose --tries=1 --spider http://localhost:8080/
```

Ver estado:

```bash
docker-compose ps
```

Columna `STATUS` mostrará:
- `healthy` : Servicio en buena salud
- `unhealthy` : Servicio con problemas
- `starting` : Iniciando

## 📈 Monitoreo

### Ver consumo de recursos

```bash
docker stats
```

### Ver logs con filtros

```bash
# Últimas 100 líneas
docker-compose logs --tail=100

# Desde hace 10 minutos
docker-compose logs --since 10m

# Últimas líneas, tiempo seguido
docker-compose logs -f --timestamps
```

### Inspeccionar contenedor

```bash
docker inspect bolsa_empleo_app
docker inspect bolsa_empleo_mysql
```

## 🐛 Troubleshooting

### Problema: "Cannot connect to Docker daemon"

**Solución:**
- Verificar que Docker Desktop está corriendo
- En Linux: `sudo systemctl start docker`

### Problema: "Port already in use"

**Solución:**
```bash
# Encontrar proceso usando puerto
lsof -i :8080
lsof -i :3306

# Matar proceso
kill -9 <PID>

# O cambiar puerto en docker-compose.yml
# ports:
#   - "8081:8080"
```

### Problema: "app service fails to connect to mysql"

**Solución:**
- Verificar que MySQL está `healthy`
- Ver logs: `docker-compose logs mysql`
- Verificar credenciales en docker-compose.yml

### Problema: "MySQL initialization fails"

**Solución:**
- Verificar que BolsaEmpleo.sql existe
- Ver logs: `docker-compose logs mysql`
- Eliminar volumen e intentar: `docker-compose down -v`

### Problema: "Out of memory"

**Solución:**
- En Docker Desktop: Settings → Resources → aumentar Memory
- O reducir limites en docker-compose.yml:
```yaml
deploy:
  resources:
    limits:
      memory: 512M
```

## 🔄 Flujo de Desarrollo

### 1. Modificar código

```bash
# Editar archivos de código
vim src/main/java/...
```

### 2. Reconstruir imagen

```bash
docker-compose build app
```

### 3. Reiniciar servicio

```bash
docker-compose up -d app
```

### 4. Ver logs

```bash
docker-compose logs -f app
```

## 📦 Producción vs Desarrollo

### Para Desarrollo (Actual)

```bash
docker-compose up -d
```

### Para Producción

Crear `docker-compose.prod.yml`:

```yaml
version: '3.9'
services:
  app:
    image: registry.example.com/bolsa-empleo:1.0.0
    restart: always
    environment:
      SPRING_PROFILES_ACTIVE: prod
      JAVA_OPTS: "-Xmx1g -Xms512m"
```

Ejecutar:
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🔗 Networking

Los contenedores pueden comunicarse entre sí usando los nombres de servicio:

```
MySQL: mysql:3306
App:   app:8080
```

No usar `localhost` dentro de los contenedores, usar el nombre del servicio.

## 💾 Backup y Restore

### Backup de base de datos

```bash
docker-compose exec mysql mysqldump -u bolsa_user -pbolsa1234 bolsa_empleo > backup.sql
```

### Restore de base de datos

```bash
docker-compose exec -T mysql mysql -u bolsa_user -pbolsa1234 bolsa_empleo < backup.sql
```

## 📚 Recursos Útiles

- Docker Docs: https://docs.docker.com/
- Docker Compose: https://docs.docker.com/compose/
- Best Practices: https://docs.docker.com/develop/dev-best-practices/
- Alpine Linux: https://alpinelinux.org/

## ✅ Checklist

- [ ] Docker y Docker Compose instalados
- [ ] Dockerfile creado
- [ ] docker-compose.yml configurado
- [ ] BolsaEmpleo.sql en raíz del proyecto
- [ ] pom.xml presente
- [ ] Código compilable sin errores
- [ ] `docker-compose build` sin errores
- [ ] `docker-compose up -d` sin errores
- [ ] Ambos servicios en estado `healthy`
- [ ] Aplicación accesible en http://localhost:8080

---

¡Proyecto listo para deployarse en Docker! 🐳
