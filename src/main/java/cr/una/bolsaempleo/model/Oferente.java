package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "oferente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oferente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oferente")
    private Integer idOferente;

    @Column(name = "identificacion", nullable = false, unique = true, length = 50)
    private String identificacion;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "nacionalidad", length = 50)
    private String nacionalidad;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name = "residencia", length = 200)
    private String residencia;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "aprobado", nullable = false)
    private Boolean aprobado = false;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "oferente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OferenteCaracteristica> caracteristicas;

    @OneToOne(mappedBy = "oferente", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Cv cv;

    @Transient
    private String passwordSinHashear;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) fechaRegistro = LocalDateTime.now();
        if (aprobado == null) aprobado = false;
        if (activo == null) activo = true;
    }
}
