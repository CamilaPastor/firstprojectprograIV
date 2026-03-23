# 🚀 Servicios Spring Boot - Bolsa de Empleo

## Descripción General

Se han creado 7 servicios (interfaces + implementaciones) en los paquetes:
- `cr.una.bolsaempleo.service` - Interfaces
- `cr.una.bolsaempleo.service.impl` - Implementaciones

Todos los servicios utilizan:
- `@Service` para anotación
- `@RequiredArgsConstructor` de Lombok (inyección automática)
- `@Transactional` para manejo de transacciones
- `@Transactional(readOnly = true)` en métodos de lectura

---

## 📋 Servicios Creados

### 1. **EmpresaService**

Gestión de empresas registradas.

```java
public interface EmpresaService {
    Empresa registrar(Empresa empresa);
    Optional<Empresa> loginPorCorreo(String correo, String password);
    List<Empresa> pendientes();
    Empresa aprobar(Integer idEmpresa);
    Optional<Empresa> findById(Integer idEmpresa);
    List<Empresa> obtenerActivas();
    Empresa actualizar(Empresa empresa);
    void desactivar(Integer idEmpresa);
}
```

**Características:**
- ✅ Hashing automático de contraseña con BCrypt
- ✅ Login con validación de credenciales
- ✅ Gestión de aprobación por administrador
- ✅ CRUD completo

**Ejemplo de uso:**
```java
@Autowired
private EmpresaService empresaService;

// Registrar
Empresa empresa = new Empresa();
empresa.setNombre("TechCorp");
empresa.setPasswordSinHashear("mipassword");
empresaService.registrar(empresa);

// Login
Optional<Empresa> logueada = empresaService
    .loginPorCorreo("info@techcorp.cr", "mipassword");

// Obtener pendientes
List<Empresa> pendientes = empresaService.pendientes();

// Aprobar
empresaService.aprobar(idEmpresa);
```

---

### 2. **OferenteService**

Gestión de oferentes (personas buscando empleo).

```java
public interface OferenteService {
    Oferente registrar(Oferente oferente);
    Optional<Oferente> loginPorCorreo(String correo, String password);
    List<Oferente> pendientes();
    Oferente aprobar(Integer idOferente);
    Optional<Oferente> findById(Integer idOferente);
    Object habilidades(Integer idOferente);
    Object agregarHabilidad(Integer idOferente, Integer idCaracteristica, Integer nivel);
    void eliminarHabilidad(Integer idOferente, Integer idCaracteristica);
    Cv subirCv(Integer idOferente, byte[] archivoPdf, String nombreArchivo);
    Optional<Cv> obtenerCv(Integer idOferente);
    List<Oferente> obtenerActivos();
    Oferente actualizar(Oferente oferente);
    void desactivar(Integer idOferente);
}
```

**Características:**
- ✅ Hashing automático de contraseña con BCrypt
- ✅ Gestión de habilidades (características)
- ✅ Subida y gestión de CVs en PDF
- ✅ Validación de duplicados (email, cédula)

**Ejemplo de uso:**
```java
@Autowired
private OferenteService oferenteService;

// Registrar
Oferente oferente = new Oferente();
oferente.setNombre("Juan");
oferente.setPasswordSinHashear("mipassword");
oferenteService.registrar(oferente);

// Agregar habilidad
oferenteService.agregarHabilidad(idOferente, idCaracteristica, 4); // Nivel 4

// Subir CV
byte[] pdfBytes = // obtener bytes del PDF
oferenteService.subirCv(idOferente, pdfBytes, "CV_Juan.pdf");

// Obtener habilidades
List<?> habilidades = oferenteService.habilidades(idOferente);
```

---

### 3. **PuestoService**

Gestión de ofertas de empleo.

```java
public interface PuestoService {
    Puesto publicar(Puesto puesto, List<Integer> idsCaracteristicas, List<Integer> nivelesRequeridos);
    List<Puesto> ultimos5Publicos();
    List<Puesto> puestosDeEmpresa(Integer idEmpresa);
    Optional<Puesto> findById(Integer idPuesto);
    void desactivar(Integer idPuesto);
    List<Puesto> todosPublicosActivos();
    List<Puesto> puestosActivosPorEmpresa(Integer idEmpresa);
    Puesto actualizar(Puesto puesto);
}
```

