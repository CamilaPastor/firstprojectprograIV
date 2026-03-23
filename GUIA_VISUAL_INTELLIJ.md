# 🎬 GUÍA VISUAL: Ejecutar Bolsa de Empleo en IntelliJ IDEA

## INICIO RÁPIDO EN 5 MINUTOS

---

## PASO 1: Configurar JDK 17

### Pantalla 1: Abrir Project Structure

```
File Menu
├── Project Structure... (o Ctrl+Alt+Shift+S)
└── (Se abre ventana de Project Structure)
```

### Pantalla 2: Seleccionar SDK

```
Project Structure Window
├── Panel Izquierdo: Selecciona "Project"
├── Panel Principal:
│   └── SDK: [Dropdown]
│       ├── Ninguno
│       ├── 17 (si ya está instalado)
│       ├── Download JDK (para descargar)
│       └── Add JDK... (para agregar manual)
└── Botones abajo: [Cancel] [Apply] [OK]
```

**Si tienes JDK 17:**
- Haz clic en el dropdown de SDK
- Selecciona "17"
- Haz clic en "Apply" y luego "OK"

**Si necesitas descargarlo:**
- Haz clic en "Download JDK"
- Selecciona Version: 17, Vendor: Eclipse Temurin
- Haz clic en "Download" (espera 5-10 minutos)
- Luego "Apply" y "OK"

---

## PASO 2: Abrir el Proyecto

### Pantalla 1: Inicio de IntelliJ

```
Welcome Tab (si es primera vez)
├── Open Project
└── (Se abre file browser)

O desde File Menu:
├── File
├── Open (Ctrl+O)
└── (Se abre file browser)
```

### Pantalla 2: Seleccionar Carpeta

```
File Browser
├── Navega a:
│   C:\Users\camil\OneDrive\Documentos\PrograIV\Bolsa_Empleo
├── Haz clic en "Open"
└── Si pregunta "Trust Project?": haz clic en "Trust Project"
```

### Pantalla 3: Esperando Maven

```
IntelliJ Window (con proyecto abierto)
├── Abajo derecha aparecerá:
│   "Maven projects need to be imported"
│   [Load] [Ignore]
├── Haz clic en "Load"
└── La consola mostrará:
    "[INFO] Importing Maven modules...
     [INFO] Downloading dependencies...
     Progress bar..."
```

**⏳ Espera 2-5 minutos mientras Maven descarga las dependencias**

### Pantalla 4: Verificar Maven Cargado

```
View Menu
├── Tool Windows
├── Maven (o presiona Alt+M)
└── Panel Maven aparecerá mostrando:
    ├── bolsa-empleo
    │   ├── Lifecycle
    │   │   ├── clean
    │   │   ├── compile
    │   │   ├── test
    │   │   ├── package
    │   │   ├── install
    │   │   └── deploy
    │   ├── Dependencies
    │   └── Plugins
    └── ✅ EXCELENTE: Maven está cargado
```

---

## PASO 3: Crear Run Configuration

### Pantalla 1: Abrir el archivo main

```
Project Explorer (izquierda)
├── Expande carpetas:
│   src/main/java/cr/una/bolsaempleo/
└── Haz clic en: BolsaEmpleoApplication.java
    (Se abre el archivo en el editor principal)
```

### Pantalla 2: Buscar el método main

```
BolsaEmpleoApplication.java (editor)
├── @SpringBootApplication
├── public class BolsaEmpleoApplication {
├──     public static void main(String[] args) {  ← AQUÍ
├──         SpringApplication.run(...);
├──     }
└── }
```

### Pantalla 3: Crear Run Config

```
Busca el triángulo verde ▶ que aparecerá:
├── Al lado de "public static void main"
└── Haz clic en él y selecciona:
    "Run 'BolsaEmpleoApplication.main()'"
```

**¡IntelliJ automáticamente creará la configuración!**

---

## PASO 4: Iniciar MySQL en Docker

### En Terminal (NO en IntelliJ)

```bash
# Navega a la carpeta del proyecto
cd c:\Users\camil\OneDrive\Documentos\PrograIV\Bolsa_Empleo

# Inicia los contenedores
docker-compose up -d

# Verifica que MySQL está corriendo
docker-compose ps

# Output esperado:
# NAME     IMAGE      STATUS
# mysql    mysql:8.0  Up 2 minutes
```

---

## PASO 5: Ejecutar la Aplicación

### Opción A: Desde el botón Run (RECOMENDADO)

```
IntelliJ Window (esquina superior derecha)
├── Dropdown de configuraciones:
│   [BolsaEmpleoApplication] ▼
└── Botón Run (triángulo verde ▶)
    └── Haz clic en ▶
        O presiona: Shift+F10 (Windows)
```

### Opción B: Desde el Terminal integrado

