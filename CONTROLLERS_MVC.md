# 🎮 Controllers MVC - Bolsa de Empleo

## Descripción General

Se han creado 5 controllers MVC en `cr.una.bolsaempleo.controller`:

1. **PublicController** - Páginas públicas
2. **LoginController** - Autenticación manual con HttpSession
3. **EmpresaController** - Dashboard y funciones para empresas
4. **OferenteController** - Dashboard y funciones para oferentes
5. **AdminController** - Administración del sistema

---

## 🔐 Sistema de Sesión Manual

### SessionUser DTO
```java
@Data
public class SessionUser {
    private Integer id;
    private String nombre;
    private String correo;
    private String tipoUsuario; // "empresa", "oferente", "admin"
}
```

### SessionUtil
Utility class con métodos helper:
```java
SessionUser getSessionUser(HttpSession session)
void setSessionUser(HttpSession session, SessionUser usuario)
boolean isLoggedIn(HttpSession session)
boolean isTipo(HttpSession session, String tipoUsuario)
void invalidateSession(HttpSession session)
```

---

## 📋 Controllers Creados

### 1. **PublicController**

Páginas públicas accesibles sin autenticación.

**Rutas:**
- `GET /` - Página de inicio con últimos 5 puestos públicos
- `GET /puestos/buscar-por-caracteristicas?ids=1,2,3` - Busca puestos por características
- `GET /registro/empresa` - Formulario registro empresa
- `POST /registro/empresa` - Registrar empresa
- `GET /registro/oferente` - Formulario registro oferente
- `POST /registro/oferente` - Registrar oferente

**Ejemplo:**
```html
<!-- GET / muestra últimos 5 puestos -->
<div th:each="puesto : ${puestos}">
  <h3 th:text="${puesto.empresa.nombre}"></h3>
  <p th:text="${puesto.descripcion}"></p>
</div>

<!-- GET /puestos/buscar-por-caracteristicas -->
<form method="get" action="/puestos/buscar-por-caracteristicas">
  <input type="hidden" name="ids" value="8,14,11" />
  <button>Buscar</button>
</form>
```

---

### 2. **LoginController**

Autenticación manual usando HttpSession.

**Rutas:**
- `GET /login` - Formulario de login
- `POST /login` - Procesar login (tipo, usuario, password)
- `GET /logout` - Cerrar sesión

**Parámetros POST /login:**
```
tipo: "empresa" | "oferente" | "admin"
usuario: correo (empresa/oferente) o identificación (admin)
password: contraseña sin hashear
```

**Lógica:**
1. Valida credenciales contra BD (BCrypt)
2. Crea SessionUser
3. Guarda en HttpSession
4. Redirige al dashboard correspondiente

**Ejemplo:**
```html
<form method="post" action="/login">
  <select name="tipo">
    <option value="empresa">Empresa</option>
    <option value="oferente">Oferente</option>
    <option value="admin">Administrador</option>
  </select>
  <input type="text" name="usuario" placeholder="Correo o ID" />
  <input type="password" name="password" placeholder="Contraseña" />
  <button>Iniciar sesión</button>
</form>
```

---

### 3. **EmpresaController** (`@RequestMapping("/empresa")`)

Dashboard y funciones para empresas.

**Verificación:** Todos los métodos verifican que `SessionUser.tipoUsuario == "empresa"`

**Rutas:**
- `GET /empresa/dashboard` - Dashboard empresa
- `GET /empresa/mis-puestos` - Listar mis puestos
- `GET /empresa/nuevo-puesto` - Formulario crear puesto
- `POST /empresa/nuevo-puesto` - Crear puesto con características
- `POST /empresa/desactivar` - Desactivar puesto
- `GET /empresa/buscar-candidatos?idPuesto=1` - Buscar candidatos
- `GET /empresa/detalle-candidato/{idOferente}` - Ver detalles de candidato

**Ejemplo: Crear puesto con características**
```html
<form method="post" action="/empresa/nuevo-puesto">
  <input name="descripcion" />
  <input name="salario" type="number" />
  
  <!-- Características requeridas -->
  <select name="caracteristicas" multiple>
    <option value="8">Java</option>
    <option value="14">Spring Boot</option>
  </select>
  
  <!-- Niveles requeridos -->
  <input type="hidden" name="niveles" value="4,4" />
  
  <button>Publicar</button>
</form>
```

