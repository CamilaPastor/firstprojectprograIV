# Dockerfile Multi-Stage para Spring Boot 3.2.3 con Maven 3.9 y Java 17

# ============================
# ETAPA 1: BUILD (Compilación)
# ============================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

# Etiqueta de metadatos
LABEL stage=builder

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Copiar código fuente
COPY src ./src

# Compilar la aplicación sin ejecutar tests
RUN mvn clean package -DskipTests -q

# ==============================
# ETAPA 2: RUNTIME (Ejecución)
# ==============================
FROM eclipse-temurin:17-jre-alpine

# Metadatos
LABEL maintainer="Bolsa de Empleo"
LABEL version="1.0.0"
LABEL description="Aplicación Spring Boot 3.2.3 para Bolsa de Empleo"

# Variables de entorno para la JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Crear usuario no-root para mayor seguridad
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/bolsa-empleo-1.0.0.jar app.jar

# Cambiar propietario del archivo
RUN chown -R appuser:appuser /app

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
