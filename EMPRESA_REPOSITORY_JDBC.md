# EmpresaRepositoryJdbc - Documentación

## 📋 Descripción

`EmpresaRepositoryJdbc` es una clase de acceso a datos que utiliza **JDBC directo** para interactuar con la base de datos Oracle. Proporciona una alternativa a JPA/Spring Data con más control sobre las operaciones SQL.

---

## 🔑 Características Principales

### ✅ Métodos Implementados

| Método | Parámetros | Retorna | Descripción |
|--------|-----------|---------|-------------|
| `saveEmpresa` | Empresa | Long | Inserta nueva empresa y retorna ID generado |
| `findByCorreo` | String | Optional<Empresa> | Busca empresa por email |
| `findById` | Long | Optional<Empresa> | Busca empresa por ID |
| `findAllEmpresas` | - | List<Empresa> | Retorna todas las empresas |
| `findByAprobadoTrue` | - | List<Empresa> | Retorna empresas aprobadas |
| `findByAprobadoFalse` | - | List<Empresa> | Retorna empresas pendientes |
| `findByLocalizacion` | String | List<Empresa> | Busca por ubicación (LIKE) |
| `updateEmpresa` | Empresa | boolean | Actualiza empresa existente |
| `deleteEmpresa` | Long | boolean | Elimina empresa por ID |
| `countEmpresas` | - | long | Cuenta total de empresas |
| `existsByCorreo` | String | boolean | Verifica si existe correo |
| `executeQuery` | String | ResultSet | Ejecuta query personalizado |
| `executeUpdate` | String | int | Ejecuta INSERT/UPDATE/DELETE |

---

## 💻 Ejemplos de Uso

### 1. Crear Conexión y Repositorio

```java
// Opción 1: Con OracleDbConnection
OracleDbConnection dbConnection = new OracleDbConnection(
    "localhost", "1521", "XE", "system", "password"
);
EmpresaRepositoryJdbc repository = new EmpresaRepositoryJdbc(dbConnection);

// Opción 2: Directamente con parámetros
EmpresaRepositoryJdbc repository = new EmpresaRepositoryJdbc(
    "localhost", "1521", "XE", "system", "password"
);
```

### 2. Guardar Nueva Empresa

```java
Empresa empresa = new Empresa();
empresa.setNombre("Tech Solutions S.A.");
empresa.setCorreo("info@techsolutions.com");
empresa.setTelefono("(55) 1234-5678");
empresa.setLocalizacion("Ciudad de México");
empresa.setDescripcion("Empresa de tecnología");
empresa.setPasswordHash(PasswordHashUtil.hashPassword("password123"));
empresa.setAprobado(false);

try {
    Long id = repository.saveEmpresa(empresa);
    System.out.println("Empresa guardada con ID: " + id);
} catch (SQLException e) {
    System.err.println("Error al guardar: " + e.getMessage());
}
```

### 3. Buscar por Correo

