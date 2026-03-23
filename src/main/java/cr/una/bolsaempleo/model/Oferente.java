package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "oferente", indexes = {
    @Index(name = "idx_identificacion", columnList = "identificacion"),
    @Index(name = "idx_correo", columnList = "correo"),
    @Index(name = "idx_aprobado", columnList = "aprobado"),
    @Index(name = "idx_activo", columnList = "activo"),
    @Index(name = "idx_nombre_apellido", columnList = "nombre,apellido")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferente")
    private Integer idOferente;

    @NotBlank(message = "La identificación es requerida")
    @Pattern(regexp = "^[0-9]{8,20}$", message = "La identificación debe tener entre 8 y 20 dígitos")
    @Column(name = "identificacion", nullable = false, unique = true, length = 50)
    private String identificacion;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Size(max = 50, message = "La nacionalidad no puede exceder 50 caracteres")
    @Column(name = "nacionalidad", length = 50)
    private String nacionalidad;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Size(max = 200, message = "La residencia no puede exceder 200 caracteres")
    @Column(name = "residencia", length = 200)
    private String residencia;

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

    @OneToMany(mappedBy = "oferente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OferenteCaracteristica> caracteristicas;

    @OneToOne(mappedBy = "oferente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Cv cv;

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
