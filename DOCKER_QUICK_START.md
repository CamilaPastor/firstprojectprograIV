# Docker - Inicio Rápido

## 🚀 Opción 1: Script Automático (RECOMENDADO)

### En Windows

```batch
docker-init.bat
```

O simplemente haz doble click en `docker-init.bat`

### En Linux/Mac

```bash
chmod +x docker-init.sh
./docker-init.sh
```

### ¿Qué hace el script?

1. ✓ Verifica que Docker está instalado
2. ✓ Verifica que Docker Compose está instalado
3. ✓ Verifica que todos los archivos necesarios existen
4. ✓ Construye la imagen Docker
5. ✓ Inicia los servicios (MySQL + App)
6. ✓ Muestra información de conexión

---

## 🚀 Opción 2: Comandos Manuales

### Paso 1: Verificar requisitos

```bash
# Verificar Docker
docker --version

# Verificar Docker Compose
docker-compose --version
```

Deberías ver:
- Docker version 20.10+ 
- Docker Compose version 1.29+

### Paso 2: Construir imagen

```bash
docker-compose build
```

Este comando:
- Lee el Dockerfile
- Etapa 1: Compila el código con Maven
- Etapa 2: Crea imagen runtime con JRE
- Resultado: imagen ~170MB

Tiempo estimado: 3-5 minutos

### Paso 3: Iniciar servicios

```bash
docker-compose up -d
```

Flags:
- `-d` : Ejecutar en background (detached)

Este comando:
- Levanta MySQL
- Levanta la aplicación Spring Boot
- Ambos en la red `bolsa_network`

### Paso 4: Verificar estado

```bash
docker-compose ps
```

Deberías ver:

```
NAME                    STATUS
bolsa_empleo_mysql      Up (healthy)
bolsa_empleo_app        Up (healthy)
```

### Paso 5: Acceder a la aplicación

```
http://localhost:8080
```

---

## 📊 Estados y Significados

### STATUS

- `Up (healthy)` : ✓ Servicio funcionando correctamente
- `Up (starting)` : ⏳ Servicio iniciando, espera un poco
- `Up (unhealthy)` : ⚠️ Servicio con problemas
- `Exited` : ✗ Servicio parado

### Si ves `unhealthy`:

```bash
# Ver logs para diagnosticar
docker-compose logs mysql
docker-compose logs app

# Reiniciar servicio
docker-compose restart mysql

# O ambos
docker-compose restart
```

---

## 🔗 Conexiones

### Desde el Host (Tu máquina)

```bash
# Acceder aplicación web
http://localhost:8080

# Conectar a MySQL
mysql -h localhost -u bolsa_user -p bolsa1234 bolsa_empleo
```

### Desde dentro de un contenedor

```bash
# Entrar al contenedor de la app
docker-compose exec app sh

# Entrar al contenedor de MySQL
docker-compose exec mysql bash

# Dentro del contenedor, usar nombre del servicio
mysql -h mysql -u bolsa_user -p bolsa1234 bolsa_empleo
```

---

## 🔧 Comandos Útiles

### Ver logs en tiempo real

```bash
# Todos los servicios
docker-compose logs -f

# Solo MySQL
docker-compose logs -f mysql

# Solo app
docker-compose logs -f app

# Últimas 100 líneas
docker-compose logs --tail=100
```

### Ejecutar comandos en contenedores

```bash
# Ver versión de Java en la app
docker-compose exec app java -version

# Ver variables de entorno
docker-compose exec app env | grep SPRING

# Listar archivos en la app
docker-compose exec app ls -la /app
```

### Detener servicios

```bash
# Solo detener (sin eliminar)
docker-compose stop

# Detener y eliminar contenedores
docker-compose down

# Detener, eliminar contenedores y volúmenes
docker-compose down -v
```

---

## 🔄 Flujo de Desarrollo

### 1. Cambiar código Java

```bash
# Editar archivo
vim src/main/java/...

# Reconstruir imagen
docker-compose build app

# Reiniciar servicio
docker-compose up -d app

# Ver logs
docker-compose logs -f app
```

### 2. Cambiar configuración

```bash
# Editar docker-compose.yml
vim docker-compose.yml

# Aplicar cambios
docker-compose up -d

# Verificar
docker-compose ps
```

