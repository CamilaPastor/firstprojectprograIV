# Guía de Desarrollo - Bolsa de Empleo

## Estructura de Paquetes

```
cr.una.bolsaempleo/
├── config/           # Configuraciones globales
├── controller/       # Controladores MVC
├── model/            # Entidades JPA
├── repository/       # Repositorios de datos
├── service/          # Lógica de negocio
├── util/             # Utilidades y helpers
└── exception/        # Excepciones personalizadas
```

## Convenciones de Código

### Naming

- **Paquetes:** Todo minúsculas
- **Clases:** PascalCase (UsuarioService, UsuarioRepository)
- **Métodos:** camelCase (obtenerPorId, guardarUsuario)
- **Constantes:** UPPER_SNAKE_CASE
- **Variables:** camelCase

### Entidades

```java
@Entity
@Table(name = "tabla_nombre")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NombreEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String campo;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
}
```

### Repositorios

```java
@Repository
public interface NombreRepository extends JpaRepository<Nombre, Long> {
    Optional<Nombre> findByEmail(String email);
    List<Nombre> findByNombreContaining(String nombre);
    @Query("SELECT n FROM Nombre n WHERE n.activo = true")
    List<Nombre> findActivos();
}
```

### Servicios

```java
@Service
@Transactional
@Slf4j
public class NombreService {
    
    @Autowired
    private NombreRepository nombreRepository;
    
    @Transactional(readOnly = true)
    public Nombre obtenerPorId(Long id) {
        return nombreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No encontrado"));
    }
    
    public Nombre guardar(Nombre nombre) {
        log.info("Guardando entidad: {}", nombre);
        return nombreRepository.save(nombre);
    }
    
    public void eliminar(Long id) {
        nombreRepository.deleteById(id);
    }
}
```

### Controladores

```java
@Controller
@RequestMapping("/ruta")
@Slf4j
public class NombreController {
    
    @Autowired
    private NombreService nombreService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("items", nombreService.obtenerTodos());
        return "nombre/lista";
    }
    
    @GetMapping("/{id}")
    public String obtener(@PathVariable Long id, Model model) {
        model.addAttribute("item", nombreService.obtenerPorId(id));
        return "nombre/detalle";
    }
    
    @PostMapping
    public String guardar(@Valid @ModelAttribute Nombre nombre, 
                         BindingResult result) {
        if (result.hasErrors()) {
            return "nombre/formulario";
        }
        nombreService.guardar(nombre);
        return "redirect:/nombre";
    }
}
```

## DTOs (Data Transfer Objects)

Crear DTOs para transferencia de datos:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    
    @Email(message = "Email debe ser válido")
    private String email;
}
```

## Validación

Usar anotaciones de validación:

```java
@Data
public class Usuario {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
    
    @Email(message = "Email debe ser válido")
    private String email;
    
    @Pattern(regexp = "^[0-9]{8}$", message = "Cédula debe tener 8 dígitos")
    private String cedula;
    
    @Min(value = 18, message = "Debe ser mayor de 18 años")
    private Integer edad;
}
```

## Manejo de Excepciones

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e, Model model) {
        log.error("Entidad no encontrada", e);
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        log.error("Error inesperado", e);
        model.addAttribute("error", "Error interno del servidor");
        return "error/500";
    }
}
```

## Transacciones

```java
@Service
public class TransaccionService {
    
    @Transactional
    public void operacionCompleja() {
        // Se hace rollback si hay excepción
        operacion1();
        operacion2();
    }
    
    @Transactional(readOnly = true)
    public List<Item> obtenerItems() {
        // Optimizado para lectura
        return itemRepository.findAll();
    }
}
```

## Templates Thymeleaf

### Estructura básica

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title th:text="${titulo}">Título</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <h1 th:text="${titulo}"></h1>
    
    <!-- Variables -->
    <p th:text="${variable}"></p>
    
    <!-- Condicionales -->
    <div th:if="${condicion}">Si es verdadero</div>
    <div th:unless="${condicion}">Si es falso</div>
    
    <!-- Iteraciones -->
    <ul>
        <li th:each="item : ${items}" th:text="${item.nombre}"></li>
    </ul>
    
    <!-- Formularios -->
    <form th:action="@{/guardar}" th:object="${objeto}" method="post">
        <input type="text" th:field="*{nombre}" required>
        <span th:if="${#fields.hasErrors('nombre')}" th:errors="*{nombre}"></span>
        <button type="submit">Guardar</button>
    </form>
    
    <!-- Enlaces -->
    <a th:href="@{/ruta/123}">Link</a>
</body>
</html>
```

## Debugging

### Logging

```java
@Slf4j
public class MiClase {
    public void metodo() {
        log.debug("Mensaje de debug");
        log.info("Mensaje informativo");
        log.warn("Advertencia");
        log.error("Error", exception);
    }
}
```

### Punto de quiebre

- Click en el número de línea para crear/eliminar breakpoint
- F6 para Step Over
- F5 para Step Into
- F8 para Resume

## Testing

```java
@SpringBootTest
@AutoConfigureMockMvc
class MiServicioTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private MiRepository miRepository;
    
    @Test
    void testObtener() throws Exception {
        when(miRepository.findById(1L))
            .thenReturn(Optional.of(new Entidad()));
        
        mockMvc.perform(get("/entidad/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("detalle"));
    }
}
```

## Deployment

### Generar JAR

```bash
mvn clean package
```

El JAR se encuentra en `target/bolsa-empleo-1.0.0.jar`

### Ejecutar JAR

```bash
java -jar target/bolsa-empleo-1.0.0.jar
```

### Variables de entorno

```bash
java -jar target/bolsa-empleo-1.0.0.jar \
  --server.port=8080 \
  --spring.datasource.url=jdbc:mysql://localhost:3306/bolsa_empleo \
  --spring.datasource.username=root \
  --spring.datasource.password=password
```

## Checklist de Desarrollo

- [ ] Crear entidad con anotaciones JPA
- [ ] Crear repositorio extendiendo JpaRepository
- [ ] Crear servicio con lógica de negocio
- [ ] Crear controlador con endpoints
- [ ] Crear templates Thymeleaf
- [ ] Agregar validación de datos
- [ ] Escribir tests unitarios
- [ ] Documentar código con comentarios
- [ ] Revisar seguridad (SQL Injection, XSS, CSRF)
- [ ] Probar funcionalidad completa
