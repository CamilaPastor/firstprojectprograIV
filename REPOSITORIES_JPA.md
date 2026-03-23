# 📚 Spring Data JPA Repositories - Bolsa de Empleo

## Visión General

Se han creado 8 interfaces JPA Repository que extienden `JpaRepository` con métodos personalizados para consultas específicas de cada entidad. Todos los repositorios están en el paquete `cr.una.bolsaempleo.repository`.

---

## 📋 Repositorios Creados

### 1. **AdministradorRepository**

Métodos para gestionar administradores del sistema.

```java
Optional<Administrador> findByIdentificacion(String identificacion)
boolean existsByIdentificacion(String identificacion)
long countByActivoTrue()
```

**Ejemplos de uso:**
```java
// Login
Optional<Administrador> admin = administradorRepository
    .findByIdentificacion("admin");

// Verificar existencia
if (administradorRepository.existsByIdentificacion("admin2")) {
    // Admin existe
}

// Contar activos
long activos = administradorRepository.countByActivoTrue();
```

---

### 2. **EmpresaRepository**

Métodos para gestionar empresas registradas.

```java
Optional<Empresa> findByCorreo(String correo)
List<Empresa> findByAprobadoFalse()
List<Empresa> findByAprobadoTrue()
List<Empresa> findByAprobadoFalseAndActivoTrue()
boolean existsByCorreo(String correo)
long countByAprobadoTrue()
long countByAprobadoFalse()
List<Empresa> findByNombreContainingIgnoreCase(String nombre)
List<Empresa> findAllActivas()  // @Query custom
```

**Ejemplos de uso:**
```java
// Login de empresa
Optional<Empresa> empresa = empresaRepository
    .findByCorreo("info@techcorp.cr");

// Obtener empresas pendientes
List<Empresa> pendientes = empresaRepository
    .findByAprobadoFalseAndActivoTrue();

// Buscar por nombre
List<Empresa> resultados = empresaRepository
    .findByNombreContainingIgnoreCase("tech");

// Obtener estadísticas
long aprobadas = empresaRepository.countByAprobadoTrue();
```

---

### 3. **OferenteRepository**

Métodos para gestionar oferentes (personas buscando empleo).

```java
Optional<Oferente> findByCorreo(String correo)
List<Oferente> findByAprobadoFalse()
List<Oferente> findByAprobadoTrue()
List<Oferente> findByAprobadoFalseAndActivoTrue()
Optional<Oferente> findByIdentificacion(String identificacion)
boolean existsByCorreo(String correo)
boolean existsByIdentificacion(String identificacion)
long countByAprobadoTrue()
long countByAprobadoFalse()
List<Oferente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido)
List<Oferente> findAllActivos()  // @Query custom
List<Oferente> findByNacionalidad(String nacionalidad)
List<Oferente> findAprobadosConCv()  // @Query custom
```

**Ejemplos de uso:**
```java
// Login
Optional<Oferente> oferente = oferenteRepository
    .findByCorreo("juan@gmail.com");

// Búsqueda por cédula
Optional<Oferente> porCedula = oferenteRepository
    .findByIdentificacion("118456789");

// Búsqueda por nombre
List<Oferente> resultados = oferenteRepository
    .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase("Juan", "Juan");

// Obtener aprobados con CV
List<Oferente> listos = oferenteRepository
    .findAprobadosConCv();

// Oferentes pendientes
List<Oferente> pendientes = oferenteRepository
    .findByAprobadoFalse();
```

---

### 4. **CaracteristicaRepository**

Métodos para gestionar características (habilidades) y su jerarquía.

```java
List<Caracteristica> findByPadreIsNull()
List<Caracteristica> findByPadre_IdCaracteristica(Integer idPadre)
Optional<Caracteristica> findByNombre(String nombre)
List<Caracteristica> findByNombreContainingIgnoreCase(String nombre)
List<Caracteristica> findByActivoTrue()
List<Caracteristica> findAllCategoriasPrincipales()  // @Query custom
List<Caracteristica> findHierarchy()  // @Query custom
List<Caracteristica> findActiveHijos(Integer idPadre)  // @Query custom
long countByPadre_IdCaracteristica(Integer idPadre)
boolean existsByNombre(String nombre)
```

**Ejemplos de uso:**
```java
// Obtener categorías principales
List<Caracteristica> categorias = caracteristicaRepository
    .findByPadreIsNull();

// Obtener subcategorías
List<Caracteristica> subcat = caracteristicaRepository
    .findByPadre_IdCaracteristica(1);  // Subcategorías de Bases de Datos

// Obtener árbol completo
List<Caracteristica> arbol = caracteristicaRepository
    .findHierarchy();

// Buscar por nombre
Optional<Caracteristica> java = caracteristicaRepository
    .findByNombre("Java");

// Contar hijos
long cantidad = caracteristicaRepository
    .countByPadre_IdCaracteristica(1);
```

---

### 5. **PuestoRepository**

Métodos para gestionar ofertas de empleo.

