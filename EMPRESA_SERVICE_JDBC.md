# EmpresaServiceJdbc - Documentación Completa

## 📋 Descripción

`EmpresaServiceJdbc` es la capa de servicio (Service Layer) que contiene toda la lógica de negocio para operaciones con empresas. Actúa como intermediaria entre los controladores y el repositorio JDBC.

**Responsabilidades:**
- Validar datos de entrada
- Aplicar reglas de negocio
- Coordinar operaciones de base de datos
- Manejo de excepciones
- Seguridad (contraseñas cifradas)

---

## 🔑 Métodos Principales

### 1. `registerEmpresa(Empresa empresa) → Empresa`

**Lógica de Negocio:**

```
1. Validar datos de la empresa
2. Validar formato de email
3. Verificar que email no exista
4. Validar teléfono (si se proporciona)
5. Cifrar contraseña
6. Establecer aprobado = false
7. Guardar en BD
8. Retornar empresa con ID generado
```

**Validaciones Realizadas:**
- ✅ Nombre: 3-150 caracteres
- ✅ Email: Formato válido + Único en BD
- ✅ Teléfono: Formato válido (opcional)
- ✅ PasswordHash: Mínimo 10 caracteres
- ✅ No campos NULL requeridos

**Ejemplo de Uso:**
```java
EmpresaService service = new EmpresaService(repository);

Empresa empresa = new Empresa();
empresa.setNombre("Tech Solutions");
empresa.setCorreo("info@tech.com");
empresa.setTelefono("(55) 1234-5678");
empresa.setLocalizacion("Ciudad de México");
empresa.setDescripcion("Empresa de tecnología");
empresa.setPasswordHash("miContraseña123");  // Plain password

try {
    Empresa registrada = service.registerEmpresa(empresa);
    System.out.println("Registrada con ID: " + registrada.getIdEmpresa());
    System.out.println("Estado: " + (registrada.getAprobado() ? "Aprobada" : "Pendiente"));
} catch (IllegalArgumentException e) {
    System.out.println("Error de validación: " + e.getMessage());
} catch (SQLException e) {
    System.out.println("Error de BD: " + e.getMessage());
}
```

**Excepciones:**
- `IllegalArgumentException` - Si validación falla
- `SQLException` - Si operación de BD falla

---

### 2. `validateLogin(String correo, String password) → Optional<Empresa>`

**Lógica de Negocio:**

```
1. Validar que correo no esté vacío
2. Validar que password no esté vacío
3. Buscar empresa por correo
4. Si no existe → retornar Optional.empty()
5. Verificar hash de contraseña
6. Si contraseña incorrecta → retornar Optional.empty()
7. Verificar que empresa esté aprobada
8. Si NO aprobada → lanzar IllegalStateException
9. Si todo correcto → retornar Optional<Empresa>
```

**Características de Seguridad:**
- ✅ No revela si email existe o contraseña es incorrecta
- ✅ Verifica aprobación de empresa
- ✅ Usa hash verificado (timing-attack resistant)
- ✅ Valida entrada

**Ejemplo de Uso:**
```java
try {
    Optional<Empresa> result = service.validateLogin(
        "info@tech.com",
        "miContraseña123"
    );
    
    if (result.isPresent()) {
        Empresa empresa = result.get();
        System.out.println("✓ Login exitoso");
        System.out.println("Empresa: " + empresa.getNombre());
        // Crear sesión, redireccionar a dashboard, etc.
    } else {
        System.out.println("✗ Email o contraseña incorrectos");
    }
} catch (IllegalStateException e) {
    // Empresa existe pero no está aprobada
    System.out.println("Tu empresa está pendiente de aprobación");
} catch (SQLException e) {
    System.out.println("Error de BD: " + e.getMessage());
}
```

**Retorna:**
- `Optional.of(empresa)` - Login exitoso
- `Optional.empty()` - Email no existe o contraseña incorrecta
- Lanza `IllegalStateException` - Empresa no aprobada

---

## 📚 Métodos Adicionales

### Búsqueda y Consulta

