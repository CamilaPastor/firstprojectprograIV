package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "puesto", indexes = {
    @Index(name = "idx_id_empresa", columnList = "id_empresa"),
    @Index(name = "idx_tipo_publicacion", columnList = "tipo_publicacion"),
    @Index(name = "idx_activo", columnList = "activo"),
    @Index(name = "idx_fecha_registro", columnList = "fecha_registro")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Integer idPuesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false, foreignKey = @ForeignKey(name = "fk_puesto_empresa"))
    @ToString.Exclude
    private Empresa empresa;

    @NotBlank(message = "La descripción es requerida")
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor a 0")
    @Column(name = "salario", precision = 15, scale = 2)
    private BigDecimal salario;

    @Column(name = "tipo_publicacion", nullable = false, length = 10, columnDefinition = "ENUM('publico', 'privado')")
    private String tipoPublicacion = "publico";

    @Column(name = "activo", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    @Column(name = "fecha_registro", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "puesto", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<PuestoCaracteristica> caracteristicasRequeridas;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (fechaActualizacion == null) {
            fechaActualizacion = LocalDateTime.now();
        }
        if (tipoPublicacion == null) {
            tipoPublicacion = "publico";
        }
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
