package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "puesto_caracteristica", indexes = {
    @Index(name = "idx_id_puesto", columnList = "id_puesto"),
    @Index(name = "idx_id_caracteristica", columnList = "id_caracteristica"),
    @Index(name = "idx_nivel_requerido", columnList = "nivel_requerido"),
    @Index(name = "uk_puesto_caracteristica", columnList = "id_puesto,id_caracteristica", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuestoCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto", nullable = false, foreignKey = @ForeignKey(name = "fk_puesto_caracteristica_puesto"))
    @ToString.Exclude
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caracteristica", nullable = false, foreignKey = @ForeignKey(name = "fk_puesto_caracteristica_caracteristica"))
    @ToString.Exclude
    private Caracteristica caracteristica;

    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 5, message = "El nivel máximo es 5")
    @Column(name = "nivel_requerido", nullable = false, columnDefinition = "INT DEFAULT 3")
    private Integer nivelRequerido = 3;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (nivelRequerido == null) {
            nivelRequerido = 3;
        }
    }
}
