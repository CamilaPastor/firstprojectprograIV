# EmpresaController - Documentación Completa

## 📋 Descripción

`EmpresaController` es el controlador Spring MVC que maneja todas las operaciones relacionadas con empresas. Proporciona endpoints REST para registro, login, dashboard y gestión de perfil.

**Responsabilidades:**
- Manejo de solicitudes HTTP
- Validación de entrada
- Integración con EmpresaService
- Gestión de sesiones
- Renderizado de vistas

---

## 🔑 3 Endpoints Principales Solicitados

### 1️⃣ GET /empresa/register

**Descripción:** Muestra el formulario de registro de empresa

**Parámetros:** Ninguno

**Respuesta:**
- Vista: `empresa/register.html`
- Modelo: Empresa vacío para binding del formulario

**Ejemplo de Uso:**
```
GET http://localhost:8080/empresa/register
```

**Código:**
```java
@GetMapping("/register")
public String showRegistrationForm(Model model) {
    model.addAttribute("empresa", new Empresa());
    return "empresa/register";
}
```

---

### 2️⃣ POST /empresa/register

**Descripción:** Procesa el registro de una nueva empresa

**Parámetros:** Form data con objeto Empresa
- `nombre` - String (requerido)
- `correo` - String (requerido)
- `passwordHash` - String (requerido)
- `telefono` - String (opcional)
- `localizacion` - String (opcional)
- `descripcion` - String (opcional)

**Validaciones Realizadas:**
1. ✅ Nombre no vacío
2. ✅ Email no vacío
3. ✅ Password no vacío
4. ✅ Email válido (validación en servicio)
5. ✅ Email único (validación en servicio)

**Respuesta Exitosa:**
- Redirect: `/empresa/login`
- Sesión: Vacía (usuario debe login)

**Respuesta Fallida:**
- Vista: `empresa/register`
- Modelo: Error message + datos enviados

**Ejemplo de Uso:**
```html
<form action="/empresa/register" method="post">
    <input type="text" name="nombre" value="Tech Company" />
    <input type="email" name="correo" value="info@tech.com" />
    <input type="password" name="passwordHash" value="password123" />
    <button type="submit">Registrar</button>
</form>
```

**Manejo de Errores:**
```java
try {
    Empresa registrada = empresaService.registerEmpresa(empresa);
    // Redirect a login
} catch (IllegalArgumentException e) {
    // Mostrar error de validación
} catch (SQLException e) {
    // Mostrar error de BD
}
```

---

### 3️⃣ GET /empresa/dashboard

**Descripción:** Muestra el dashboard de la empresa (requiere autenticación)

**Parámetros:** Ninguno (requiere sesión)

**Autenticación Requerida:**
- Session attribute `empresaId` debe estar presente
- Session attribute `empresaNombre` debe estar presente

**Validaciones:**
1. ✅ Usuario autenticado (verificar sesión)
2. ✅ Empresa existe en BD
3. ✅ Empresa está aprobada
4. ✅ Datos de sesión coinciden con BD

**Respuesta Exitosa:**
- Vista: `empresa/dashboard.html`
- Modelo:
  - `empresa` - Objeto Empresa con datos completos
  - `empresaNombre` - Nombre de la empresa
  - `empresaCorreo` - Email de la empresa

**Respuesta Fallida:**
- Sin sesión → Redirect: `/empresa/login`
- Empresa no encontrada → Redirect: `/empresa/login` + Sesión invalidada
- Empresa no aprobada → Error message + Redirect: `/empresa/login`

**Ejemplo de Uso:**
```
GET http://localhost:8080/empresa/dashboard
Session: empresaId=123, empresaNombre=Tech Corp
```

