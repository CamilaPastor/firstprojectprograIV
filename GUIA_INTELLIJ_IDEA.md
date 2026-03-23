# 🚀 Guía: Ejecutar Bolsa de Empleo desde IntelliJ IDEA

## Requisitos Previos
✅ MySQL 8.0 corriendo en Docker en puerto 3306
✅ IntelliJ IDEA instalado (Community o Ultimate)
✅ JDK 17 instalado en la máquina
✅ Maven instalado en la máquina (o usar Maven integrado de IntelliJ)

---

## PASO 1: Configurar JDK 17 en IntelliJ IDEA

### Opción A: Descargar JDK 17 desde IntelliJ (Recomendado)

1. **Abre IntelliJ IDEA**

2. **Ve a: File → Project Structure** (o presiona Ctrl+Alt+Shift+S en Windows)

3. **En el panel izquierdo, selecciona "Project"**

4. **En "SDK", haz clic en el dropdown y selecciona "Download JDK"**

5. **Selecciona:**
   - Version: 17
   - Vendor: Eclipse Temurin (u Oracle OpenJDK)
   - Location: (dejar por defecto)

6. **Haz clic en "Download"** y espera a que termine

7. **Haz clic en "Apply" y luego "OK"**

### Opción B: Si JDK 17 ya está instalado

1. **File → Project Structure**

2. **Selecciona "Project" en el panel izquierdo**

3. **En "SDK", haz clic en el dropdown**

4. **Haz clic en "Add JDK..."**

5. **Busca la carpeta donde está instalado JDK 17:**
   - Windows: `C:\Program Files\Java\jdk-17` (o similar)
   - Mac: `/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home`
   - Linux: `/usr/lib/jvm/java-17-openjdk`

6. **Selecciona la carpeta y haz clic en "Open"**

7. **Verifica que dice "JDK version 17" en la ventana**

8. **Haz clic en "Apply" y luego "OK"**

### Verificar la configuración:

En **File → Project Structure → Project**, debe mostrarse:
```
SDK: 17 (o JDK 17)
Language level: 17
```

---

## PASO 2: Marcar pom.xml como Proyecto Maven

### Opción A: IntelliJ reconoce automáticamente el pom.xml

1. **Si abriste la carpeta `Bolsa_Empleo` como proyecto:**
   - IntelliJ debería reconocer automáticamente que es un proyecto Maven
   - Verás en la esquina inferior derecha: "Maven projects need to be imported"

2. **Haz clic en "Load" o "Import Changes"**

3. **Espera a que Maven descargue las dependencias** (puede tomar 2-5 minutos)

### Opción B: Importar manualmente

1. **File → Open** → Navega a la carpeta `Bolsa_Empleo` → **OK**

2. **Si IntelliJ pregunta "Trust Project?", haz clic en "Trust Project"**

3. **A la derecha, abre el panel "Maven"** (View → Tool Windows → Maven)

4. **Deberías ver el árbol Maven con:**
   ```
   bolsa-empleo
   ├── Dependencies
   ├── Plugins
   └── (otros módulos)
   ```

5. **Si ves "pom.xml" en rojo, haz clic derecho en la carpeta raíz y selecciona:**
   ```
   Maven → Reload Projects
   ```

### Verificar que Maven funciona:

En el panel Maven (esquina superior derecha), expande la carpeta:
```
Bolsa_Empleo
├── Lifecycle
   ├── clean
   ├── compile
   ├── test
   ├── package
   ├── install
   └── deploy
```

Si ves esto, ¡Maven está correctamente configurado!

---

## PASO 3: Crear Run Configuration para BolsaEmpleoApplication

### Método 1: Desde el archivo Java (Más fácil)

1. **Abre el archivo:**
   ```
   src/main/java/cr/una/bolsaempleo/BolsaEmpleoApplication.java
   ```

2. **Verifica que tenga la anotación `@SpringBootApplication`**

3. **Posiciónate en el método `main` y busca el botón verde ▶ (Run)**

4. **Haz clic en el triángulo verde ▶ junto al método `main`**

5. **Selecciona "Run 'BolsaEmpleoApplication.main()'"**

6. **¡IntelliJ creará automáticamente una Run Configuration!**

### Método 2: Crear manualmente

1. **Ve a: Run → Edit Configurations** (o presiona Alt+Shift+F10 en Windows)

2. **Haz clic en el botón "+" (Add New Configuration)**

3. **Selecciona "Spring Boot"** de la lista

4. **Completa los campos:**
   ```
   Name: BolsaEmpleoApplication
   Main class: cr.una.bolsaempleo.BolsaEmpleoApplication
   Working directory: (carpeta del proyecto)
   JRE: 17 (o la versión que configuraste)
   VM options: (dejar en blanco)
   Program arguments: (dejar en blanco)
   ```

5. **Haz clic en "Apply" y luego "OK"**

### Configuración adicional (Opcional pero Recomendado):

En la misma ventana de Run Configuration:
- **Enable "On 'Update' action"**: Marca esta opción para hot reload
- **Enable "On frame deactivation"**: Para recargar cambios automáticamente

---

## PASO 4: Ejecutar la Aplicación

### Opción A: Desde el botón Run

1. **Asegúrate que MySQL está corriendo en Docker:**
   ```bash
   docker-compose up -d
   ```

2. **En IntelliJ, busca el dropdown de configuraciones** (esquina superior derecha, donde dice "BolsaEmpleoApplication")

3. **Verifica que esté seleccionado "BolsaEmpleoApplication"**

4. **Haz clic en el botón ▶ (Run)** 
   - O presiona Shift+F10 en Windows
   - O presiona Ctrl+R en Mac

### Opción B: Desde el terminal integrado

