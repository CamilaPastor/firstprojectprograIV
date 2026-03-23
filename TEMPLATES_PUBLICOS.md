# 📄 Vistas Thymeleaf Públicas - Bolsa de Empleo

## Descripción General

Se han creado 5 vistas Thymeleaf en `templates/public/` que extienden `layout.html`:

1. **inicio.html** - Página principal con hero, estadísticas y últimos puestos
2. **login.html** - Formulario de login con selector de tipo
3. **registro-empresa.html** - Formulario de registro para empresas
4. **registro-oferente.html** - Formulario de registro para oferentes
5. **buscar-puestos.html** - Búsqueda de puestos por características

Todas las vistas usan `layout:decorate="~{layout}"` y `layout:fragment="content"`.

---

## 📋 Vistas Creadas

### 1. **inicio.html** - Página Principal

**Ruta**: `GET /`

**Componentes:**
- **Hero Section**: Título "Encuentra tu Empleo Ideal Hoy Mismo" con gradiente
- **Estadísticas**: Cards con 10,000+ puestos, 5,000+ empresas, 50,000+ oferentes
- **Últimos Puestos**: Grid de cards mostrando descripción, empresa, salario, tipo
- **CTA Banner**: Banner naranja con botones para registro

**Thymeleaf Features:**
- `th:each` para iterar puestos
- `th:if` para condicionales
- Formateo de números con `#numbers.formatDecimal`
- Links dinámicos con `@{}`

---

### 2. **login.html** - Formulario de Login

**Ruta**: `GET /login`, `POST /login`

**Componentes:**
- **Card centrada**: Formulario en card blanco
- **Selector de tipo**: Dropdown con empresa/oferente/admin
- **Campos**: usuario (correo/ID) y password
- **Mensajes**: `th:if` para error y success
- **Links**: Registro para oferente y empresa

**Thymeleaf Features:**
- `th:if` para mostrar mensajes flash
- Formulario POST a `/login`
- Placeholder explicativo para campo usuario

---

### 3. **registro-empresa.html** - Registro Empresa

**Ruta**: `GET /registro/empresa`, `POST /registro/empresa`

**Campos del formulario:**
- nombre (required)
- localizacion
- correo (email, required)
- telefono
- descripcion (textarea)
- password (required)

**Características:**
- Validación HTML5
- Mensajes de error
- Link a login
- Botón submit full-width

---

### 4. **registro-oferente.html** - Registro Oferente

**Ruta**: `GET /registro/oferente`, `POST /registro/oferente`

**Campos del formulario:**
- identificacion (pattern números 8-20, required)
- nombre (required)
- apellido (required)
- nacionalidad
- telefono
- correo (email, required)
- residencia
- password (required)

**Características:**
- Validación con regex para cédula
- Pattern validation
- Mensajes de error
- Link a login

---

### 5. **buscar-puestos.html** - Búsqueda de Puestos

**Ruta**: `GET /puestos/buscar-por-caracteristicas`

**Componentes:**
- **Formulario**: Checkboxes organizados por categorías
- **Grid responsive**: Características en grid
- **Resultados**: Cards de puestos encontrados
- **Filtros**: Indicador de filtros aplicados

**Thymeleaf Features:**
- `th:each` anidado para categorías y subcategorías
- Checkboxes con `name="ids"` para múltiples selecciones
- Condicionales para mostrar resultados
- Formateo de números y badges para requisitos

---

## 🎨 Diseño y Estilos

### Layout Heredado
Todas las vistas heredan del `layout.html`:
- Navbar sticky con navegación dinámica
- Footer con grid de 4 columnas
- Variables CSS (--orange, --dark, etc.)
- Sistema de botones y cards

### Componentes Utilizados
- `.hero` - Sección hero con gradiente
- `.dash-grid` - Grid de cards para estadísticas
- `.card .job-card` - Cards con hover
- `.form-group .form-control` - Formularios con focus naranja
- `.btn-primary .btn-outline` - Botones del sistema
- `.badge-orange` - Badges para requisitos
- `.alert-danger .alert-success` - Mensajes flash

### Responsive Design
- Container max-width 1200px
- Grid responsive con auto-fit
- Media queries para móvil
- Flexbox para layouts

---

## 🔗 Integración con Controllers

### PublicController
```java
@GetMapping("/")
public String index(Model model) {
    List<Puesto> puestos = puestoService.ultimos5Publicos();
    model.addAttribute("puestos", puestos);
    return "public/inicio";
}
```

### LoginController
```java
@PostMapping("/login")
public String login(@RequestParam String tipo, 
                   @RequestParam String usuario,
                   @RequestParam String password,
                   HttpSession session, Model model) {
    // Lógica de autenticación
}
```

### Formularios de Registro
```java
@PostMapping("/registro/empresa")
public String registroEmpresa(@ModelAttribute Empresa empresa, Model model) {
    try {
        empresaService.registrar(empresa);
        return "redirect:/login";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "public/registro-empresa";
    }
}
```

---

## 📊 Funcionalidades Implementadas

### ✅ Página de Inicio
- Hero atractivo con CTA
- Estadísticas dinámicas
- Últimos puestos con links
- Banner de registro

### ✅ Sistema de Login
- Selector de tipo de usuario
- Validación de credenciales
- Mensajes de error/success
- Redirección automática

### ✅ Registro de Usuarios
- Formularios validados
- Campos requeridos marcados
- Mensajes de error
- Links de navegación

### ✅ Búsqueda de Puestos
- Checkboxes jerárquicos
- Filtros múltiples
- Resultados en cards
- Indicador de filtros aplicados

---

## 🚀 Próximos Pasos

1. Crear vistas para dashboards (empresa, oferente, admin)
2. Implementar formularios de gestión (puestos, habilidades, CV)
3. Agregar validaciones JavaScript
4. Implementar AJAX para búsquedas dinámicas
5. Crear vistas de detalle y edición

---

**Última actualización**: 19 de marzo de 2026
**Versión**: 1.0 - Completo
**Estado**: ✅ Listo para usar con controllers
