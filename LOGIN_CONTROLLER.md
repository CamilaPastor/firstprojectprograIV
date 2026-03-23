# 🔐 LOGIN CONTROLLER - Documentación Completa

## 📋 Descripción General

**LoginController** es un controlador Spring MVC unificado que maneja la autenticación para ambos tipos de usuarios:
- **Empresa** - Reclutadores/Administradores
- **Oferente** - Candidatos/Buscadores de empleo

El controlador valida credenciales usando la capa de servicio y gestiona sesiones para ambos tipos de usuarios.

---

## 🔑 ENDPOINTS PRINCIPALES

### 1. GET /login
```
Propósito: Mostrar página de selección de tipo de usuario
Autenticación: NO requerida
Retorna: Vista login/index.html
Descripción: Permite al usuario elegir entre login de empresa o candidato
```

### 2. GET /login/empresa
```
Propósito: Mostrar formulario de login para empresa
Autenticación: NO requerida
Retorna: Vista login/empresa.html
Campos: email, password
```

### 3. POST /login/empresa
```
Propósito: Procesar login de empresa
Validaciones:
  ✓ Email y password no vacíos
  ✓ Credenciales válidas (via EmpresaService)
  ✓ Empresa aprobada

Respuesta Exitosa:
  • Crear sesión con:
    - userType = "empresa"
    - empresaId
    - empresaNombre
    - empresaCorreo
  • Redirect: /empresa/dashboard

Respuesta Fallida:
  • Vista: login/empresa.html
  • Error: "Correo o contraseña incorrectos"
```

### 4. GET /login/oferente
```
Propósito: Mostrar formulario de login para candidato
Autenticación: NO requerida
Retorna: Vista login/oferente.html
Campos: email, password
```

### 5. POST /login/oferente
```
Propósito: Procesar login de candidato (oferente)
Validaciones:
  ✓ Email y password no vacíos
  ✓ Credenciales válidas (via OferenteService)

Respuesta Exitosa:
  • Crear sesión con:
    - userType = "oferente"
    - oferenteId
    - oferenteNombre
    - oferenteCorreo
  • Redirect: /oferente/dashboard

Respuesta Fallida:
  • Vista: login/oferente.html
  • Error: "Correo o contraseña incorrectos"
```

### 6. GET /logout
```
Propósito: Cerrar sesión del usuario
Autenticación: Requerida (detecta automáticamente)
Funciona para: Ambos tipos de usuario (empresa y oferente)
Acciones:
  • Invalida la sesión
  • Log del logout
  • Redirect: /
```

---

## 📊 SESIÓN - Estructura

### Para Empresa (Después de Login)
```java
session.setAttribute("userType", "empresa");
session.setAttribute("empresaId", 123L);
session.setAttribute("empresaNombre", "Tech Corp");
session.setAttribute("empresaCorreo", "info@tech.com");
```

### Para Oferente (Después de Login)
```java
session.setAttribute("userType", "oferente");
session.setAttribute("oferenteId", 456L);
session.setAttribute("oferenteNombre", "Juan");
session.setAttribute("oferenteCorreo", "juan@email.com");
```

### Verificar Sesión en Controladores
```java
// Detectar tipo de usuario
Object userType = session.getAttribute("userType");

if ("empresa".equals(userType)) {
    Long empresaId = (Long) session.getAttribute("empresaId");
}

if ("oferente".equals(userType)) {
    Long oferenteId = (Long) session.getAttribute("oferenteId");
}

// Invalidar sesión
session.invalidate();
```

---

## 🔄 FLUJOS DE AUTENTICACIÓN

### Flujo 1: Login de Empresa

```
1. GET /login → Mostrar opciones
   ↓
2. Click "Empresa" → GET /login/empresa → Mostrar formulario
   ↓
3. Usuario ingresa email y password
   ↓
4. POST /login/empresa con credenciales
   ↓
5. LoginController valida:
   • Email y password no vacíos
   • Llama a empresaService.validateLogin()
   ↓
6. EmpresaService:
   • Busca empresa por email
   • Verifica password (SHA-256)
   • Verifica aprobación
   ↓
7. Resultado:
   ✓ OK → Crear sesión + Redirect /empresa/dashboard
   ✗ FAIL → Mostrar error + Vista login/empresa
```

### Flujo 2: Login de Oferente

```
1. GET /login → Mostrar opciones
   ↓
2. Click "Candidato" → GET /login/oferente → Mostrar formulario
   ↓
3. Usuario ingresa email y password
   ↓
4. POST /login/oferente con credenciales
   ↓
5. LoginController valida:
   • Email y password no vacíos
   • Llama a oferenteService.validateLogin()
   ↓
6. OferenteService:
   • Busca oferente por email
   • Verifica password (SHA-256)
   ↓
7. Resultado:
   ✓ OK → Crear sesión + Redirect /oferente/dashboard
   ✗ FAIL → Mostrar error + Vista login/oferente
```

---

## 🛠️ INTEGRACIÓN CON SERVICIOS

