package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "caracteristica", indexes = {
    @Index(name = "idx_nombre", columnList = "nombre"),
    @Index(name = "idx_id_padre", columnList = "id_padre"),
    @Index(name = "idx_activo", columnList = "activo")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caracteristica")
    private Integer idCaracteristica;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_padre", foreignKey = @ForeignKey(name = "fk_caracteristica_padre"))
    @ToString.Exclude
    private Caracteristica padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Caracteristica> hijos;

    @Column(name = "nivel_minimo", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer nivelMinimo = 1;

    @Column(name = "nivel_maximo", nullable = false, columnDefinition = "INT DEFAULT 5")
    private Integer nivelMaximo = 5;

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "caracteristica", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<PuestoCaracteristica> puestosRequeridos;

    @OneToMany(mappedBy = "caracteristica", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OferenteCaracteristica> oferentesConCaracteristica;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (nivelMinimo == null) {
            nivelMinimo = 1;
        }
        if (nivelMaximo == null) {
            nivelMaximo = 5;
        }
        if (activo == null) {
            activo = true;
        }
    }
}