```java
List<Puesto> findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc(String tipoPublicacion)
List<Puesto> findByEmpresa_IdEmpresa(Integer idEmpresa)
List<Puesto> findByTipoPublicacionAndActivoTrue(String tipoPublicacion)
List<Puesto> findPublicosActivos()  // @Query custom
List<Puesto> findPrivadosByEmpresa(Integer idEmpresa)  // @Query custom
List<Puesto> findByEmpresa_IdEmpresaAndActivoTrue(Integer idEmpresa)
long countByActivoTrue()
long countByTipoPublicacionAndActivoTrue(String tipoPublicacion)
List<Puesto> findByRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax)  // @Query custom
List<Puesto> buscarPorKeyword(String keyword)  // @Query custom
```

**Ejemplos de uso:**
```java
// Puestos públicos más recientes
List<Puesto> recientes = puestoRepository
    .findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc("publico");

// Puestos de una empresa
List<Puesto> delEmpresa = puestoRepository
    .findByEmpresa_IdEmpresa(1);

// Puestos públicos y activos
List<Puesto> publicos = puestoRepository
    .findByTipoPublicacionAndActivoTrue("publico");

// Búsqueda por rango de salario
BigDecimal min = BigDecimal.valueOf(2000000);
BigDecimal max = BigDecimal.valueOf(3000000);
List<Puesto> enRango = puestoRepository
    .findByRangoSalario(min, max);

// Búsqueda por palabra clave
List<Puesto> resultados = puestoRepository
    .buscarPorKeyword("Java");

// Estadísticas
long activos = puestoRepository.countByActivoTrue();
```

---

### 6. **PuestoCaracteristicaRepository**

Métodos para gestionar características requeridas en puestos.

```java
List<PuestoCaracteristica> findByPuesto_IdPuesto(Integer idPuesto)
List<PuestoCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica)
PuestoCaracteristica findByPuestoAndCaracteristica(Integer idPuesto, Integer idCaracteristica)  // @Query custom
long countByPuesto_IdPuesto(Integer idPuesto)
List<PuestoCaracteristica> findByNivelRequerido(Integer idPuesto, Integer nivelMinimo)  // @Query custom
List<PuestoCaracteristica> findByPuestoOrderedByNivel(Integer idPuesto)  // @Query custom
List<PuestoCaracteristica> findPuestosWithMinNivel(Integer idCaracteristica, Integer nivelMinimo)  // @Query custom
boolean existsByPuesto_IdPuestoAndCaracteristica_IdCaracteristica(Integer idPuesto, Integer idCaracteristica)
```

**Ejemplos de uso:**
```java
// Características de un puesto
List<PuestoCaracteristica> reqs = puestoCaracteristicaRepository
    .findByPuesto_IdPuesto(1);

// Puestos que requieren una característica
List<PuestoCaracteristica> puestos = puestoCaracteristicaRepository
    .findByCaracteristica_IdCaracteristica(8);  // Java

// Características nivel 4+
List<PuestoCaracteristica> avanzadas = puestoCaracteristicaRepository
    .findByNivelRequerido(1, 4);

// Verificar si existe
boolean existe = puestoCaracteristicaRepository
    .existsByPuesto_IdPuestoAndCaracteristica_IdCaracteristica(1, 8);
```

---

### 7. **OferenteCaracteristicaRepository**

Métodos para gestionar características de oferentes.

```java
List<OferenteCaracteristica> findByOferente_IdOferente(Integer idOferente)
List<OferenteCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica)
OferenteCaracteristica findByOferenteAndCaracteristica(Integer idOferente, Integer idCaracteristica)  // @Query custom
long countByOferente_IdOferente(Integer idOferente)
List<OferenteCaracteristica> findByNivelMinimo(Integer idOferente, Integer nivelMinimo)  // @Query custom
List<OferenteCaracteristica> findByOferenteOrderedByNivel(Integer idOferente)  // @Query custom
List<OferenteCaracteristica> findOferentesWithMinNivel(Integer idCaracteristica, Integer nivelMinimo)  // @Query custom
List<OferenteCaracteristica> findAprobadosWithCaracteristica(Integer idCaracteristica)  // @Query custom
boolean existsByOferente_IdOferenteAndCaracteristica_IdCaracteristica(Integer idOferente, Integer idCaracteristica)
long countByCaracteristica_IdCaracteristica(Integer idCaracteristica)
```

**Ejemplos de uso:**
```java
// Características de un oferente
List<OferenteCaracteristica> caracteristicas = oferenteCaracteristicaRepository
    .findByOferente_IdOferente(1);

// Oferentes con una característica
List<OferenteCaracteristica> oferentes = oferenteCaracteristicaRepository
    .findByCaracteristica_IdCaracteristica(8);  // Java

// Oferentes aprobados con Java nivel 3+
List<OferenteCaracteristica> calificados = oferenteCaracteristicaRepository
    .findOferentesWithMinNivel(8, 3);

// Características nivel 4+
List<OferenteCaracteristica> avanzadas = oferenteCaracteristicaRepository
    .findByNivelMinimo(1, 4);
```