**Características:**
- ✅ Publicación con características requeridas
- ✅ Gestión automática de PuestoCaracteristica
- ✅ Filtrado por tipo de publicación
- ✅ Desactivación segura

**Ejemplo de uso:**
```java
@Autowired
private PuestoService puestoService;

// Publicar puesto con características
Puesto puesto = new Puesto();
puesto.setDescripcion("Desarrollador Java");
puesto.setEmpresa(empresa);
puesto.setSalario(BigDecimal.valueOf(2500000));

List<Integer> caracteristicas = Arrays.asList(8, 14, 11); // Java, Spring, SQL
List<Integer> niveles = Arrays.asList(4, 4, 3);

puestoService.publicar(puesto, caracteristicas, niveles);

// Obtener últimos 5
List<Puesto> recientes = puestoService.ultimos5Publicos();
```

---

### 4. **CaracteristicaService**

Gestión de características (habilidades) y su jerarquía.

```java
public interface CaracteristicaService {
    List<Caracteristica> raices();
    List<Caracteristica> hijos(Integer idPadre);
    List<Caracteristica> todas();
    Optional<Caracteristica> findById(Integer idCaracteristica);
    Caracteristica crear(String nombre, Integer idPadre);
    Caracteristica crear(String nombre, String descripcion, Integer idPadre);
    List<Caracteristica> obtenerHierarchy();
    List<Caracteristica> hijosActivos(Integer idPadre);
}
```

**Características:**
- ✅ Estructura jerárquica padre-hijo
- ✅ Validación de nombres únicos
- ✅ Obtención de jerarquía completa

**Ejemplo de uso:**
```java
@Autowired
private CaracteristicaService caracteristicaService;

// Obtener categorías raíz
List<Caracteristica> categorias = caracteristicaService.raices();

// Obtener subcategorías
List<Caracteristica> subcat = caracteristicaService.hijos(1); // Hijos de Bases de Datos

// Crear subcategoría
caracteristicaService.crear("Redis", 1); // Bajo Bases de Datos

// Obtener jerarquía completa
List<Caracteristica> arbol = caracteristicaService.obtenerHierarchy();
```

---

### 5. **BusquedaService**

Búsquedas avanzadas y matching de candidatos.

```java
public interface BusquedaService {
    List<ResultadoCandidato> buscarCandidatos(Integer idPuesto);
    List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> idsCaracteristicas);
    List<Puesto> buscarPorRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax);
    List<Puesto> buscarPorKeyword(String keyword);
}
```

**Características:**
- ✅ Matching inteligente: oferentes que cumplen requisitos
- ✅ Cálculo automático de porcentaje de cumplimiento
- ✅ Ordenamiento por porcentaje (mayor a menor)
- ✅ DTO ResultadoCandidato con oferen te + porcentaje

**Ejemplo de uso:**
```java
@Autowired
private BusquedaService busquedaService;

// Buscar candidatos para puesto
List<ResultadoCandidato> candidatos = busquedaService.buscarCandidatos(idPuesto);

for (ResultadoCandidato rc : candidatos) {
    System.out.println(rc.getOferente().getNombre()); // Nombre del oferente
    System.out.println(rc.getCumplidos() + "/" + rc.getTotal()); // Características cumplidas
    System.out.println(rc.getPorcentaje() + "%"); // Porcentaje de match
}

// Buscar puestos por características
List<Puesto> puestos = busquedaService
    .buscarPuestosPorCaracteristicas(Arrays.asList(8, 14)); // Java, Spring Boot

// Buscar por rango salarial
List<Puesto> enRango = busquedaService.buscarPorRangoSalario(
    BigDecimal.valueOf(2000000),
    BigDecimal.valueOf(3000000)
);
```

---

### 6. **AdminService**

Gestión de administradores.

```java
public interface AdminService {
    Optional<Administrador> login(String identificacion, String password);
    Optional<Administrador> findById(Integer idAdmin);
}
```

**Características:**
- ✅ Login con validación BCrypt
- ✅ Simple y eficiente

**Ejemplo de uso:**
```java
@Autowired
private AdminService adminService;

// Login
Optional<Administrador> admin = adminService.login("admin", "admin123");

if (admin.isPresent()) {
    System.out.println("Admin autenticado");
}
```

---

### 7. **ReporteService**

Generación de reportes en PDF con iTextPDF.

