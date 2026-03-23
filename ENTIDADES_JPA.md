# 📦 Entidades JPA - Bolsa de Empleo

## Descripción General

Se han creado 8 entidades JPA que mapean exactamente con las tablas del script SQL. Todas utilizan:
- **Anotaciones JPA**: @Entity, @Table, @Column, @ManyToOne, @OneToMany, @OneToOne
- **Lombok**: @Data, @NoArgsConstructor, @AllArgsConstructor, @ToString.Exclude
- **Validación**: Jakarta Validation Constraints (@NotBlank, @Email, @Min, @Max, etc.)
- **Auditoría**: @PrePersist y @PreUpdate para timestamps automáticos

---

## 📋 Entidades Creadas

### 1. **Administrador**
**Paquete**: `cr.una.bolsaempleo.model.Administrador`

```java
@Entity
@Table(name = "administrador")
public class Administrador {
    @Id Integer idAdmin;
    String identificacion (UNIQUE)
    String passwordHash
    LocalDateTime fechaCreacion
    Boolean activo
}
```

**Características:**
- Administradores del sistema
- Identificación única
- Password hasheada en BCrypt
- Fecha de creación automática
- Estado activo/inactivo

---

### 2. **Empresa**
**Paquete**: `cr.una.bolsaempleo.model.Empresa`

```java
@Entity
@Table(name = "empresa")
public class Empresa {
    @Id Integer idEmpresa;
    String nombre
    String localizacion
    String correo (UNIQUE)
    String telefono
    String descripcion
    String passwordHash
    Boolean aprobado
    LocalDateTime fechaRegistro
    Boolean activo
    @OneToMany Set<Puesto> puestos
    @Transient String passwordSinHashear
}
```

**Relaciones:**
- `puestos`: Relación 1:N con Puesto (CASCADE, orphanRemoval)

**Campos Especiales:**
- `passwordSinHashear`: Campo @Transient para manejar password sin hashear en formularios
- `aprobado`: Controla si la empresa fue aprobada por administrador

---

### 3. **Oferente**
**Paquete**: `cr.una.bolsaempleo.model.Oferente`

```java
@Entity
@Table(name = "oferente")
public class Oferente {
    @Id Integer idOferente;
    String identificacion (UNIQUE)
    String nombre
    String apellido
    String nacionalidad
    String telefono
    String correo (UNIQUE)
    String residencia
    String passwordHash
    Boolean aprobado
    LocalDateTime fechaRegistro
    Boolean activo
    @OneToMany Set<OferenteCaracteristica> caracteristicas
    @OneToOne Cv cv
    @Transient String passwordSinHashear
}
```

**Relaciones:**
- `caracteristicas`: Relación N:M con Caracteristica (a través de OferenteCaracteristica)
- `cv`: Relación 1:1 UNIQUE con Cv (CASCADE, orphanRemoval)

**Campos Especiales:**
- `passwordSinHashear`: Para formularios de registro/cambio de contraseña
- `identificacion`: Patrón de validación para cédula/pasaporte

---

### 4. **Caracteristica**
**Paquete**: `cr.una.bolsaempleo.model.Caracteristica`

```java
@Entity
@Table(name = "caracteristica")
public class Caracteristica {
    @Id Integer idCaracteristica;
    String nombre
    String descripcion
    @ManyToOne Caracteristica padre
    @OneToMany Set<Caracteristica> hijos
    Integer nivelMinimo (default 1)
    Integer nivelMaximo (default 5)
    Boolean activo
    LocalDateTime fechaCreacion
    @OneToMany Set<PuestoCaracteristica> puestosRequeridos
    @OneToMany Set<OferenteCaracteristica> oferentesConCaracteristica
}
```

**Relaciones:**
- `padre`: AUTO-REFERENCIA @ManyToOne a sí misma (id_padre)
- `hijos`: AUTO-REFERENCIA @OneToMany inversa (CASCADE, orphanRemoval)
- `puestosRequeridos`: Relación N:M con Puesto
- `oferentesConCaracteristica`: Relación N:M con Oferente

**Estructura Jerárquica:**
```
Bases de Datos (padre=null)
├─ MySQL (padre=1)
├─ PostgreSQL (padre=1)
└─ ...

Lenguajes de Programación (padre=null)
├─ Java (padre=2)
├─ Python (padre=2)
└─ ...
```

---