**Código:**
```java
@GetMapping("/dashboard")
public String showDashboard(HttpSession session, Model model) {
    // 1. Verificar sesión
    Object empresaIdObj = session.getAttribute("empresaId");
    if (empresaIdObj == null) {
        return "redirect:/empresa/login";
    }
    
    // 2. Obtener datos de BD
    Optional<Empresa> empresa = empresaService.obtenerEmpresaPorId(...);
    
    // 3. Verificar aprobación
    if (!empresa.get().getAprobado()) {
        return "redirect:/empresa/login";
    }
    
    // 4. Agregar a modelo
    model.addAttribute("empresa", empresa.get());
    
    return "empresa/dashboard";
}
```

---

## 📚 Métodos Adicionales

### GET /empresa/login
```java
@GetMapping("/login")
public String showLoginForm(Model model)
```
Muestra formulario de login

### POST /empresa/login
```java
@PostMapping("/login")
public String processLogin(String correo, String password, HttpSession session, Model model)
```
Procesa login y establece sesión

**Validaciones:**
- Email y password no vacíos
- Credenciales válidas (servicio)
- Empresa aprobada

**Respuesta:**
- Éxito → Crear sesión + Redirect: `/empresa/dashboard`
- Fallo → Error message + Vista: `empresa/login`

### GET /empresa/logout
```java
@GetMapping("/logout")
public String logout(HttpSession session)
```
Cierra sesión del usuario
- Invalida la sesión
- Redirect: `/`

### GET /empresa/profile
```java
@GetMapping("/profile")
public String showProfile(HttpSession session, Model model)
```
Muestra perfil de la empresa
- Requiere autenticación
- Permite editar información

### POST /empresa/profile
```java
@PostMapping("/profile")
public String updateProfile(Empresa empresa, HttpSession session, Model model)
```
Actualiza perfil de la empresa
- Requiere autenticación
- No permite cambiar email ni password

---

## 🔐 Seguridad Implementada

### Autenticación
- ✅ Verificación de sesión en endpoints protegidos
- ✅ Login con credenciales cifradas
- ✅ Validación de aprobación de empresa

### Datos Sensibles
- ✅ Password nunca se devuelve
- ✅ Email no editable en perfil
- ✅ Password no editable en dashboard

### Session Management
- ✅ Atributos: empresaId, empresaNombre, empresaCorreo
- ✅ Timeout automático
- ✅ Invalidación en logout

### Logging
- ✅ Todos los endpoints loguean operaciones
- ✅ Errores de seguridad se registran
- ✅ Accesos exitosos y fallidos se registran

---

## 💻 Integración con Servicio

El controlador usa `EmpresaService` para:

```java
// Registro
empresaService.registerEmpresa(empresa)
    → Validación completa
    → Cifrado de password
    → Guardado en BD
    → Retorna Empresa con ID

// Login
empresaService.validateLogin(correo, password)
    → Búsqueda de empresa
    → Verificación de password
    → Verificación de aprobación
    → Retorna Optional<Empresa>

// Obtener datos
empresaService.obtenerEmpresaPorId(id)
    → Busca en BD
    → Retorna Optional<Empresa>

// Actualizar
empresaService.actualizarPerfil(empresa)
    → Validación
    → Guardado de cambios
    → Retorna boolean
```

---

## 🎯 Flujos de Negocio

### Flujo 1: Registro Completo

```
┌──────────────────┐
│ GET /registro    │
└────────┬─────────┘
         │
         ▼
    Mostrar formulario
         │
         ▼
┌──────────────────────┐
│ POST /registro       │
│ • Datos empresa      │
└────────┬─────────────┘
         │
         ▼
    registerEmpresa()
    • Validar datos
    • Verificar email
    • Cifrar password
    • Guardar en BD
         │
      ┌──┴──┐
      │     │
     OK    ERROR
      │     │
      ▼     ▼
   /login  /register + error
```

### Flujo 2: Login y Dashboard

```
┌──────────────────┐
│ GET /login       │
└────────┬─────────┘
         │
         ▼
    Mostrar formulario
         │
         ▼
┌──────────────────────┐
│ POST /login          │
│ • Email y password   │
└────────┬─────────────┘
         │
         ▼
    validateLogin()
    • Buscar empresa
    • Verificar password
    • Verificar aprobación
         │
      ┌──┴───────┐
      │          │
     OK        ERROR
      │          │
      ▼          ▼
   Crear      /login
   sesión     + error
      │
      ▼
   /dashboard
      │
      ▼
   Mostrar dashboard
```