**Ejemplo: Buscar candidatos**
```
GET /empresa/buscar-candidatos?idPuesto=1

Retorna lista de ResultadoCandidato:
- oferente
- cumplidos (requisitos cumplidos)
- total
- porcentaje (cumplidos/total * 100)
Ordenados: mayor porcentaje primero
```

---

### 4. **OferenteController** (`@RequestMapping("/oferente")`)

Dashboard y funciones para oferentes.

**Verificación:** Todos los métodos verifican que `SessionUser.tipoUsuario == "oferente"`

**Rutas:**
- `GET /oferente/dashboard` - Dashboard oferente
- `GET /oferente/habilidades` - Listar mis habilidades
- `GET /oferente/agregar-habilidad` - Formulario agregar habilidad
- `POST /oferente/agregar-habilidad` - Agregar habilidad
- `POST /oferente/eliminar-habilidad` - Eliminar habilidad
- `GET /oferente/mi-cv` - Ver mi CV
- `GET /oferente/subir-cv` - Formulario subir CV
- `POST /oferente/subir-cv` - Subir CV (MultipartFile PDF)
- `GET /oferente/descargar-cv/{idOferente}` - Descargar CV (ResponseEntity<byte[]>)

**Ejemplo: Subir CV**
```html
<form method="post" action="/oferente/subir-cv" enctype="multipart/form-data">
  <input type="file" name="archivo" accept=".pdf" />
  <button>Subir CV</button>
</form>
```

**Ejemplo: Descargar CV**
```java
// Retorna ResponseEntity con:
// - Content-Type: application/pdf
// - Content-Disposition: attachment
// - Body: byte[] del PDF
@GetMapping("/descargar-cv/{idOferente}")
public ResponseEntity<byte[]> descargarCv(@PathVariable Integer idOferente)
```

---

### 5. **AdminController** (`@RequestMapping("/admin")`)

Administración del sistema.

**Verificación:** Todos los métodos verifican que `SessionUser.tipoUsuario == "admin"`

**Rutas:**
- `GET /admin/dashboard` - Dashboard admin (estadísticas)
- `GET /admin/empresas-pendientes` - Listar empresas sin aprobar
- `POST /admin/aprobar-empresa?idEmpresa=1` - Aprobar empresa
- `GET /admin/oferentes-pendientes` - Listar oferentes sin aprobar
- `POST /admin/aprobar-oferente?idOferente=1` - Aprobar oferente
- `GET /admin/caracteristicas?idPadre=1` - Ver características
- `GET /admin/crear-caracteristica?idPadre=1` - Formulario crear característica
- `POST /admin/crear-caracteristica` - Crear característica
- `GET /admin/reportes` - Página de reportes
- `GET /admin/generar-reporte?anio=2026&mes=3` - Descargar PDF reporte mensual

**Ejemplo: Generar reporte**
```java
// Retorna ResponseEntity con PDF
// Parámetros: anio, mes
// Archivo: Reporte_Puestos_2026_03.pdf
@GetMapping("/generar-reporte")
public ResponseEntity<byte[]> generarReporte(@RequestParam Integer anio,
                                            @RequestParam Integer mes)
```

---

## 🔒 Verificación de Sesión

Todos los controllers protegidos (empresa, oferente, admin) verifican sesión:

```java
private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
    if (!SessionUtil.isTipo(session, "empresa")) {
        SessionUtil.addErrorMessage(attributes, "Debes iniciar sesión como empresa");
        return null;
    }
    return SessionUtil.getSessionUser(session);
}

@GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
    SessionUser user = verificarSesion(session, attributes);
    if (user == null) return "redirect:/login";
    
    // Código del dashboard
}
```

---

## 📊 Flujo de Login

```
1. Usuario accede a GET /login
2. Ve formulario con 3 opciones (empresa, oferente, admin)
3. Completa: tipo, usuario, password
4. POST /login recibe parámetros
5. Valida credenciales contra BD (con BCrypt)
6. Si válido:
   - Crea SessionUser
   - Guarda en HttpSession
   - Redirige a /empresa/dashboard, /oferente/dashboard o /admin/dashboard
7. Si inválido:
   - Muestra error
   - Redirecciona a GET /login
```

