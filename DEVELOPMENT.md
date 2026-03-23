# Guía de Desarrollo - Bolsa de Empleo

## 🎯 Estructura del Proyecto

### Patrón MVC (Model-View-Controller)

#### Models (Entidades)
Las entidades se encuentran en `com.bolsaempleo.model`:
- **Empresa.java** - Representa una empresa registrada
- **Oferente.java** - Representa un candidato/solicitante de empleo
- **Administrador.java** - Usuario administrador del sistema
- **Puesto.java** - Oferta de empleo publicada
- **Caracteristica.java** - Habilidad requerida o poseída
- **CV.java** - Curriculum Vitae del candidato

**Relaciones principales:**
```
Empresa (1) ──────────────(N) Puesto
Puesto  (1) ──────────────(N) Caracteristica
Oferente (1) ──────────────(N) Caracteristica
Oferente (1) ──────────────(N) CV
```

#### Controllers (Controladores)
Los controladores manejan las solicitudes HTTP en `com.bolsaempleo.controller`:
- **HomeController** - Página de inicio y páginas generales
- **EmpresaController** - Gestión de empresas (registro, login, perfil)
- **OferenteController** - Gestión de candidatos
- **PuestoController** - Gestión de ofertas de empleo
- **CaracteristicaController** - Gestión de habilidades
- **CVController** - Gestión de currículums

**Convención de Rutas:**
```
GET  /empresa/registro      → Mostrar formulario de registro
POST /empresa/registro      → Procesar registro
GET  /empresa/login         → Mostrar formulario de login
POST /empresa/login         → Procesar login
GET  /empresa/dashboard     → Dashboard de empresa
```

#### Services (Servicios)
La lógica de negocio se encuentra en `com.bolsaempleo.service`:
- **EmpresaService** - Lógica de empresas (validación, búsqueda, etc.)
- **OferenteService** - Lógica de candidatos
- **PuestoService** - Lógica de empleos
- **CaracteristicaService** - Lógica de habilidades
- **CVService** - Lógica de carga de archivos y CV

Los servicios:
- Encapsulan la lógica de negocio
- Interactúan con los repositorios
- Implementan validaciones
- Manejan transacciones

#### Repositories (Repositorios)
Los repositorios manejan el acceso a datos en `com.bolsaempleo.repository`:
- Extienden `JpaRepository<Entity, ID>`
- Proporcionan métodos CRUD automáticos
- Permiten consultas personalizadas

Ejemplo:
```java
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByEmail(String email);
    Optional<Empresa> findByRfc(String rfc);
}
```

#### Configuration (Configuración)
- **SecurityConfig.java** - Configuración de seguridad (BCrypt para contraseñas)
- **WebConfig.java** - Configuración web (recursos estáticos)

#### Views (Vistas)
Las vistas Thymeleaf están en `src/main/resources/templates`:
- Páginas HTML con variables dinámicas
- Formularios para entrada de datos
- Listados y detalles
- Control de errores y mensajes

## 🔄 Flujo de una Solicitud HTTP

### Ejemplo: Registro de Empresa

1. **Usuario accede a `/empresa/registro`**
   ```
   GET /empresa/registro → EmpresaController.mostrarFormularioRegistro()
   ↓
   Retorna: empresa/registro.html con formulario vacío
   ```

2. **Usuario llena el formulario y lo envía**
   ```
   POST /empresa/registro → EmpresaController.registrarEmpresa(@ModelAttribute Empresa)
   ↓
   EmpresaService.registrarEmpresa(empresa)
   ↓
   Validaciones, encriptar contraseña con BCrypt
   ↓
   EmpresaRepository.save(empresa)
   ↓
   Guardar en base de datos
   ↓
   Redirect: /empresa/login?success
   ```

3. **Página de éxito**
   ```
   Mostrar mensaje de registro exitoso
   Usuario puede proceder a login
   ```

## 🔐 Seguridad

### Encriptación de Contraseñas
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// Uso en servicio
empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));
```

### Validación de Login
```java
public boolean validarCredenciales(String email, String password) {
    Optional<Empresa> empresa = empresaRepository.findByEmail(email);
    if (empresa.isPresent()) {
        return passwordEncoder.matches(password, empresa.get().getPassword());
    }
    return false;
}
```

### Control de Acceso (Actual)
- Implementado mediante parámetros URL (`?id=123`)
- Verificar que el usuario sea el propietario del recurso
- **Mejora futura:** Implementar Spring Security session/token

## 📝 Agregando Nuevas Características

### Paso 1: Crear la Entidad
```java
@Entity
@Table(name = "MI_TABLA")
@Data
public class MiEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
}
```

### Paso 2: Crear el Repositorio
```java
@Repository
public interface MiRepositorio extends JpaRepository<MiEntidad, Long> {
    List<MiEntidad> findByNombre(String nombre);
}
```

### Paso 3: Crear el Servicio
```java
@Service
@RequiredArgsConstructor
@Transactional
public class MiServicio {
    private final MiRepositorio miRepositorio;
    