1. **View → Tool Windows → Terminal**

2. **En el terminal, ejecuta:**
   ```bash
   mvn spring-boot:run
   ```

3. **O en Windows PowerShell:**
   ```powershell
   mvn.cmd spring-boot:run
   ```

---

## PASO 5: Verificar que la Aplicación Está Corriendo

### En la consola de IntelliJ, busca estos mensajes:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.3)

2026-03-20 14:30:00.123  INFO 12345 --- [           main] c.u.b.BolsaEmpleoApplication             : Starting BolsaEmpleoApplication
2026-03-20 14:30:02.456  INFO 12345 --- [           main] c.u.b.BolsaEmpleoApplication             : The following 1 profile is active: "default"
2026-03-20 14:30:05.789  INFO 12345 --- [           main] o.s.b.w.e.t.TomcatWebServer              : Tomcat initialized with port(s): 8080 (http)
2026-03-20 14:30:06.012  INFO 12345 --- [           main] o.s.b.w.e.t.TomcatWebServer              : Tomcat started on port(s): 8080 (http) with context path ''
2026-03-20 14:30:06.234  INFO 12345 --- [           main] c.u.b.BolsaEmpleoApplication             : Started BolsaEmpleoApplication in 6.111 seconds (JVM running for 6.456)
```

### Si ves estos mensajes, ¡la aplicación está corriendo! ✅

---

## PASO 6: Verificar Conexión a MySQL

### Opción A: Desde el navegador

1. **Abre http://localhost:8080**

2. **Deberías ver la página de inicio de la Bolsa de Empleo**

3. **Si ves el sitio web, MySQL está conectado correctamente ✅**

### Opción B: Desde los logs de Spring

1. **En la consola de IntelliJ, busca este mensaje:**
   ```
   INFO  --- [           main] o.h.e.t.j.p.t.JdbcEnvironmentInitializer : HikariPool-1 - Starting...
   INFO  --- [           main] o.h.e.t.j.p.t.JdbcEnvironmentInitializer : HikariPool-1 - Start completed.
   ```

2. **Este mensaje indica que la conexión a MySQL es exitosa ✅**

### Opción C: Desde la aplicación web

1. **En http://localhost:8080, intenta:**
   - Ir a "/login" y ver si carga el formulario
   - Intentar registrarte como oferente o empresa
   - Si la BD está conectada, guardará los datos ✅

### Si hay error de conexión:

Si ves este error:
```
Error: Could not get a connection, pool error Could not connect to address=(host=localhost),port=3306)
```

**Soluciones:**

1. **Verifica que MySQL está corriendo:**
   ```bash
   docker-compose ps
   ```
   Deberías ver el contenedor `mysql` como `UP`

2. **Si no está corriendo, inicia Docker:**
   ```bash
   docker-compose up -d
   ```

3. **Verifica que el puerto 3306 está disponible:**
   ```bash
   netstat -an | findstr :3306  # Windows
   lsof -i :3306               # Mac/Linux
   ```

4. **En application.properties, asegúrate que dice:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bolsa_empleo
   spring.datasource.username=bolsa_user
   spring.datasource.password=bolsa1234
   ```

5. **Reinicia la aplicación:**
   - Haz clic en el botón ⏹ (Stop) en IntelliJ
   - Espera 2 segundos
   - Haz clic en ▶ (Run) para ejecutar de nuevo

---

## PASO 7: Detener la Aplicación

### Cuando quieras parar la aplicación:

1. **En IntelliJ, haz clic en el botón ⏹ (Stop)** en la barra de herramientas

2. **O presiona Ctrl+F2**

3. **La consola mostrará:**
   ```
   2026-03-20 14:35:10.123  INFO  --- [       Thread-6] c.u.b.BolsaEmpleoApplication             : Application run finished
   ```

---

## 🎯 Resumen de Verificaciones

Cuando ejecutes la aplicación, verifica que:

- ✅ **IntelliJ muestra JDK 17** en File → Project Structure
- ✅ **Maven está cargado** (ves el árbol en el panel Maven)
- ✅ **La consola muestra "Tomcat started on port(s): 8080"**
- ✅ **La consola muestra "HikariPool-1 - Start completed"** (conexión BD)
- ✅ **Puedes acceder a http://localhost:8080**
- ✅ **MySQL está corriendo en Docker** (docker-compose ps)

---

## Problemas Comunes y Soluciones

### Problema: "Could not find main class"
**Solución:** Verifica que BolsaEmpleoApplication.java tiene:
```java
@SpringBootApplication
public class BolsaEmpleoApplication {
    public static void main(String[] args) {
        SpringApplication.run(BolsaEmpleoApplication.class, args);
    }
}
```

### Problema: "Maven projects need to be imported"
**Solución:** Haz clic en "Load" o "Import Changes"

### Problema: "Port 8080 already in use"
**Solución:** 
- Cambia el puerto en application.properties: `server.port=8081`
- O detén el proceso en el puerto 8080

### Problema: "Connection refused to host: localhost:3306"
**Solución:**
- Verifica que MySQL está en Docker: `docker-compose ps`
- Inicia Docker si no está corriendo: `docker-compose up -d`

---

## 🎉 ¡Lista para Desarrollar!

Una vez que veas http://localhost:8080 en el navegador con la página de inicio, podrás:

1. ✅ Ver la interfaz web con Thymeleaf
2. ✅ Hacer login/registro
3. ✅ Hacer cambios en el código y ver recargar automáticamente
4. ✅ Desarrollar nuevas funcionalidades

**¡Felicidades, tu aplicación Spring Boot está corriendo!** 🚀

---

**Última actualización**: 20 de marzo de 2026
**Versión**: 1.0
**Estado**: ✅ Listo para desarrollo