```java
public interface ReporteService {
    byte[] generarReporteMensual(Integer anio, Integer mes) throws IOException;
    byte[] generarReporteCandidatos(Integer idPuesto) throws IOException;
    byte[] generarReporteOferentes() throws IOException;
}
```

**Características:**
- ✅ PDF con tablas formateadas
- ✅ Reporte mensual de puestos
- ✅ Reporte de candidatos para puesto
- ✅ Reporte de oferentes aprobados
- ✅ Usa iTextPDF 8.x

**Ejemplo de uso:**
```java
@Autowired
private ReporteService reporteService;

// Generar reporte mensual
byte[] pdfMensual = reporteService.generarReporteMensual(2026, 3);

// Generar reporte de candidatos
byte[] pdfCandidatos = reporteService.generarReporteCandidatos(idPuesto);

// Generar reporte de oferentes
byte[] pdfOferentes = reporteService.generarReporteOferentes();

// Guardar o enviar como descarga
response.setContentType("application/pdf");
response.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
response.getOutputStream().write(pdfMensual);
```

---

## 📊 Clase DTO: ResultadoCandidato

```java
@Data
public class ResultadoCandidato {
    private Oferente oferente;      // Datos del oferente
    private Integer cumplidos;      // Características cumplidas
    private Integer total;          // Total de características requeridas
    private Double porcentaje;      // Porcentaje calculado (cumplidos/total * 100)
}
```

---

## 🔗 Inyección de Dependencias

Todos los servicios se inyectan con `@RequiredArgsConstructor`:

```java
@Service
@RequiredArgsConstructor
public class MiServicio {
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    // Los campos se inicializan automáticamente
}
```

En controladores:

```java
@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {
    private final EmpresaService empresaService;
    
    @PostMapping("/registrar")
    public Empresa registrar(@RequestBody Empresa empresa) {
        return empresaService.registrar(empresa);
    }
}
```

---

## 🔐 Manejo de Contraseñas

**En Empresa y Oferente:**
- Campo `passwordHash`: contraseña hasheada (persistida)
- Campo `@Transient passwordSinHashear`: contraseña sin hashear (entrada)

**En registro:**
```java
empresa.setPasswordSinHashear("mipassword");
empresaService.registrar(empresa); // Se hashea automáticamente
```

**En login:**
```java
Optional<Empresa> logueada = empresaService
    .loginPorCorreo(correo, passwordSinHashear);
// Compara con passwordEncoder.matches()
```

---

## ⚙️ Configuración Requerida

Asegúrate de que en `pom.xml` o `build.gradle` tengas:

```xml
<!-- iTextPDF para reportes -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-core</artifactId>
    <version>8.0.2</version>
</dependency>

<!-- Spring Security para BCrypt -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

---

## 🚀 Patrones Utilizados

### Patrón Service
- Lógica de negocio separada del controlador
- Transacciones manejadas por el servicio
- Excepciones capturadas y relanzadas

### Patrón Repository
- Acceso a datos delegado a repositorio
- Métodos reutilizables
- Lazy loading para relaciones

### Inyección de Dependencias
- Autowiring por constructor
- @RequiredArgsConstructor de Lombok
- Testeable sin Spring

---

## 📝 Transaccionalidad

```java
@Service
@Transactional  // Por defecto: read-write
@RequiredArgsConstructor
public class EmpresaServiceImpl {
    
    @Transactional(readOnly = true)  // Lectura
    public List<Empresa> pendientes() {
        return repo.findByAprobadoFalse();
    }
    
    @Transactional  // Escritura
    public Empresa registrar(Empresa empresa) {
        // Operaciones que requieren transacción
        return repo.save(empresa);
    }
}
```

---

## 🧪 Testing

```java
@SpringBootTest
public class EmpresaServiceTest {
    
    @MockBean
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private EmpresaService empresaService;
    
    @Test
    public void testLogin() {
        // Arrange
        Empresa empresa = new Empresa();
        empresa.setCorreo("test@test.com");
        when(empresaRepository.findByCorreo("test@test.com"))
            .thenReturn(Optional.of(empresa));
        
        // Act
        Optional<Empresa> resultado = empresaService
            .loginPorCorreo("test@test.com", "password");
        
        // Assert
        assertTrue(resultado.isPresent());
    }
}
```

---

**Última actualización**: 19 de marzo de 2026
**Versión**: 1.0 - Completo
**Estado**: ✅ Listo para usar en controladores