### 3. Limpiar y empezar de cero

```bash
# Eliminar todo (contenedores + volúmenes)
docker-compose down -v

# Reconstruir desde cero
docker-compose build --no-cache

# Iniciar
docker-compose up -d

# Verificar
docker-compose ps
```

---

## 🗄️ Base de Datos

### Credenciales

- Host: `mysql` (desde contenedores) o `localhost` (desde host)
- Puerto: `3306`
- Usuario: `bolsa_user`
- Contraseña: `bolsa1234`
- Base de datos: `bolsa_empleo`

### Conectar desde host

```bash
# Con mysql-cli
mysql -h localhost -u bolsa_user -p

# Ingresar contraseña: bolsa1234
# Usar base de datos
USE bolsa_empleo;

# Ver tablas
SHOW TABLES;
```

### Conectar desde Docker

```bash
# Entrar al contenedor
docker-compose exec mysql bash

# Conectar a MySQL
mysql -u bolsa_user -p bolsa1234

# Ingresar contraseña: bolsa1234
```

### Backup y Restore

```bash
# Backup
docker-compose exec mysql mysqldump -u bolsa_user -pbolsa1234 bolsa_empleo > backup.sql

# Restore
docker-compose exec -T mysql mysql -u bolsa_user -pbolsa1234 bolsa_empleo < backup.sql
```

---

## 🐛 Solución de Problemas

### Problema: "Cannot connect to Docker daemon"

**Solución:**
- Abrir Docker Desktop
- Esperar a que se inicie
- Intentar comando nuevamente

### Problema: "Port 8080 is already allocated"

**Solución:**

Opción 1: Usar otro puerto
```yaml
# En docker-compose.yml
ports:
  - "8081:8080"
```

Opción 2: Liberar el puerto
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows (PowerShell como admin)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Problema: "MySQL fails to start"

**Solución:**
```bash
# Ver logs detallados
docker-compose logs mysql

# Limpiar volumen
docker-compose down -v

# Reconstruir
docker-compose up -d
```

### Problema: "App no puede conectar a MySQL"

**Solución:**
```bash
# Verificar que MySQL está healthy
docker-compose ps

# Ver logs de la app
docker-compose logs app

# Verificar conectividad dentro del contenedor
docker-compose exec app ping mysql

# Reiniciar ambos servicios
docker-compose restart
```

### Problema: "Out of memory"

**Solución:**
- Docker Desktop → Settings → Resources → aumentar Memory
- O reducir límites en docker-compose.yml

---

## 📈 Monitoreo

### Ver recursos utilizados

```bash
docker stats
```

Presiona `Ctrl+C` para salir.

### Ver información detallada de contenedores

```bash
# Ver configuración de un contenedor
docker inspect bolsa_empleo_app
docker inspect bolsa_empleo_mysql

# Ver variables de entorno
docker-compose exec app env
```

---

## 📚 Archivos Importantes

- `Dockerfile` : Construcción de imagen
- `docker-compose.yml` : Orquestación de servicios
- `.dockerignore` : Archivos a excluir del build
- `BolsaEmpleo.sql` : Script de inicialización de BD
- `docker-init.sh` / `docker-init.bat` : Scripts automáticos
- `DOCKER_GUIDE.md` : Guía completa de Docker

---

## ✅ Checklist

- [ ] Docker Desktop instalado y corriendo
- [ ] Todos los archivos en su lugar
- [ ] `docker-compose build` completado
- [ ] `docker-compose up -d` sin errores
- [ ] `docker-compose ps` muestra ambos servicios `healthy`
- [ ] http://localhost:8080 accesible
- [ ] MySQL accesible desde localhost:3306

---

## 🎯 Próximos Pasos

1. **Verificar que todo funciona:**
   ```bash
   docker-compose ps
   ```

2. **Acceder a la aplicación:**
   ```
   http://localhost:8080
   ```

3. **Ver logs en tiempo real:**
   ```bash
   docker-compose logs -f app
   ```

4. **Hacer cambios en el código y reconstruir:**
   ```bash
   docker-compose build app && docker-compose up -d app
   ```

---

¡Listo para containerizar tu aplicación! 🐳
