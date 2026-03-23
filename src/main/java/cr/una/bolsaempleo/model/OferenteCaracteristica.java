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
@Table(name = "oferente_caracteristica", indexes = {
    @Index(name = "idx_id_oferente", columnList = "id_oferente"),
    @Index(name = "idx_id_caracteristica", columnList = "id_caracteristica"),
    @Index(name = "idx_nivel", columnList = "nivel"),
    @Index(name = "uk_oferente_caracteristica", columnList = "id_oferente,id_caracteristica", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OferenteCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oferente", nullable = false, foreignKey = @ForeignKey(name = "fk_oferente_caracteristica_oferente"))
    @ToString.Exclude
    private Oferente oferente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caracteristica", nullable = false, foreignKey = @ForeignKey(name = "fk_oferente_caracteristica_caracteristica"))
    @ToString.Exclude
    private Caracteristica caracteristica;

    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 5, message = "El nivel máximo es 5")
    @Column(name = "nivel", nullable = false, columnDefinition = "INT DEFAULT 3")
    private Integer nivel = 3;

    @Column(name = "fecha_creacion", nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (nivel == null) {
            nivel = 3;
        }
    }
}