### 5. **Puesto**
**Paquete**: `cr.una.bolsaempleo.model.Puesto`

```java
@Entity
@Table(name = "puesto")
public class Puesto {
    @Id Integer idPuesto;
    @ManyToOne Empresa empresa
    String descripcion
    BigDecimal salario
    String tipoPublicacion // "publico" o "privado"
    Boolean activo
    LocalDateTime fechaRegistro
    LocalDateTime fechaActualizacion
    @OneToMany Set<PuestoCaracteristica> caracteristicasRequeridas
}
```

**Relaciones:**
- `empresa`: Relación N:1 con Empresa (NO cascade)
- `caracteristicasRequeridas`: Relación N:M con Caracteristica (CASCADE)

**Campos Especiales:**
- `tipoPublicacion`: String con valores "publico" o "privado"
- `salario`: BigDecimal para precisión monetaria
- `fechaActualizacion`: Se actualiza automáticamente (@PreUpdate)

---

### 6. **PuestoCaracteristica**
**Paquete**: `cr.una.bolsaempleo.model.PuestoCaracteristica`

```java
@Entity
@Table(name = "puesto_caracteristica")
public class PuestoCaracteristica {
    @Id Integer id;
    @ManyToOne Puesto puesto
    @ManyToOne Caracteristica caracteristica
    Integer nivelRequerido (1-5)
    LocalDateTime fechaCreacion
}
```

**Relaciones:**
- `puesto`: Relación N:1 (Foreign Key)
- `caracteristica`: Relación N:1 (Foreign Key)

**Constraints:**
- UNIQUE (id_puesto, id_caracteristica): Una característica por puesto
- CHECK nivelRequerido BETWEEN 1 AND 5

---

### 7. **OferenteCaracteristica**
**Paquete**: `cr.una.bolsaempleo.model.OferenteCaracteristica`

```java
@Entity
@Table(name = "oferente_caracteristica")
public class OferenteCaracteristica {
    @Id Integer id;
    @ManyToOne Oferente oferente
    @ManyToOne Caracteristica caracteristica
    Integer nivel (1-5)
    LocalDateTime fechaCreacion
}
```

**Relaciones:**
- `oferente`: Relación N:1 (Foreign Key)
- `caracteristica`: Relación N:1 (Foreign Key)

**Constraints:**
- UNIQUE (id_oferente, id_caracteristica): Una característica por oferente
- CHECK nivel BETWEEN 1 AND 5

---

### 8. **Cv**
**Paquete**: `cr.una.bolsaempleo.model.Cv`

```java
@Entity
@Table(name = "cv")
public class Cv {
    @Id Integer id;
    @OneToOne Oferente oferente (UNIQUE)
    @Lob byte[] archivoPdf
    String nombreArchivo
    LocalDateTime fechaSubida
    LocalDateTime fechaActualizacion
}
```

**Relaciones:**
- `oferente`: Relación 1:1 UNIQUE con Oferente

**Campos Especiales:**
- `archivoPdf`: Anotación @Lob para almacenar BLOB (PDF)
- `archivoPdf`: @Column columnDefinition = "LONGBLOB"
- @ToString.Exclude en archivoPdf (no incluir en logs)

---

## 🔗 Diagrama de Relaciones

```
┌──────────────────┐
│  ADMINISTRADOR   │
└──────────────────┘

┌──────────────┐              ┌──────────────┐
│   EMPRESA    │──────1:N────→│   PUESTO     │
└──────────────┘              └──────────────┘
                                      │
                                      │ N:M
                                      │
                   ┌──────────────────┴─────────────┐
                   │                                │
        ┌──────────────────────┐       ┌──────────────────────┐
        │ PUESTO_CARACTERISTICA│       │ OFERENTE_CARACTERIS  │
        └──────────────────────┘       └──────────────────────┘
                   │                                │
                   └──────────────────┬─────────────┘
                                      │ N:M
                        ┌─────────────────────────┐
                        │  CARACTERISTICA         │
                        │  (Jerarquía: padre-hijo)│
                        └─────────────────────────┘

┌──────────────┐              ┌──────────────┐
│  OFERENTE    │──────1:1────→│      CV      │
└──────────────┘              └──────────────┘
       │
       │ N:M
       │
┌──────────────────────────────────┐
│ OFERENTE_CARACTERISTICA          │
└──────────────────────────────────┘
```

---

## 🔐 Validaciones Incluidas

