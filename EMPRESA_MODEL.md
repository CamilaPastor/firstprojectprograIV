# Empresa Model - Documentación

## 📋 Estructura de la Entidad Empresa

### Campos Principales

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| `idEmpresa` | Long | PK, Auto-generado | Identificador único de la empresa |
| `nombre` | String | NOT NULL, 3-150 caracteres | Nombre de la empresa |
| `localizacion` | String | Max 255 caracteres | Ubicación/dirección de la empresa |
| `correo` | String | NOT NULL, UNIQUE | Email de contacto (único) |
| `telefono` | String | Max 20 caracteres | Teléfono de la empresa |
| `descripcion` | String | CLOB | Descripción detallada de la empresa |
| `passwordHash` | String | NOT NULL, Min 10 caracteres | Hash cifrado de la contraseña |
| `aprobado` | Boolean | NOT NULL, Default false | Estado de aprobación de la empresa |
| `fechaRegistro` | LocalDateTime | Auto-generado | Fecha y hora del registro |

---

## 🔍 Anotaciones JPA

```java
@Entity                          // Indica que es una entidad de BD
@Table(name = "EMPRESA")         // Nombre de la tabla en BD
@Data                            // Lombok: genera getters, setters, equals, hashCode, toString
@NoArgsConstructor               // Lombok: constructor sin argumentos
@AllArgsConstructor              // Lombok: constructor con todos los argumentos
public class Empresa {
    
    @Id                          // Clave primaria
    @GeneratedValue(...)         // Auto-generado por secuencia
    @SequenceGenerator(...)      // Define la secuencia
    @Column(name = "ID_EMPRESA") // Nombre en BD
    private Long idEmpresa;
    
    @Column(name = "NOMBRE", nullable = false, length = 150)
    private String nombre;
    // ...
}
```

---

## 💾 Ejemplo de Uso

### 1. Crear una Nueva Empresa

```java
// Crear instancia
Empresa empresa = new Empresa();
empresa.setNombre("Tech Solutions S.A.");
empresa.setCorreo("info@techsolutions.com");
empresa.setTelefono("(555) 123-4567");
empresa.setLocalizacion("Ciudad de México, CDMX");
empresa.setDescripcion("Empresa líder en soluciones tecnológicas");
empresa.setPasswordHash("miContraseña123");  // Se cifrará automáticamente

// Registrar (incluye validación y hash de contraseña)
Empresa empresaRegistrada = empresaService.registrarEmpresa(empresa);
```

### 2. Validar Datos

```java
// Validar empresa completa
if (EmpresaValidator.isValidEmpresa(empresa)) {
    System.out.println("Empresa válida");
} else {
    String errores = EmpresaValidator.getValidationErrors(empresa);
    System.out.println(errores);
}

// Validar campos individuales
if (EmpresaValidator.isValidEmail("test@example.com")) {
    System.out.println("Email válido");
}

if (EmpresaValidator.isValidPhone("(555) 123-4567")) {
    System.out.println("Teléfono válido");
}
```

### 3. Cifrar Contraseña

```java
// Cifrar contraseña
String passwordPlain = "miContraseña123";
String passwordHash = PasswordHashUtil.hashPassword(passwordPlain);
empresa.setPasswordHash(passwordHash);

// Verificar contraseña
if (PasswordHashUtil.verifyPassword(passwordPlain, passwordHash)) {
    System.out.println("Contraseña correcta");
}
```

### 4. Operaciones de Servicio

```java
// Obtener empresa por ID
Optional<Empresa> empresa = empresaService.obtenerEmpresaPorId(1L);

// Obtener empresa por correo
Optional<Empresa> empresa = empresaService.obtenerEmpresaPorCorreo("info@techsolutions.com");

// Listar todas las empresas
List<Empresa> todas = empresaService.listarEmpresas();

// Listar empresas aprobadas
List<Empresa> aprobadas = empresaService.listarEmpresasAprobadas();

// Listar empresas pendientes
List<Empresa> pendientes = empresaService.listarEmpresasPendientes();

// Buscar por localización
List<Empresa> enCdmx = empresaService.buscarPorLocalizacion("Ciudad de México");

// Actualizar empresa
empresaService.actualizarEmpresa(empresa);

// Aprobar empresa
empresaService.aprobarEmpresa(1L);

// Desaprobar empresa
empresaService.desaprobarEmpresa(1L);

// Eliminar empresa
empresaService.eliminarEmpresa(1L);

// Validar credenciales (login)
if (empresaService.validarCredenciales("info@techsolutions.com", "miContraseña123")) {
    System.out.println("Login exitoso");
}
```

---

## 🗄️ Script SQL para Tabla EMPRESA

