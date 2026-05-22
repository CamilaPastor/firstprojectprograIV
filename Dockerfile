FROM node:20-alpine AS frontend-builder

LABEL stage=frontend-builder

WORKDIR /frontend

COPY frontend/ ./

RUN npm install && mkdir -p /tmp/static && \
    npx vite build --outDir /tmp/static --emptyOutDir

FROM maven:3.9-eclipse-temurin-17-alpine AS backend-builder

LABEL stage=backend-builder

WORKDIR /app

COPY pom.xml .

COPY src ./src

COPY --from=frontend-builder /tmp/static ./src/main/resources/static

RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Bolsa de Empleo"
LABEL version="1.0.0"
LABEL description="Spring Boot 3.2.3 REST API + React SPA"

ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

RUN addgroup -S appuser && adduser -S appuser -G appuser

WORKDIR /app

COPY --from=backend-builder /app/target/bolsa-empleo-1.0.0.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