```
View Menu
├── Tool Windows
├── Terminal
└── En el terminal que aparece:
    mvn spring-boot:run
```

---

## PASO 6: Verificar que Está Funcionando

### En la Consola de IntelliJ (parte inferior)

Busca estos mensajes en ESTE ORDEN:

```
✅ 1. [INFO] Starting BolsaEmpleoApplication v1.0.0
       La aplicación comienza a iniciar

✅ 2. [INFO] Tomcat initialized with port(s): 8080
       El servidor web Tomcat se está preparando

✅ 3. [INFO] HikariPool-1 - Starting...
       Inicia la conexión a la BD

✅ 4. [INFO] HikariPool-1 - Start completed.
       ¡MySQL está conectado!

✅ 5. [INFO] Started BolsaEmpleoApplication in 6.234 seconds
       ¡LA APLICACIÓN ESTÁ LISTA!
```

**Si ves TODOS estos mensajes: ✅ TODO FUNCIONA PERFECTAMENTE**

### Si ves algún ERROR:

```
❌ Error: "Connection refused"
   → MySQL no está corriendo
   → Abre terminal: docker-compose up -d

❌ Error: "Port 8080 already in use"
   → Otro programa usa el puerto
   → Cambia en application.properties: server.port=8081

❌ Error: "JDK not found"
   → Vuelve a PASO 1 y descarga JDK 17
```

---

## PASO 7: Abrir en Navegador

### Dirección

```
Abre tu navegador (Chrome, Firefox, Edge, etc.)
Escribe en la barra de direcciones:

http://localhost:8080
```

### Página de Inicio que Deberías Ver

```
┌─────────────────────────────────────────┐
│ Bolsa de Empleo (logo naranja)          │
├─────────────────────────────────────────┤
│                                         │
│  Encuentra tu Empleo Ideal Hoy Mismo   │
│  [Buscar Empleos] (botón naranja)      │
│                                         │
│  📊 10,000+ puestos | 5,000+ empresas  │
│     50,000+ oferentes                  │
│                                         │
│  [Cards de últimos 5 puestos]          │
│                                         │
│  ¿Listo para encontrar tu próximo...   │
│  [Soy Candidato] [Soy Empresa]        │
│                                         │
└─────────────────────────────────────────┘
```

**Si ves esto: ¡LA APLICACIÓN ESTÁ FUNCIONANDO!** 🎉

---

## PRUEBAS ADICIONALES

### Probar Login

```
En el navegador, ve a:
http://localhost:8080/login

Deberías ver:
┌────────────────────────────┐
│ Iniciar Sesión             │
├────────────────────────────┤
│ Tipo de Usuario:           │
│ [Empresa ▼]               │
│                            │
│ Usuario: [__________]      │
│                            │
│ Contraseña: [__________]  │
│                            │
│ [Iniciar Sesión]          │
│                            │
│ ¿No tienes cuenta?         │
│ [Registrar como...]        │
└────────────────────────────┘
```

### Probar Registro

```
En el navegador, ve a:
http://localhost:8080/registro/oferente

Si ves el formulario sin errores:
✅ MySQL está conectado correctamente
✅ Spring Boot está sirviendo templates
✅ Thymeleaf está renderizando HTML
```

---

## ⏸️ Detener la Aplicación

```
Cuando termines de desarrollar:

1. En IntelliJ, haz clic en el botón ⏹ (Stop)
   O presiona: Ctrl+F2

2. La consola mostrará:
   [INFO] Application run finished

3. El servidor HTTP se cerrará
```

---

## 🎓 RESUMEN

| Paso | Acción | Verificación |
|------|--------|--------------|
| 1 | Configurar JDK 17 | File → Project Structure muestra JDK 17 |
| 2 | Abrir proyecto | Maven cargado (View → Maven) |
| 3 | Crear Run Config | Run 'BolsaEmpleoApplication.main()' disponible |
| 4 | Iniciar MySQL | docker-compose ps muestra mysql UP |
| 5 | Ejecutar app | Console muestra "Started BolsaEmpleoApplication" |
| 6 | Verificar conectividad | Console muestra "HikariPool-1 - Start completed" |
| 7 | Probar en navegador | http://localhost:8080 carga correctamente |

---

## 🚀 ¡LISTO PARA DESARROLLAR!

Una vez que veas la página de inicio en http://localhost:8080, puedes:

✅ Modificar código Java y ver cambios automáticamente (con hot reload)
✅ Editar templates HTML y refrescar el navegador
✅ Agregar nuevas funcionalidades
✅ Probar la conexión a MySQL

**¡Felicidades, tu entorno está configurado correctamente!** 🎉

---

**Documentación**: GUIA_INTELLIJ_IDEA.md (para detalles completos)
**Fecha**: 20 de marzo de 2026
**Versión**: 1.0
