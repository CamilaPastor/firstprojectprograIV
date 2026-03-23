# Referencia Rápida de Spring Data JPA

## Interfaz Básica

```java
public interface MiRepository extends JpaRepository<MiEntidad, Long> {
    // Los métodos básicos vienen automáticamente
}
```

### Métodos heredados automáticamente

```java
miRepository.save(entidad);                    // Guardar
miRepository.saveAll(lista);                   // Guardar múltiples
miRepository.findById(1L);                     // Obtener por ID
miRepository.findAll();                        // Obtener todos
miRepository.findAll(Sort.by("nombre"));       // Con ordenamiento
miRepository.findAll(PageRequest.of(0, 10));   // Con paginación
miRepository.count();                          // Contar
miRepository.existsById(1L);                   // Existe por ID
miRepository.deleteById(1L);                   // Eliminar por ID
miRepository.delete(entidad);                  // Eliminar entidad
miRepository.deleteAll();                      // Eliminar todos
```

## Query Methods (Métodos derivados)

El nombre del método genera la query automáticamente.

### Buscar por propiedad

```java
// SELECT * FROM usuarios WHERE email = ?
Optional<Usuario> findByEmail(String email);

// SELECT * FROM usuarios WHERE nombre = ?
Optional<Usuario> findByNombre(String nombre);

// SELECT * FROM usuarios WHERE cedula = ?
Optional<Usuario> findByCedula(String cedula);
```

### Buscar con LIKE

```java
// SELECT * FROM usuarios WHERE nombre LIKE '%?%'
List<Usuario> findByNombreContaining(String nombre);
List<Usuario> findByNombreContainingIgnoreCase(String nombre);

// SELECT * FROM usuarios WHERE nombre LIKE '?%'
List<Usuario> findByNombreStartingWith(String nombre);

// SELECT * FROM usuarios WHERE nombre LIKE '%?'
List<Usuario> findByNombreEndingWith(String nombre);
```

### Buscar por rango

```java
// SELECT * FROM usuarios WHERE edad >= ?
List<Usuario> findByEdadGreaterThanEqual(Integer edad);

// SELECT * FROM usuarios WHERE edad > ?
List<Usuario> findByEdadGreaterThan(Integer edad);

// SELECT * FROM usuarios WHERE edad <= ?
List<Usuario> findByEdadLessThanEqual(Integer edad);

// SELECT * FROM usuarios WHERE edad < ?
List<Usuario> findByEdadLessThan(Integer edad);

// SELECT * FROM usuarios WHERE edad BETWEEN ? AND ?
List<Usuario> findByEdadBetween(Integer min, Integer max);
```

### Buscar booleanos

```java
// SELECT * FROM usuarios WHERE activo = true
List<Usuario> findByActivoTrue();

// SELECT * FROM usuarios WHERE activo = false
List<Usuario> findByActivoFalse();
```

### Buscar con AND/OR

```java
// SELECT * FROM usuarios WHERE nombre = ? AND email = ?
Optional<Usuario> findByNombreAndEmail(String nombre, String email);

// SELECT * FROM usuarios WHERE nombre = ? OR email = ?
List<Usuario> findByNombreOrEmail(String nombre, String email);
```

### NOT

```java
// SELECT * FROM usuarios WHERE nombre != ?
List<Usuario> findByNombreNot(String nombre);

// SELECT * FROM usuarios WHERE activo = false
List<Usuario> findByActivoNot(Boolean activo);
```

### IN y NOT IN

```java
// SELECT * FROM usuarios WHERE id IN (?,?,?)
List<Usuario> findByIdIn(List<Long> ids);

// SELECT * FROM usuarios WHERE id NOT IN (?,?,?)
List<Usuario> findByIdNotIn(List<Long> ids);

// SELECT * FROM usuarios WHERE tipoUsuario IN (?,?)
List<Usuario> findByTipoUsuarioIn(List<TipoUsuario> tipos);
```

### NULL

```java
// SELECT * FROM usuarios WHERE descripcion IS NULL
List<Usuario> findByDescripcionIsNull();

// SELECT * FROM usuarios WHERE descripcion IS NOT NULL
List<Usuario> findByDescripcionIsNotNull();
```

### Ordenamiento

```java
// ORDER BY nombre ASC
List<Usuario> findByActivoTrueOrderByNombreAsc(Boolean activo);

// ORDER BY nombre DESC
List<Usuario> findByActivoTrueOrderByNombreDesc(Boolean activo);

// ORDER BY nombre ASC, email DESC
List<Usuario> findAllByOrderByNombreAscEmailDesc();
```

### Limit

```java
// SELECT * FROM usuarios LIMIT 5
List<Usuario> findTop5ByOrderByNombreAsc();

// SELECT * FROM usuarios LIMIT 1
Optional<Usuario> findFirstByOrderByFechaCreacionDesc();
```

## @Query - Queries Personalizadas

### JPQL (Java Persistence Query Language)

```java
@Query("SELECT u FROM Usuario u WHERE u.activo = true")
List<Usuario> findActivos();

@Query("SELECT u FROM Usuario u WHERE u.email = ?1")
Optional<Usuario> findByEmailJPQL(String email);

@Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %?1%")
List<Usuario> buscarPorNombre(String nombre);

@Query("SELECT u FROM Usuario u WHERE u.edad >= ?1 AND u.activo = true")
List<Usuario> findAdultosActivos(Integer minEdad);

// Con parámetros nombrados
@Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.activo = :activo")
List<Usuario> findByEmailAndActivo(@Param("email") String email, 
                                    @Param("activo") Boolean activo);
```

### SQL Nativo

