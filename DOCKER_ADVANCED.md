# docker-compose con aplicación Dockerfile multi-stage optimizado

Este archivo proporciona ejemplos y variaciones de docker-compose para diferentes escenarios.

## 1. Desarrollo Local (docker-compose.yml actual)

```yaml
version: '3.9'
services:
  mysql:
    image: mysql:8.0
    # ... configuración ...
  
  app:
    build: .
    depends_on:
      mysql:
        condition: service_healthy
```

### Comando:
```bash
docker-compose up -d
```

## 2. Con Variables de Entorno Externas

Crear archivo `.env`:

```properties
# MySQL
MYSQL_ROOT_PASSWORD=root_secure_password
MYSQL_DATABASE=bolsa_empleo
MYSQL_USER=bolsa_user
MYSQL_PASSWORD=bolsa1234

# Spring Boot
SPRING_PROFILES_ACTIVE=docker
JAVA_XMX=512m
JAVA_XMS=256m

# Network
COMPOSE_PROJECT_NAME=bolsa_empleo_local
```

Usar en docker-compose.yml:

```yaml
environment:
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
  SPRING_DATASOURCE_PASSWORD: ${MYSQL_PASSWORD}
```

## 3. Dockerfile Multi-Stage Explicado

### Etapa 1: Builder

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Compilar sin tests (rápido)
RUN mvn clean package -DskipTests -q
```

Características:
- Alpine: imagen pequeña (150MB)
- Maven 3.9: versión reciente
- Java 17: última LTS
- `DskipTests`: omite tests en Docker

### Etapa 2: Runtime

```dockerfile
FROM eclipse-temurin:17-jre-alpine

# Copiar solo el JAR compilado
COPY --from=builder /app/target/bolsa-empleo-1.0.0.jar app.jar

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

Características:
- Solo JRE (no compilador, 90MB vs 400MB)
- Usuario no-root (seguridad)
- Alpine: imagen ultra-pequeña
- Tamaño final: ~170MB

## 4. Optimizaciones de Dockerfile

### Caché de dependencias de Maven

```dockerfile
# Copiar solo pom.xml primero
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código después
COPY src ./src
RUN mvn package -DskipTests -q
```

### Reducir tamaño de imagen

```dockerfile
# 1. Usar alpine
FROM eclipse-temurin:17-jre-alpine

# 2. Limpiar apk cache
RUN apk update && apk add --no-cache curl && rm -rf /var/cache/apk/*

# 3. Eliminar archivos innecesarios
RUN find / -name "*.md" -delete
```

## 5. Health Check Avanzado

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 30s
```

Para esto, agregar actuator en pom.xml:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 6. Volumes Avanzados

```yaml
volumes:
  # Volumen nombrado (persistente)
  mysql_data:
    driver: local
  
  # Bind mount para desarrollo
  # ./src:/app/src
  
  # Volumen temporal (tmpfs)
  # tmpfs: {}
```

## 7. Redes Personalizadas

```yaml
networks:
  bolsa_network:
    driver: bridge
    driver_opts:
      com.docker.network.bridge.name: br0
    ipam:
      config:
        - subnet: 172.20.0.0/16
          gateway: 172.20.0.1
```

## 8. Logging Avanzado

```yaml
services:
  app:
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
        labels: "app=bolsa_empleo"
```

## 9. Secrets en Producción

```yaml
# docker-compose.prod.yml
version: '3.9'
services:
  mysql:
    environment:
      MYSQL_PASSWORD_FILE: /run/secrets/mysql_password
    secrets:
      - mysql_password

secrets:
  mysql_password:
    external: true
    # O archivo local:
    # file: ./secrets/mysql.txt
```

Crear secret:
```bash
echo "bolsa1234" | docker secret create mysql_password -
```

## 10. Orquestación Avanzada

### Reinicio automático

```yaml
services:
  app:
    restart: always
    # Opciones: no, always, unless-stopped, on-failure
```

### Límites de recursos

```yaml
deploy:
  resources:
    limits:
      cpus: '1.0'          # 1 core
      memory: 768M
    reservations:
      cpus: '0.5'          # mínimo
      memory: 512M
```

### Escalado (Solo en Swarm)

```yaml
deploy:
  replicas: 3
  placement:
    constraints:
      - node.role == worker
```

## 11. Para Desarrollo con Hot Reload

Agregar en pom.xml:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

docker-compose.dev.yml:

```yaml
version: '3.9'
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile.dev  # Dockerfile optimizado para dev
    volumes:
      - ./src:/app/src           # Bind mount del código
      - ./target:/app/target     # Bind mount del target
    environment:
      SPRING_DEVTOOLS_RESTART_ENABLED: "true"
```

Dockerfile.dev:

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine

WORKDIR /app
COPY . .

EXPOSE 8080
CMD ["mvn", "spring-boot:run"]
```

Usar:
```bash
docker-compose -f docker-compose.dev.yml up
```

## 12. Integración con CI/CD

```yaml
# docker-compose.test.yml
version: '3.9'
services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    environment:
      SPRING_PROFILES_ACTIVE: test
  
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: bolsa_empleo_test
```

## 13. Registry Privado

```bash
# Login
docker login registry.example.com

# Tag image
docker tag bolsa-empleo:1.0.0 registry.example.com/bolsa-empleo:1.0.0

# Push
docker push registry.example.com/bolsa-empleo:1.0.0

# En docker-compose:
# image: registry.example.com/bolsa-empleo:1.0.0
```

## 14. Monitoreo con Prometheus + Grafana

```yaml
version: '3.9'
services:
  app:
    # ... configuración ...
    ports:
      - "8080:8080"
      - "9090:9090"  # Prometheus metrics
  
  prometheus:
    image: prom/prometheus:latest
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
  
  grafana:
    image: grafana/grafana:latest
    ports:
      - "3000:3000"
```

## 15. Recomendaciones Finales

### ✅ HACER:
- [x] Usar Alpine Linux para imágenes pequeñas
- [x] Multi-stage builds
- [x] Usuario no-root
- [x] Health checks configurados
- [x] Logging centralizado
- [x] Variables de entorno
- [x] Volúmenes persistentes

### ❌ NO HACER:
- [ ] Exponer contraseñas en Dockerfile
- [ ] Usar `root` como usuario
- [ ] Imágenes grandes innecesariamente
- [ ] Copiar .git, node_modules, etc
- [ ] Ejecutar `apt-get` en produción
- [ ] Sin health checks

---

**Referencia rápida:**
```bash
# Build
docker-compose build

# Levantar
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener
docker-compose down

# Con volumen
docker-compose down -v
```
