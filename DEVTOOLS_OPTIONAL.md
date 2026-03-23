# Spring Boot DevTools - Hot Reload

Este archivo contiene la configuración para agregar Spring Boot DevTools
que permite recargar cambios sin reiniciar la aplicación.

## ¿Qué es DevTools?

DevTools proporciona actualizaciones rápidas durante el desarrollo:
- Cambios en templates (Thymeleaf) se recargan automáticamente
- Cambios en CSS se aplican sin reiniciar
- No requiere reiniciar el servidor

## Cómo Agregarlo

### Opción 1: Agregar a pom.xml (RECOMENDADO)

Agregar esta dependencia en la sección `<dependencies>` del pom.xml:

```xml
<!-- Spring Boot DevTools (opcional para desarrollo) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Opción 2: Compilar con Maven después de agregar

```bash
mvn clean install
```

## Configurar en IntelliJ IDEA

1. File → Settings → Build, Execution, Deployment → Compiler
2. Marcar: "Build project automatically"
3. Ctrl+Shift+Alt+/ (o Cmd+Shift+Alt+/ en Mac)
4. Seleccionar "Registry"
5. Buscar: "compiler.automake.allow.when.app.running"
6. Marcar el checkbox

## Configurar en Eclipse

1. Window → Preferences
2. General → Workspace
3. Marcar: "Refresh using native hooks or polling"
4. Marcar: "Build automatically"

## Uso

Simplemente edita tus archivos y guarda. Los cambios se aplicarán automáticamente:

- Cambios en HTML/CSS: instantáneo
- Cambios en Java: requiere compilación (automática en IDE)

## Notas

- DevTools solo funciona en modo desarrollo
- No se incluye en el JAR final (scope=runtime)
- Aumenta ligeramente el tiempo de compilación

## Desactivar DevTools

Si quieres desactivar DevTools temporalmente:

En application.properties:
```properties
spring.devtools.restart.enabled=false
```

O en application-prod.properties:
```properties
spring.devtools.restart.enabled=false
```

## Alternativa: Live Reload

Si quieres usar Live Reload con complemento de navegador:

1. Instalar complemento "LiveReload" en tu navegador
2. DevTools tiene soporte built-in para Live Reload

---

Para más info: https://spring.io/projects/spring-boot#learn
