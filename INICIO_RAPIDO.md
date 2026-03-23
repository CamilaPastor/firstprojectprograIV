# ⚡ INICIO RÁPIDO - BOLSA DE EMPLEO

## 1. Verificar Requisitos

```bash
# Verificar Java 17
java -version

# Verificar Maven
mvn --version

# Verificar MySQL
mysql --version
```

## 2. Crear Base de Datos

```bash
# Conectar a MySQL
mysql -u root -p

# En la consola de MySQL:
CREATE DATABASE bolsa_empleo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'bolsa_user'@'localhost' IDENTIFIED BY 'bolsa_password';
GRANT ALL PRIVILEGES ON bolsa_empleo.* TO 'bolsa_user'@'localhost';
FLUSH PRIVILEGES;
```

## 3. Configurar Proyecto

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bolsa_empleo?useSSL=false&serverTimezone=UTC
spring.datasource.username=bolsa_user
spring.datasource.password=bolsa_password
```

## 4. Compilar e Instalar Dependencias

```bash
mvn clean install
```

## 5. Ejecutar la Aplicación

**Opción A - Con Maven:**
```bash
mvn spring-boot:run
```

**Opción B - Con Java:**
```bash
java -jar target/bolsa-empleo-1.0.0.jar
```

**Opción C - Desde IDE:**
- Click en ► Run (Green play button)
- O Shift+F10 (IntelliJ)

## 6. Acceder a la Aplicación

- 🌐 http://localhost:8080

## 📁 Estructura Base Creada

```
✅ pom.xml                          - Configuración Maven
✅ application.properties            - Propiedades de la app
✅ HomeController                    - Controlador ejemplo
✅ Usuario (Model)                   - Entidad de ejemplo
✅ UsuarioRepository                 - Repositorio de ejemplo
✅ UsuarioService                    - Servicio de ejemplo
✅ UsuarioDTO                        - DTO de ejemplo
✅ SecurityConfig                    - Seguridad configurada
✅ Templates HTML                    - Páginas de ejemplo
✅ CSS Responsive                    - Estilos base
✅ GlobalExceptionHandler            - Manejo de errores
✅ Tests unitarios                   - Ejemplos de testing
```

## 🎯 Comando Más Común

```bash
# Compilar + Instalar + Ejecutar
mvn clean install && java -jar target/bolsa-empleo-1.0.0.jar
```

## 🔧 Cambiar Puerto

Editar `application.properties`:
```properties
server.port=8081
```

## 📊 Ver Logs

En `application.properties`, cambiar niveles:
```properties
logging.level.cr.una.bolsaempleo=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 🐛 Limpiar Todo

```bash
mvn clean
```

Esto elimina la carpeta `target/` y prepara para compilar de nuevo.

## 💾 Crear JAR Ejecutable

```bash
mvn package
# El JAR estará en: target/bolsa-empleo-1.0.0.jar
```

## 🚀 Flujo de Desarrollo

1. **Crear Entidad** en `model/`
2. **Crear Repositorio** en `repository/`
3. **Crear Servicio** en `service/`
4. **Crear Controlador** en `controller/`
5. **Crear Template** en `templates/`
6. **Crear Tests** en `test/`

## 📚 Recursos Importantes

- 📖 `README_MAVEN.md` - Documentación completa
- 🛠️ `GUIA_DESARROLLO.md` - Convenciones y patrones
- 📝 `PROYECTO_SETUP_MAVEN.md` - Setup detallado

## ✅ Checklist Inicial

- [ ] Java 17 instalado
- [ ] Maven instalado
- [ ] MySQL en ejecución
- [ ] Base de datos creada
- [ ] application.properties configurado
- [ ] `mvn clean install` ejecutado sin errores
- [ ] Aplicación corre en http://localhost:8080
- [ ] Base de datos se conecta correctamente

## 🎓 Primeros Pasos

1. Entender estructura de carpetas
2. Revisar HomeController
3. Ver Usuario, UsuarioRepository, UsuarioService
4. Crear tu propia entidad siguiendo el patrón
5. Agregar vistas HTML con Thymeleaf
6. Escribir tests

## ⚠️ Problemas Comunes

| Problema | Solución |
|----------|----------|
| "Cannot find symbol" | `mvn clean install` |
| "Port 8080 in use" | Cambiar puerto en properties |
| "No database" | Crear DB con SQL arriba |
| "Connection refused" | Verificar MySQL está corriendo |
| "Permission denied" | Usar `sudo` en Linux/Mac |

## 📞 Soporte Rápido

1. Revisar los logs en consola
2. Buscar error en documentación
3. Revisar archivos de configuración
4. Intentar `mvn clean install` nuevamente

---

¡Listo para comenzar a desarrollar! 🚀