---

## 🎯 Flujo de Registro

```
Empresa:
1. GET /registro/empresa → muestra formulario
2. POST /registro/empresa → registra (hashea password)
3. Redirige a /login

Oferente:
1. GET /registro/oferente → muestra formulario
2. POST /registro/oferente → registra (hashea password)
3. Redirige a /login

Admin aprueba antes de poder acceder
```

---

## 📂 Estructura de Vistas (templates)

```
resources/templates/
├── index.html                          (GET /)
├── login/
│   └── login.html                      (GET/POST /login)
├── registro/
│   ├── empresa.html                    (GET/POST /registro/empresa)
│   └── oferente.html                   (GET/POST /registro/oferente)
├── empresa/
│   ├── dashboard.html                  (GET /empresa/dashboard)
│   ├── mis-puestos.html
│   ├── nuevo-puesto.html
│   ├── candidatos.html
│   └── detalle-candidato.html
├── oferente/
│   ├── dashboard.html
│   ├── habilidades.html
│   ├── agregar-habilidad.html
│   ├── mi-cv.html
│   └── subir-cv.html
└── admin/
    ├── dashboard.html
    ├── empresas-pendientes.html
    ├── oferentes-pendientes.html
    ├── caracteristicas.html
    ├── crear-caracteristica.html
    └── reportes.html
```

---

## ⚙️ Configuración Necesaria

### application.properties
```properties
# Sesión
server.servlet.session.timeout=1800
spring.session.cookie.max-age=1800
spring.session.cookie.http-only=true
spring.session.cookie.same-site=Strict

# Multipart file uploads
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

### Thymeleaf en pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

---

## 🌐 Integración con Thymeleaf

**Acceder a SessionUser en templates:**
```html
<!-- Obtener SessionUser de sesión -->
<div th:if="${session.usuario != null}">
  <p>Bienvenido <span th:text="${session.usuario.nombre}"></span></p>
  <p th:text="${session.usuario.tipoUsuario}"></p>
</div>
```

**Mensajes flash:**
```html
<!-- Mostrar mensaje de éxito -->
<div th:if="${success != null}" class="alert alert-success">
  <span th:text="${success}"></span>
</div>

<!-- Mostrar mensaje de error -->
<div th:if="${error != null}" class="alert alert-danger">
  <span th:text="${error}"></span>
</div>
```

---

## 📝 Ejemplo de Flujo Completo

### Empresa publica puesto y revisa candidatos:

```
1. Empresa POST /registro/empresa
   ├─ Se registra (password hasheado)
   └─ Admin aprueba

2. Empresa POST /login (tipo=empresa, usuario=correo, password=pwd)
   ├─ Valida credenciales
   ├─ Crea SessionUser
   ├─ Guarda en session
   └─ Redirige a GET /empresa/dashboard

3. GET /empresa/nuevo-puesto
   └─ Muestra formulario

4. POST /empresa/nuevo-puesto
   ├─ Guarda puesto
   ├─ Guarda características requeridas
   └─ Redirige a GET /empresa/mis-puestos

5. GET /empresa/buscar-candidatos?idPuesto=1
   ├─ Ejecuta BusquedaService.buscarCandidatos()
   ├─ Retorna List<ResultadoCandidato>
   ├─ Ordenados por porcentaje (mayor a menor)
   └─ Muestra tabla con oferentes

6. GET /empresa/detalle-candidato/1
   ├─ Muestra datos del oferente
   ├─ Muestra habilidades
   └─ Opción para descargar CV
```

---

## 🚀 Próximos Pasos

1. Crear templates Thymeleaf
2. Configurar Bootstrap o CSS
3. Implementar validaciones en formularios
4. Agregar encriptación de sesión
5. Implementar CSRF protection
6. Crear interceptor para verificación automática

---

**Última actualización**: 19 de marzo de 2026
**Versión**: 1.0 - Completo
**Estado**: ✅ Listo para integrar con templates Thymeleaf