```java
@Query(value = "SELECT * FROM usuarios WHERE activo = true", nativeQuery = true)
List<Usuario> findActivosNative();

@Query(value = "SELECT * FROM usuarios WHERE email = ?1", nativeQuery = true)
Optional<Usuario> findByEmailNative(String email);

@Query(value = "SELECT COUNT(*) FROM usuarios WHERE tipo_usuario = ?1", nativeQuery = true)
long countByTipo(String tipoUsuario);
```

## Paginación y Ordenamiento

```java
// Página 0, 10 items por página
Page<Usuario> findAll(PageRequest.of(0, 10));

// Con ordenamiento
Page<Usuario> findAll(PageRequest.of(0, 10, Sort.by("nombre").ascending()));

// Con Sort
List<Usuario> findAll(Sort.by("nombre").ascending());

// En query personalizada
@Query("SELECT u FROM Usuario u WHERE u.activo = true")
Page<Usuario> findActivos(Pageable pageable);

// Uso
Page<Usuario> pagina = usuarioRepository.findAll(PageRequest.of(0, 10));
pagina.getTotalElements();   // Total de elementos
pagina.getTotalPages();      // Total de páginas
pagina.getContent();         // Lista de elementos de la página
pagina.hasNext();            // ¿Hay próxima página?
pagina.hasPrevious();        // ¿Hay página anterior?
pagina.getNumber();          // Número de página actual
pagina.getSize();            // Tamaño de la página
```

## Proyecciones (Interfaces)

```java
public interface UsuarioProjection {
    Long getId();
    String getNombre();
    String getEmail();
}

// En repository
List<UsuarioProjection> findByActivoTrue();

// O con DTOs
@Query("SELECT new com.bolsa.UsuarioDTO(u.id, u.nombre, u.email) " +
       "FROM Usuario u WHERE u.activo = true")
List<UsuarioDTO> findActivosDTO();
```

## Modificación

### @Modifying

```java
@Modifying
@Query("UPDATE Usuario u SET u.activo = false WHERE u.id = ?1")
int desactivarUsuario(Long id);

@Modifying
@Query("DELETE FROM Usuario u WHERE u.activo = false AND " +
       "u.fechaCreacion < ?1")
int eliminarInactivosAntiguos(LocalDateTime fecha);

@Modifying
@Transactional
@Query("UPDATE Usuario u SET u.nombre = ?1 WHERE u.id = ?2")
void actualizarNombre(String nombre, Long id);

// Uso
@Transactional
public void guardarCambios() {
    usuarioRepository.desactivarUsuario(1L);
    // Los cambios se graban automáticamente al finalizar
}
```

## Count y Exists

```java
@Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
long countActivos();

@Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.email = ?1")
boolean existsEmail(String email);

// Métodos derivados
long countByActivoTrue();
boolean existsByEmail(String email);
```

## Ejemplos Completos

### Repositorio Usuario Completo

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Búsquedas básicas
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCedula(String cedula);
    
    // Búsquedas activos
    List<Usuario> findByActivoTrue();
    List<Usuario> findByActivoFalse();
    
    // Búsquedas por tipo
    List<Usuario> findByTipoUsuarioAndActivoTrue(Usuario.TipoUsuario tipo);
    
    // Búsquedas con LIKE
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    // Counts
    long countByActivoTrue();
    
    // Queries personalizadas
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombre")
    List<Usuario> findActivosOrdenados();
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.activo = true")
    Optional<Usuario> findActiveByEmail(@Param("email") String email);
    
    // Paginación
    Page<Usuario> findByActivoTrue(Pageable pageable);
    
    // Modificación
    @Modifying
    @Query("UPDATE Usuario u SET u.activo = false WHERE u.id = ?1")
    int desactivar(Long id);
}
```

### Uso en Servicio

```java
@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    // Obtener con paginación
    public Page<Usuario> obtenerActivos(int page, int size) {
        return usuarioRepository.findByActivoTrue(
            PageRequest.of(page, size, Sort.by("nombre").ascending())
        );
    }
    
    // Búsqueda simple
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    // Búsqueda compleja
    public List<Usuario> buscarActivos(String nombre) {
        List<Usuario> resultado = usuarioRepository
            .findByNombreContainingIgnoreCase(nombre);
        return resultado.stream()
            .filter(Usuario::isActivo)
            .toList();
    }
    
    // Guardar
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    // Desactivar
    @Transactional
    public void desactivar(Long id) {
        usuarioRepository.desactivar(id);
    }
}
```

## Especificaciones (Specifications)

Para queries muy complejas:

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>,
                                          JpaSpecificationExecutor<Usuario> {
}

// Uso
public List<Usuario> buscarComplejo(String nombre, Boolean activo, String tipo) {
    Specification<Usuario> spec = Specification.where(null);
    
    if (nombre != null) {
        spec = spec.and((root, query, cb) -> 
            cb.like(root.get("nombre"), "%" + nombre + "%"));
    }
    
    if (activo != null) {
        spec = spec.and((root, query, cb) -> 
            cb.equal(root.get("activo"), activo));
    }
    
    if (tipo != null) {
        spec = spec.and((root, query, cb) -> 
            cb.equal(root.get("tipoUsuario"), tipo));
    }
    
    return usuarioRepository.findAll(spec);
}
```

## Tips

1. Preferir métodos derivados para queries simples
2. Usar `Optional<>` para resultados únicos
3. Usar `Page<>` para paginación automática
4. Siempre usar `@Transactional` en métodos que modifican
5. Usar `@Param` para queries con múltiples parámetros nombrados
6. Usar `nativeQuery = true` solo si es necesario
7. Verificar los logs SQL para optimizar queries

---

Para más info: https://spring.io/projects/spring-data-jpa