### EmpresaService.validateLogin()
```java
// Llama a:
empresaService.validateLogin(correo, password)

// Retorna:
Optional<Empresa>
  ├─ Si éxito: Optional.of(empresa)
  ├─ Si fallido: Optional.empty()
  └─ Si no aprobada: Lanza IllegalStateException
```

### OferenteService.validateLogin()
```java
// Llama a:
oferenteService.validateLogin(correo, password)

// Retorna:
Optional<Oferente>
  ├─ Si éxito: Optional.of(oferente)
  └─ Si fallido: Optional.empty()
```

---

## 📱 VISTAS HTML CREADAS

### 1. **login/index.html** - Página Principal de Login
```
Muestra dos opciones:
├─ Soy Empresa
│  ├─ Descripción
│  ├─ Botón "Iniciar Sesión"
│  └─ Botón "Registrarse"
│
└─ Soy Candidato
   ├─ Descripción
   ├─ Botón "Iniciar Sesión"
   └─ Botón "Registrarse"
```

### 2. **login/empresa.html** - Login Empresa
```
Campos:
├─ Email (requerido)
└─ Password (requerido)

Características:
├─ Mensajes de error
├─ Link a registro
└─ Info sobre aprobación
```

### 3. **login/oferente.html** - Login Oferente
```
Campos:
├─ Email (requerido)
└─ Password (requerido)

Características:
├─ Mensajes de error
├─ Link a registro
└─ Beneficios de registrarse
```

---

## ⚠️ MANEJO DE ERRORES

### Errores Capturados

```
SQLException
├─ Error de conexión BD
├─ Error en query
└─ Error al buscar en BD

IllegalArgumentException
└─ Validación en servicio

IllegalStateException
└─ Empresa no aprobada

Exception
└─ Errores inesperados
```

### Mensajes Mostrados al Usuario

```
✗ Email o contraseña incorrectos
  → Ambos casos (email no existe, password incorrecto)

✓ Tu empresa está pendiente de aprobación
  → Empresa existe pero no aprobada

⚠️ Error al procesar el login. Intenta más tarde.
  → Error de BD o inesperado
```

---

## 🔐 CARACTERÍSTICAS DE SEGURIDAD

### ✅ Validación
```
• Campos no vacíos
• Email y password validados
• Credenciales verificadas con hash
```

### ✅ Sesión
```
• Sesión segura (JSESSIONID)
• Atributos específicos para cada tipo
• Invalidación en logout
```

### ✅ Contraseña
```
• SHA-256 con salt aleatorio
• Nunca se devuelve en texto plano
• Verificación timing-attack resistant
```

### ✅ Información
```
• No revela si usuario existe
• No revela si password es incorrecto
• Mensajes genéricos de error
```

---

## 📊 ENDPOINTS RESUMEN

```
GET  /login                  → Seleccionar tipo usuario
GET  /login/empresa          → Formulario login empresa
POST /login/empresa          → Procesar login empresa
GET  /login/oferente         → Formulario login oferente
POST /login/oferente         → Procesar login oferente
GET  /logout                 → Cerrar sesión
```

---

## 🧪 EJEMPLOS DE PRUEBA

### Test 1: Login Empresa Exitoso
```bash
POST /login/empresa
correo=info@tech.com
password=SecurePass123

Expected:
→ Status 302 (Redirect)
→ Location: /empresa/dashboard
→ Session: userType=empresa, empresaId=123
```

### Test 2: Login Oferente Exitoso
```bash
POST /login/oferente
correo=juan@email.com
password=MyPassword456

Expected:
→ Status 302 (Redirect)
→ Location: /oferente/dashboard
→ Session: userType=oferente, oferenteId=456
```

### Test 3: Credenciales Inválidas
```bash
POST /login/empresa
correo=wrong@email.com
password=wrongpass

Expected:
→ Status 200 (Formulario)
→ Mensaje error: "Correo o contraseña incorrectos"
→ Sin sesión creada
```

### Test 4: Logout
```bash
GET /logout
(con sesión activa)

Expected:
→ Status 302 (Redirect)
→ Location: /
→ Sesión invalidada
```

---

## 💡 MEJORES PRÁCTICAS IMPLEMENTADAS

✅ **Separación de Responsabilidades**
- LoginController maneja HTTP
- Servicios manejan lógica
- Repositorio maneja BD

✅ **Logging**
- Log de todos los accesos
- Log de errores
- Log de operaciones exitosas

✅ **Validación**
- Validación en controlador
- Validación en servicio
- Validación en repositorio

✅ **Seguridad**
- Contraseñas cifradas
- Sesiones seguras
- Mensajes genéricos

✅ **Experiencia de Usuario**
- Mensajes claros
- Redirects automáticos
- Links útiles

---

## 📚 DOCUMENTACIÓN RELACIONADA

- `EmpresaController.java` - Controlador de empresa
- `OferenteController.java` - Controlador de oferente
- `EmpresaService.java` - Servicio de empresa
- `OferenteService.java` - Servicio de oferente

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.0.0  
**Estado:** ✅ COMPLETADO