---

## 📊 Estructura de Sesión

```java
// Después de login exitoso
session.setAttribute("empresaId", 123L);
session.setAttribute("empresaNombre", "Tech Corp");
session.setAttribute("empresaCorreo", "info@tech.com");

// En dashboard y otros endpoints protegidos
Long empresaId = (Long) session.getAttribute("empresaId");

// En logout
session.invalidate();
```

---

## ⚠️ Excepciones Manejadas

```
IllegalArgumentException
├─ Email ya registrado
├─ Nombre inválido
├─ Email inválido
└─ Teléfono inválido

SQLException
├─ Error de conexión BD
├─ Error en query
└─ Error al guardar datos

IllegalStateException
└─ Empresa no aprobada

Exception
└─ Errores inesperados
```

---

## 📱 Vistas HTML

### registro.html
- Formulario de registro
- Campos: nombre, email, password, teléfono, localización, descripción
- Validación frontend
- Mensajes de error

### login.html
- Formulario de login simple
- Campos: email, password
- Mensaje de bienvenida
- Link a registro

### dashboard.html
- Información de empresa
- Estado de aprobación
- Acciones rápidas
- Estadísticas (si aprobada)
- Links a crear ofertas

### profile.html
- Edición de perfil
- Campos no editables: nombre, email
- Campos editables: teléfono, localización, descripción
- Opciones de seguridad

---

## 🚀 Ejemplos de Uso

### Ejemplo 1: Registro Completo

```bash
# 1. Acceder a formulario
GET /empresa/register

# 2. Rellenar y enviar
POST /empresa/register
Content-Type: application/x-www-form-urlencoded

nombre=Tech Company&
correo=info@tech.com&
passwordHash=SecurePass123&
telefono=%2855%29+1234-5678&
localizacion=Mexico%20City&
descripcion=Empresa+de+tecnologia

# 3. Redirección
→ /empresa/login
```

### Ejemplo 2: Login

```bash
# 1. Acceder a login
GET /empresa/login

# 2. Enviar credenciales
POST /empresa/login
Content-Type: application/x-www-form-urlencoded

correo=info@tech.com&password=SecurePass123

# 3. Validación exitosa
→ Crear sesión
→ /empresa/dashboard
```

### Ejemplo 3: Dashboard Protegido

```bash
# 1. Con sesión válida
GET /empresa/dashboard
Cookie: JSESSIONID=abc123...

# 2. Respuesta
→ Vista dashboard.html
→ Modelo con datos de empresa

# 3. Sin sesión
GET /empresa/dashboard

# Respuesta
→ /empresa/login
```

---

## 🧪 Pruebas Recomendadas

### Test 1: Registro
```java
@Test
public void testRegistrationSuccess() {
    // Datos válidos → Success
}

@Test
public void testRegistrationDuplicateEmail() {
    // Email duplicado → Error
}

@Test
public void testRegistrationInvalidEmail() {
    // Email inválido → Error
}
```

### Test 2: Login
```java
@Test
public void testLoginSuccess() {
    // Credenciales válidas → Dashboard
}

@Test
public void testLoginInvalidPassword() {
    // Password incorrecto → Error
}

@Test
public void testLoginNotApproved() {
    // Empresa no aprobada → Error
}
```

### Test 3: Dashboard
```java
@Test
public void testDashboardAuthenticated() {
    // Con sesión → Dashboard
}

@Test
public void testDashboardUnauthenticated() {
    // Sin sesión → Login
}
```

---

## 📚 Documentación Relacionada

- `EmpresaService.java` - Lógica de negocio
- `EmpresaRepositoryJdbc.java` - Acceso a datos
- `Empresa.java` - Modelo de datos
- `registro.html`, `login.html`, `dashboard.html` - Vistas

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.0.0  
**Estado:** ✅ COMPLETADO