```sql
-- Crear secuencia
CREATE SEQUENCE empresa_seq START WITH 1 INCREMENT BY 1;

-- Crear tabla
CREATE TABLE EMPRESA (
    ID_EMPRESA NUMBER PRIMARY KEY,
    NOMBRE VARCHAR2(150) NOT NULL,
    LOCALIZACION VARCHAR2(255),
    CORREO VARCHAR2(100) NOT NULL UNIQUE,
    TELEFONO VARCHAR2(20),
    DESCRIPCION CLOB,
    PASSWORD_HASH VARCHAR2(255) NOT NULL,
    APROBADO NUMBER(1) DEFAULT 0 NOT NULL,
    FECHA_REGISTRO TIMESTAMP,
    
    CONSTRAINT chk_aprobado CHECK (APROBADO IN (0, 1))
);

-- Crear índices
CREATE INDEX idx_empresa_correo ON EMPRESA(CORREO);
CREATE INDEX idx_empresa_aprobado ON EMPRESA(APROBADO);
CREATE INDEX idx_empresa_localizacion ON EMPRESA(LOCALIZACION);

-- Datos de prueba
INSERT INTO EMPRESA (ID_EMPRESA, NOMBRE, CORREO, PASSWORD_HASH, APROBADO, FECHA_REGISTRO)
VALUES (empresa_seq.NEXTVAL, 'Tech Solutions', 'tech@example.com', 
        'hash_aqui_123456', 1, SYSDATE);

COMMIT;
```

---

## ✅ Validaciones Automáticas

### Validaciones de Email
```
✓ Formato correcto (ejemplo@dominio.com)
✓ Único en la base de datos
```

### Validaciones de Contraseña
```
✓ Mínimo 10 caracteres
✓ Se cifra automáticamente con SHA-256 + Salt
✓ Se verifica sin guardar en texto plano
```

### Validaciones de Nombre
```
✓ Mínimo 3 caracteres
✓ Máximo 150 caracteres
✓ No puede estar vacío
```

### Validaciones de Teléfono
```
✓ Formato: 7-20 caracteres
✓ Permite dígitos, espacios, paréntesis, guiones, +
```

---

## 🔐 Seguridad

### Hash de Contraseña
- Algoritmo: SHA-256 con Salt
- Salt: 16 bytes aleatorios por contraseña
- Encoding: Base64
- Verificación: Constante-time para evitar timing attacks

### Campos Sensibles
- `passwordHash`: Nunca se devuelve en APIs
- `correo`: Único en la BD (constrain UNIQUE)
- Validación de email antes de guardar

---

## 📊 Relaciones

```
Empresa (1) ──────────────(N) Puesto
   │
   └─→ Relación: @OneToMany(mappedBy = "empresa")
       
Puesto tiene:
   @ManyToOne
   @JoinColumn(name = "EMPRESA_ID")
   private Empresa empresa;
```

---

## 🔄 Ciclo de Vida de Aprobación

```
┌─────────────┐
│  REGISTRADA │  (aprobado = false)
├─────────────┤
│  PENDIENTE  │  Esperando aprobación del administrador
└──────┬──────┘
       │
       ├─→ APROBAR ──→ ┌──────────┐
       │               │ APROBADA │ (aprobado = true)
       │               └──────────┘
       │
       └─→ RECHAZAR ──→ ┌──────────┐
                        │ RECHAZADA│ (aprobado = false)
                        └──────────┘
```

---

## 🎯 Métodos de Validación

### EmpresaValidator

```java
// Validaciones individuales
isValidEmail(String correo)           // Valida formato de email
isValidPhone(String telefono)         // Valida teléfono
isValidNombre(String nombre)          // Valida nombre (3-150 car)
isValidLocalizacion(String loc)       // Valida localización
isValidDescripcion(String desc)       // Valida descripción
isValidPasswordHash(String pass)      // Valida hash (min 10 car)

// Validación completa
isValidEmpresa(Empresa empresa)       // Valida toda la empresa
getValidationErrors(Empresa emp)      // Retorna errores encontrados
```

---

## 📱 Ejemplo Completo: Registro de Empresa

```java
@PostMapping("/registro")
public String registrarEmpresa(@ModelAttribute Empresa empresa, Model model) {
    try {
        // El servicio se encarga de:
        // 1. Validar datos
        // 2. Verificar email único
        // 3. Cifrar contraseña
        // 4. Establecer aprobado = false
        // 5. Guardar en BD
        Empresa nuevaEmpresa = empresaService.registrarEmpresa(empresa);
        
        model.addAttribute("mensaje", "Registro exitoso. En espera de aprobación.");
        return "redirect:/empresa/login";
        
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("empresa", empresa);
        return "empresa/registro";
    }
}
```

---

## 🧪 Testing

```java
@Test
public void testRegistroEmpresa() {
    Empresa empresa = new Empresa();
    empresa.setNombre("Test Company");
    empresa.setCorreo("test@example.com");
    empresa.setPasswordHash("password123");
    
    Empresa resultado = empresaService.registrarEmpresa(empresa);
    
    assertNotNull(resultado);
    assertTrue(resultado.getIdEmpresa() > 0);
    assertFalse(resultado.getAprobado());
}

@Test
public void testValidarCredenciales() {
    boolean valido = empresaService.validarCredenciales(
        "test@example.com", 
        "password123"
    );
    assertTrue(valido);
}
```

---

**Última actualización:** 18 de Marzo de 2026  
**Versión:** 1.1.0