---

### 8. **CvRepository**

Métodos para gestionar CVs en PDF.

```java
Optional<Cv> findByOferente_IdOferente(Integer idOferente)
boolean existsByOferente_IdOferente(Integer idOferente)
long count()
List<Cv> findRecentCvs(LocalDateTime fecha)  // @Query custom
List<Cv> findCvsFromAprobados()  // @Query custom
List<Cv> findAllOrderByFecha()  // @Query custom
Optional<Cv> findByNombreArchivo(String nombreArchivo)
List<Cv> findByNacionalidad(String nacionalidad)  // @Query custom
Optional<Cv> findLatestCv()  // @Query custom
List<Cv> findRecentlyUpdated(LocalDateTime fecha)  // @Query custom
```

**Ejemplos de uso:**
```java
// Obtener CV de un oferente
Optional<Cv> cv = cvRepository
    .findByOferente_IdOferente(1);

// Verificar si existe CV
if (cvRepository.existsByOferente_IdOferente(1)) {
    // Obtener CV
}

// CVs recientes (últimos 7 días)
LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
List<Cv> recientes = cvRepository
    .findRecentCvs(hace7Dias);

// CVs de oferentes aprobados
List<Cv> aprobados = cvRepository
    .findCvsFromAprobados();

// CV más reciente
Optional<Cv> ultimoCv = cvRepository
    .findLatestCv();
```

---

## 🔍 Tipos de Métodos

### Métodos Derivados (Query Methods)
Spring Data JPA genera automáticamente queries basadas en el nombre del método:

```java
// Estos generan SQL automáticamente:
findByCorreo(String correo)
findByAprobadoFalse()
findByPadreIsNull()
findByPuesto_IdPuesto(Integer id)  // Foreign key drilling
```

### Métodos Personalizados con @Query
Para queries más complejas:

```java
@Query("SELECT e FROM Empresa e WHERE e.activo = true ORDER BY e.nombre ASC")
List<Empresa> findAllActivas();

@Query("SELECT c FROM Cv c WHERE c.fechaSubida >= :fecha ORDER BY c.fechaSubida DESC")
List<Cv> findRecentCvs(@Param("fecha") LocalDateTime fecha);
```

### Métodos de Conteo y Verificación

```java
long countByActivoTrue()          // COUNT(*)
boolean existsByCorreo(String)    // EXISTS
long countByPadre_IdCaracteristica(Integer)  // COUNT with FK
```

---

## 🎯 Patrones Comunes

### Login
```java
// Empresa/Oferente/Administrador
Optional<Entidad> usuario = repository.findByCorreo(correo);
if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getPasswordHash())) {
    // Login exitoso
}
```

### Búsquedas Avanzadas
```java
// Puestos en rango de salario
List<Puesto> puestos = puestoRepository
    .findByRangoSalario(min, max);

// Oferentes con características nivel 4+
List<OferenteCaracteristica> oferentes = oferenteCaracteristicaRepository
    .findByNivelMinimo(idOferente, 4);
```

### Matching (Oferentes para Puestos)
```java
// Características requeridas
List<PuestoCaracteristica> requeridas = puestoCaracteristicaRepository
    .findByPuesto_IdPuesto(idPuesto);

// Para cada característica, obtener oferentes
for (PuestoCaracteristica pc : requeridas) {
    List<OferenteCaracteristica> candidatos = oferenteCaracteristicaRepository
        .findOferentesWithMinNivel(pc.getCaracteristica().getIdCaracteristica(), 
                                   pc.getNivelRequerido());
}
```

### Filtros Múltiples
```java
// Empresas aprobadas y activas
List<Empresa> validas = empresaRepository
    .findByAprobadoTrueAndActivoTrue();

// Puestos públicos, activos de empresa X
List<Puesto> puestos = puestoRepository
    .findByEmpresa_IdEmpresaAndActivoTrue(idEmpresa);
```

---

## 💡 Notas Importantes

1. **Foreign Key Drilling**: `findByEmpresa_IdEmpresa` accede a propiedades de la entidad relacionada
2. **Lazy Loading**: Los repositorios utilizan LAZY loading en las entidades para evitar N+1
3. **Transaccionalidad**: Los servicios deben usar `@Transactional` para asegurar transacciones
4. **Validación**: Los métodos heredados de JpaRepository ya incluyen validación

---

## 🚀 Uso en Servicios

Todos los repositorios deben inyectarse en los servicios correspondientes:

```java
@Service
@Transactional
public class EmpresaService {
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    public Optional<Empresa> buscarPorCorreo(String correo) {
        return empresaRepository.findByCorreo(correo);
    }
    
    public List<Empresa> obtenerPendientes() {
        return empresaRepository.findByAprobadoFalse();
    }
}
```

---

**Última actualización**: 19 de marzo de 2026
**Versión**: 1.0 - Completo
**Estado**: ✅ Listo para usar con Spring Data JPA
