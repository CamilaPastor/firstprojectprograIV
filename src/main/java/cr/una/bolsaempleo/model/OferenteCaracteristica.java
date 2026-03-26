package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferente_caracteristica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OferenteCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_oferente", nullable = false)
    @ToString.Exclude
    private Oferente oferente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_caracteristica", nullable = false)
    @ToString.Exclude
    private Caracteristica caracteristica;

    @Column(name = "nivel", nullable = false)
    private Integer nivel = 3;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (nivel == null) nivel = 3;
    }
}