### Administrador
- `identificacion`: @NotBlank, @Size(3-50), UNIQUE
- `passwordHash`: @NotBlank, @Size(min 20)

### Empresa
- `nombre`: @NotBlank, @Size(3-150)
- `correo`: @NotBlank, @Email, UNIQUE
- `passwordHash`: @NotBlank, @Size(min 20)

### Oferente
- `identificacion`: @Pattern(8-20 dígitos), UNIQUE
- `nombre`: @NotBlank, @Size(3-100)
- `apellido`: @NotBlank, @Size(3-100)
- `correo`: @NotBlank, @Email, UNIQUE
- `passwordHash`: @NotBlank, @Size(min 20)

### Caracteristica
- `nombre`: @NotBlank, @Size(3-150)
- `nivelMinimo/Maximo`: Valores por defecto 1 y 5

### Puesto
- `descripcion`: @NotBlank
- `salario`: @DecimalMin(0.0)
- `tipoPublicacion`: String "publico" o "privado"

### PuestoCaracteristica / OferenteCaracteristica
- `nivel/nivelRequerido`: @Min(1), @Max(5)

### Cv
- `archivoPdf`: @NotNull, @Lob
- `nombreArchivo`: @NotBlank, @Size(max 255)

---

## 📊 Ejemplos de Uso

### Crear una empresa
```java
Empresa empresa = new Empresa();
empresa.setNombre("TechCorp");
empresa.setCorreo("info@techcorp.cr");
empresa.setLocalizacion("San José");
empresa.setPasswordHash(passwordEncoder.encode("password123"));
empresaRepository.save(empresa);
```

### Agregar característica a oferente
```java
OferenteCaracteristica oc = new OferenteCaracteristica();
oc.setOferente(oferente);
oc.setCaracteristica(java);  // Caracteristica ID 8
oc.setNivel(4);  // Nivel de dominio
oferenteCaracteristicaRepository.save(oc);
```

### Crear jerarquía de características
```java
Caracteristica padre = new Caracteristica();
padre.setNombre("Bases de Datos");
padre.setIdPadre(null);

Caracteristica hijo = new Caracteristica();
hijo.setNombre("MySQL");
hijo.setIdPadre(padre.getIdCaracteristica());

caracteristicaRepository.save(padre);
caracteristicaRepository.save(hijo);
```

### Agregar CV a oferente
```java
Cv cv = new Cv();
cv.setOferente(oferente);
cv.setArchivoPdf(bytes);  // byte[] del PDF
cv.setNombreArchivo("Juan_Perez_CV.pdf");
cvRepository.save(cv);
```

---

## 🎯 Características Principales

### Auditoría Automática
- @PrePersist: Establece fechaCreacion automáticamente
- @PreUpdate: Actualiza fechaActualizacion automáticamente

### Valores por Defecto
- `activo`: TRUE por defecto
- `aprobado`: FALSE por defecto (requiere aprobación admin)
- `tipoPublicacion`: "publico" por defecto
- `nivel/nivelRequerido`: 3 por defecto
- `nivelMinimo/Maximo`: 1 y 5 por defecto

### Foreign Keys
- Todos con `ON DELETE CASCADE`
- Algunos con `orphanRemoval = true`
- Fetch estrategia LAZY para evitar lazy loading problems

### Índices
- PRIMARY KEY en todas
- UNIQUE en campos únicos
- Índices compuestos para búsquedas frecuentes
- Índices en foreign keys

### Transactionalidad
- @Transient para campos no persistentes
- @ToString.Exclude para passwords y BLOBs
- Lombok genera automáticamente getters/setters

---

## 📝 Notas Importantes

1. **Password**: Usar campo @Transient `passwordSinHashear` para entrada, hashear antes de persistir
2. **Jerarquía**: Caracteristica se auto-referencia a través de `padre` (ManyToOne) e `hijos` (OneToMany)
3. **BLOB**: Cv almacena archivos PDF directamente en BD (byte array)
4. **Cascadas**: Deleting empresa deletes all puestos, deleting oferente deletes caracteristicas y cv
5. **Timestamps**: Automáticos en @PrePersist/@PreUpdate, no settear manualmente
6. **Enum String**: TipoPublicacion es String, NO enumeración Java (para compatibilidad SQL)

---

**Última actualización**: 19 de marzo de 2026
**Versión**: 1.0 - Completo
**Estado**: ✅ Listo para usar con Spring Data JPA