```java
try {
    Optional<Empresa> empresa = repository.findByCorreo("info@techsolutions.com");
    
    if (empresa.isPresent()) {
        System.out.println("Empresa encontrada: " + empresa.get().getNombre());
    } else {
        System.out.println("Empresa no encontrada");
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 4. Listar Todas las Empresas

```java
try {
    List<Empresa> todas = repository.findAllEmpresas();
    
    for (Empresa e : todas) {
        System.out.println(e.getNombre() + " - " + e.getCorreo());
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 5. Filtrar Empresas Aprobadas

```java
try {
    List<Empresa> aprobadas = repository.findByAprobadoTrue();
    
    System.out.println("Empresas aprobadas: " + aprobadas.size());
    
    for (Empresa e : aprobadas) {
        System.out.println("✓ " + e.getNombre());
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 6. Buscar por Localización

```java
try {
    List<Empresa> enMonterrey = repository.findByLocalizacion("Monterrey");
    
    for (Empresa e : enMonterrey) {
        System.out.println(e.getNombre() + " - " + e.getLocalizacion());
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 7. Actualizar Empresa

```java
try {
    Empresa empresa = repository.findById(1L).get();
    empresa.setNombre("Tech Solutions México");
    empresa.setAprobado(true);
    
    boolean actualizada = repository.updateEmpresa(empresa);
    
    if (actualizada) {
        System.out.println("✓ Empresa actualizada");
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 8. Eliminar Empresa

```java
try {
    boolean eliminada = repository.deleteEmpresa(1L);
    
    if (eliminada) {
        System.out.println("✓ Empresa eliminada");
    } else {
        System.out.println("✗ No se encontró empresa para eliminar");
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 9. Contar Total de Empresas

```java
try {
    long total = repository.countEmpresas();
    System.out.println("Total de empresas: " + total);
} catch (SQLException e) {
    e.printStackTrace();
}
```

### 10. Verificar Existencia de Correo

```java
try {
    if (repository.existsByCorreo("info@techsolutions.com")) {
        System.out.println("✓ Correo ya registrado");
    } else {
        System.out.println("✗ Correo disponible");
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 🔍 Queries SQL Generados

### findAllEmpresas()
```sql
SELECT * FROM EMPRESA ORDER BY FECHA_REGISTRO DESC
```

### findByCorreo(String correo)
```sql
SELECT * FROM EMPRESA WHERE CORREO = ?
```

### findByAprobadoTrue()
```sql
SELECT * FROM EMPRESA WHERE APROBADO = 1 ORDER BY NOMBRE
```

### findByAprobadoFalse()
```sql
SELECT * FROM EMPRESA WHERE APROBADO = 0 ORDER BY FECHA_REGISTRO DESC
```

### findByLocalizacion(String localizacion)
```sql
SELECT * FROM EMPRESA WHERE UPPER(LOCALIZACION) LIKE UPPER(?) ORDER BY NOMBRE
```

### saveEmpresa(Empresa empresa)
```sql
INSERT INTO EMPRESA (ID_EMPRESA, NOMBRE, LOCALIZACION, CORREO, TELEFONO, 
                     DESCRIPCION, PASSWORD_HASH, APROBADO, FECHA_REGISTRO) 
VALUES (empresa_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)
```

### updateEmpresa(Empresa empresa)
```sql
UPDATE EMPRESA SET NOMBRE = ?, LOCALIZACION = ?, TELEFONO = ?, 
                    DESCRIPCION = ?, APROBADO = ? 
WHERE ID_EMPRESA = ?
```

### deleteEmpresa(Long id)
```sql
DELETE FROM EMPRESA WHERE ID_EMPRESA = ?
```

---

## ⚙️ Características Técnicas

### Manejo de Conexiones
- ✅ Try-with-resources para cierre automático
- ✅ Reutilización de OracleDbConnection
- ✅ Gestión automática de PreparedStatements

### Seguridad
- ✅ PreparedStatements contra SQL Injection
- ✅ Validación de datos
- ✅ Manejo de excepciones

### Performance
- ✅ Índices en columnas frecuentes (CORREO, APROBADO, LOCALIZACION)
- ✅ Ordenamiento optimizado en queries
- ✅ Uso de LIKE con índices para búsquedas

### Error Handling
- ✅ Throws SQLException para manejo en cliente
- ✅ Logging de errores en System.err
- ✅ Mensajes descriptivos

---

## 🔄 Mapeo ResultSet a Objeto

```java
private Empresa mapResultSetToEmpresa(ResultSet rs) throws SQLException {
    Empresa empresa = new Empresa();
    
    empresa.setIdEmpresa(rs.getLong("ID_EMPRESA"));
    empresa.setNombre(rs.getString("NOMBRE"));
    empresa.setLocalizacion(rs.getString("LOCALIZACION"));
    empresa.setCorreo(rs.getString("CORREO"));
    empresa.setTelefono(rs.getString("TELEFONO"));
    empresa.setDescripcion(rs.getString("DESCRIPCION"));
    empresa.setPasswordHash(rs.getString("PASSWORD_HASH"));
    
    int aprobado = rs.getInt("APROBADO");
    empresa.setAprobado(aprobado == 1);
    
    Timestamp timestamp = rs.getTimestamp("FECHA_REGISTRO");
    if (timestamp != null) {
        empresa.setFechaRegistro(timestamp.toLocalDateTime());
    }
    
    return empresa;
}
```

---

## 📊 Casos de Uso Comunes

### Caso 1: Sistema de Login
```java
EmpresaRepositoryJdbc repo = new EmpresaRepositoryJdbc(...);

Optional<Empresa> empresa = repo.findByCorreo(email);

if (empresa.isPresent() && 
    PasswordHashUtil.verifyPassword(password, empresa.get().getPasswordHash()) &&
    empresa.get().getAprobado()) {
    // Login exitoso
}
```

### Caso 2: Panel de Administración
```java
// Empresas pendientes de aprobación
List<Empresa> pendientes = repo.findByAprobadoFalse();

for (Empresa e : pendientes) {
    // Mostrar en panel para que admin apruebe/rechace
    repo.updateEmpresa(e);
}
```

### Caso 3: Dashboard de Empresas
```java
// Obtener estadísticas
long total = repo.countEmpresas();
List<Empresa> aprobadas = repo.findByAprobadoTrue();

System.out.println("Total: " + total);
System.out.println("Aprobadas: " + aprobadas.size());
System.out.println("Pendientes: " + (total - aprobadas.size()));
```

### Caso 4: Búsqueda Avanzada
```java
// Empresas en una región específica
List<Empresa> enNorte = repo.findByLocalizacion("Monterrey");
List<Empresa> enSur = repo.findByLocalizacion("Cancún");
```

---

## 🧪 Ejemplo Completo: Workflow de Empresa

```java
public class EmpresaWorkflow {
    private EmpresaRepositoryJdbc repository;
    
    public EmpresaWorkflow(EmpresaRepositoryJdbc repository) {
        this.repository = repository;
    }
    
    // 1. Registrar empresa
    public Long registrar(String nombre, String correo, String password) 
            throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setCorreo(correo);
        empresa.setPasswordHash(PasswordHashUtil.hashPassword(password));
        empresa.setAprobado(false);
        
        return repository.saveEmpresa(empresa);
    }
    
    // 2. Login
    public Optional<Empresa> login(String correo, String password) 
            throws SQLException {
        Optional<Empresa> empresa = repository.findByCorreo(correo);
        
        if (empresa.isPresent() && 
            PasswordHashUtil.verifyPassword(password, empresa.get().getPasswordHash())) {
            return empresa;
        }
        return Optional.empty();
    }
    
    // 3. Aprobar empresa
    public void aprobar(Long empresaId) throws SQLException {
        Optional<Empresa> empresa = repository.findById(empresaId);
        if (empresa.isPresent()) {
            Empresa e = empresa.get();
            e.setAprobado(true);
            repository.updateEmpresa(e);
        }
    }
    
    // 4. Listar empresas aprobadas (para búsqueda pública)
    public List<Empresa> listarPublicadas() throws SQLException {
        return repository.findByAprobadoTrue();
    }
}
```

---

## 🚀 Ventajas de JDBC vs JPA

| Aspecto | JDBC | JPA |
|--------|------|-----|
| Control SQL | Total | Automático |
| Performance | Optimizado | Depende de lazy loading |
| Complejidad | Mayor | Menor |
| Curva de aprendizaje | Menor | Mayor |
| Queries complejas | Fácil | Difícil |
| Cambios de esquema | Manual | Automático |

---

## ⚠️ Consideraciones

1. **Excepciones**: Siempre manejar `SQLException`
2. **Recursos**: Usar try-with-resources para seguridad
3. **Transacciones**: Considerar manual si es necesario
4. **Validación**: Validar datos antes de guardar
5. **Cifrado**: Siempre cifrar passwordHash antes de guardar

---

## 📚 Documentación Relacionada

- `OracleDbConnection.java` - Gestor de conexiones
- `Empresa.java` - Modelo de datos
- `EmpresaValidator.java` - Validaciones
- `PasswordHashUtil.java` - Cifrado de contraseñas
- `EmpresaRepositoryJdbcExample.java` - Ejemplos de uso

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.0.0
