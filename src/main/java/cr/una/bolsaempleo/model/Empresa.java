package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "empresa", indexes = {
    @Index(name = "idx_correo", columnList = "correo"),
    @Index(name = "idx_aprobado", columnList = "aprobado"),
    @Index(name = "idx_activo", columnList = "activo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Size(max = 200, message = "La localización no puede exceder 200 caracteres")
    @Column(name = "localizacion", length = 200)
    private String localizacion;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 20, message = "El hash debe tener al menos 20 caracteres")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "aprobado", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean aprobado = false;

    @Column(name = "fecha_registro", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Puesto> puestos;

    @Transient
    @ToString.Exclude
    private String passwordSinHashear;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (aprobado == null) {
            aprobado = false;
        }
        if (activo == null) {
            activo = true;
        }
    }
}