    public MiEntidad crear(MiEntidad entidad) {
        return miRepositorio.save(entidad);
    }
}
```

### Paso 4: Crear el Controlador
```java
@Controller
@RequestMapping("/mi-ruta")
@RequiredArgsConstructor
public class MiControlador {
    private final MiServicio miServicio;
    
    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("entidad", new MiEntidad());
        return "mi-vista";
    }
    
    @PostMapping("/crear")
    public String crear(@ModelAttribute MiEntidad entidad) {
        miServicio.crear(entidad);
        return "redirect:/mi-ruta/lista";
    }
}
```

### Paso 5: Crear la Vista
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><title>Crear</title></head>
<body>
    <form th:action="@{/mi-ruta/crear}" th:object="${entidad}" method="post">
        <div class="form-group">
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" th:field="*{nombre}" required>
        </div>
        <button type="submit">Guardar</button>
    </form>
</body>
</html>
```

## 🧪 Testing

### Estructura de Tests
```
src/test/java/com/bolsaempleo/
├── controller/
│   └── EmpresaControllerTest.java
├── service/
│   └── EmpresaServiceTest.java
└── repository/
    └── EmpresaRepositoryTest.java
```

### Ejemplo de Test
```java
@SpringBootTest
public class EmpresaServiceTest {
    @MockBean
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private EmpresaService empresaService;
    
    @Test
    public void testRegistro() {
        Empresa empresa = new Empresa();
        empresa.setEmail("test@example.com");
        
        when(empresaRepository.save(any())).thenReturn(empresa);
        
        Empresa resultado = empresaService.registrarEmpresa(empresa);
        
        assertNotNull(resultado);
        assertEquals("test@example.com", resultado.getEmail());
    }
}
```

## 📊 Validación de Datos

### Anotaciones en Entidades
```java
@Entity
public class Empresa {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    
    @Email(message = "Email inválido")
    private String email;
    
    @Size(min = 3, max = 100)
    private String razonSocial;
    
    @Pattern(regexp = "^[A-Z]{3}[0-9]{9}[A-Z]{3}$")
    private String rfc;
}
```

### Validación en Controlador
```java
@PostMapping("/crear")
public String crear(@Valid @ModelAttribute Empresa empresa,
                   BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "empresa/crear";
    }
    // Procesar...
}
```

## 🗺️ Navegación y URLs

### URLs Principales
```
/                           - Página de inicio
/acerca                     - Información del sitio
/contacto                   - Formulario de contacto

/empresa/registro           - Registro de empresa
/empresa/login              - Login de empresa
/empresa/dashboard?id=1     - Dashboard de empresa
/empresa/perfil?id=1        - Perfil de empresa
/empresa/actualizar         - Actualizar perfil

/oferente/registro          - Registro de candidato
/oferente/login             - Login de candidato
/oferente/dashboard?id=1    - Dashboard de candidato
/oferente/perfil?id=1       - Perfil de candidato

/puesto/lista               - Lista de empleos
/puesto/detalle/1           - Detalle de empleo
/puesto/crear?empresaId=1   - Crear nuevo empleo
/puesto/editar/1            - Editar empleo
/puesto/buscar?ciudad=...   - Buscar por ciudad

/caracteristica/agregar-puesto/1    - Agregar habilidad a puesto
/caracteristica/agregar-oferente/1  - Agregar habilidad a candidato

/cv/subir/1                 - Subir CV
/cv/listar/1                - Listar CVs del candidato
/cv/establecer-principal/1/1 - Marcar CV como principal
/cv/eliminar/1/1            - Eliminar CV
```

## 🐛 Debugging

### Logs Útiles
```properties
logging.level.com.bolsaempleo=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Inspeccionar SQL
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 🚀 Deployment

### Build para Producción
```bash
./gradlew clean build -Pprod
```

### Configurar para Producción
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.thymeleaf.cache=true
logging.level.root=WARN
```

## 📚 Recursos y Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Hibernate ORM](https://hibernate.org/)
- [Spring Security](https://spring.io/projects/spring-security)

---
Última actualización: 18 de Marzo de 2026