```java
// Obtener empresa por ID
Optional<Empresa> empresa = service.obtenerEmpresaPorId(1L);

// Obtener empresa por correo
Optional<Empresa> empresa = service.obtenerEmpresaPorCorreo("info@tech.com");

// Listar todas las empresas
List<Empresa> todas = service.listarEmpresas();

// Listar solo aprobadas
List<Empresa> aprobadas = service.listarEmpresasAprobadas();

// Listar solo pendientes
List<Empresa> pendientes = service.listarEmpresasPendientes();

// Buscar por localización
List<Empresa> enMonterrey = service.buscarPorLocalizacion("Monterrey");
```

### Administración

```java
// Actualizar perfil (no cambia email/password)
boolean actualizada = service.actualizarPerfil(empresa);

// Aprobar empresa
Empresa aprobada = service.aprobarEmpresa(empresaId);

// Rechazar empresa
Empresa rechazada = service.rechazarEmpresa(empresaId);

// Eliminar empresa
service.eliminarEmpresa(empresaId);
```

### Utilidades

```java
// Verificar si email existe
boolean existe = service.emailExists("info@tech.com");

// Contar total de empresas
long total = service.contarEmpresas();

// Obtener estadísticas
EmpresaService.CompanyStats stats = service.obtenerEstadisticas();
System.out.println("Total: " + stats.total);
System.out.println("Aprobadas: " + stats.approved);
System.out.println("Pendientes: " + stats.pending);
System.out.println("% Aprobación: " + stats.getApprovalPercentage());
```

---

## 🔄 Flujos de Negocio

### Flujo 1: Registro de Nueva Empresa

```
┌─────────────────────┐
│   Usuario rellena   │
│   formulario        │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────────────────┐
│   registerEmpresa()             │
│   • Validar datos               │
│   • Verificar email único       │
│   • Cifrar contraseña           │
│   • Guardar en BD               │
└──────────┬──────────────────────┘
           │
           ▼
┌─────────────────────────────────┐
│   Empresa creada                │
│   Estado: PENDIENTE             │
│   ID generado                   │
└──────────┬──────────────────────┘
           │
           ▼
┌─────────────────────────────────┐
│   Admin revisa solicitud        │
│   • aprobarEmpresa()            │
│   • rechazarEmpresa()           │
└──────────┬──────────────────────┘
           │
           ▼
┌─────────────────────────────────┐
│   Estado cambia a APROBADA      │
│   Empresa puede publicar empleos│
└─────────────────────────────────┘
```

### Flujo 2: Login de Empresa

```
┌──────────────────────┐
│  Usuario ingresa     │
│  email y contraseña  │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────────────────┐
│   validateLogin()                │
│   • Buscar por email             │
│   • Verificar contraseña         │
│   • Verificar aprobación         │
└──────────┬───────────────────────┘
           │
      ┌────┴────┐
      │          │
      ▼          ▼
   ✓ Válido   ✗ Inválido
      │          │
      │          └──→ "Email o contraseña incorrectos"
      │
      ▼
   ¿Aprobada?
      │
    ┌─┴─┐
    │   │
   Sí   No
    │   │
    │   └──→ "Empresa pendiente de aprobación"
    │
    ▼
 Crear sesión
 Redireccionar a dashboard
```

---

## ⚠️ Excepciones y Manejo de Errores

### Tipos de Excepciones

```java
// Validación fallida
try {
    service.registerEmpresa(invalidEmpresa);
} catch (IllegalArgumentException e) {
    // "El correo ya está registrado"
    // "El nombre debe tener entre 3 y 150 caracteres"
    // "El correo electrónico no es válido"
}

// Base de datos
try {
    service.registerEmpresa(empresa);
} catch (SQLException e) {
    // Error de conexión o consulta SQL
}

// Empresa no aprobada en login
try {
    service.validateLogin(correo, password);
} catch (IllegalStateException e) {
    // "Tu empresa está pendiente de aprobación"
}

// Empresa no encontrada en operaciones
try {
    service.aprobarEmpresa(999L);  // ID inexistente
} catch (IllegalArgumentException e) {
    // "Empresa no encontrada"
}
```

---

## 🏗️ Integración en Controlador

```java
@Controller
@RequestMapping("/empresa")
public class EmpresaController {
    
    private EmpresaService empresaService;
    
    public EmpresaController(EmpresaRepositoryJdbc repository) {
        this.empresaService = new EmpresaService(repository);
    }
    
    // Registro
    @PostMapping("/registro")
    public String registrar(@ModelAttribute Empresa empresa, 
                           Model model) {
        try {
            Empresa registrada = empresaService.registerEmpresa(empresa);
            model.addAttribute("mensaje", 
                "Registro exitoso. Espera aprobación.");
            return "redirect:/empresa/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "empresa/registro";
        } catch (SQLException e) {
            model.addAttribute("error", "Error de servidor");
            return "error";
        }
    }
    
    // Login
    @PostMapping("/login")
    public String login(@RequestParam String correo,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            Optional<Empresa> empresa = empresaService.validateLogin(correo, password);
            
            if (empresa.isPresent()) {
                session.setAttribute("empresaId", empresa.get().getIdEmpresa());
                session.setAttribute("empresaNombre", empresa.get().getNombre());
                return "redirect:/empresa/dashboard";
            } else {
                model.addAttribute("error", "Email o contraseña incorrectos");
                return "empresa/login";
            }
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "empresa/login";
        } catch (SQLException e) {
            model.addAttribute("error", "Error de servidor");
            return "error";
        }
    }
}
```

---

## 🔐 Seguridad Implementada

### Contraseñas
- ✅ Se cifran con SHA-256 + Salt
- ✅ Nunca se devuelven en texto plano
- ✅ Verificación timing-attack resistant

### Datos Sensibles
- ✅ Email único (constraint en BD)
- ✅ No se revela si usuario existe
- ✅ No se revela si contraseña es incorrecta
- ✅ Validación en servidor

### Aprobación
- ✅ Empresas nuevas no aprobadas por defecto
- ✅ Login rechazado si no aprobada
- ✅ Solo admin puede aprobar

---

## 📊 Estadísticas - Clase CompanyStats

```java
public static class CompanyStats {
    public long total;           // Total de empresas
    public int approved;         // Aprobadas
    public int pending;          // Pendientes
    
    // Calcular porcentaje de aprobación
    public double getApprovalPercentage()
    
    // Representación en string
    public String toString()
}

// Uso
EmpresaService.CompanyStats stats = service.obtenerEstadisticas();
System.out.println(stats.total);           // 42
System.out.println(stats.approved);        // 35
System.out.println(stats.pending);         // 7
System.out.println(stats.getApprovalPercentage());  // 83.33
```

---

## 🧪 Casos de Prueba

### Caso 1: Registro Válido
```java
Empresa empresa = crearEmpresa(
    "Tech", "tech@example.com", "Pass123"
);
Empresa resultado = service.registerEmpresa(empresa);
assert resultado.getId() > 0;
assert !resultado.getAprobado();
```

### Caso 2: Email Duplicado
```java
// Primera empresa
service.registerEmpresa(empresa1);

// Segunda con mismo email
assertThrows(IllegalArgumentException.class, () -> 
    service.registerEmpresa(empresa2_sameEmail)
);
```

### Caso 3: Login Correcto
```java
service.registerEmpresa(empresa);  // Registrar
// Aprobar manualmente
Optional<Empresa> result = 
    service.validateLogin("email", "password");
assert result.isPresent();
```

### Caso 4: Login Fallido
```java
Optional<Empresa> result = 
    service.validateLogin("nope@example.com", "wrong");
assert result.isEmpty();
```

---

## 📚 Documentación Relacionada

- `Empresa.java` - Modelo de datos
- `EmpresaRepositoryJdbc.java` - Capa de acceso a datos
- `EmpresaValidator.java` - Validaciones
- `PasswordHashUtil.java` - Cifrado de contraseñas
- `EmpresaServiceExample.java` - Ejemplos prácticos

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.0.0  
**Estado:** ✅ COMPLETADO
