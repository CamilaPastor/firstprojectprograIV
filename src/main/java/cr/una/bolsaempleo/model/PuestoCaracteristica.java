package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "puesto_caracteristica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuestoCaracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_puesto", nullable = false)
    @ToString.Exclude
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_caracteristica", nullable = false)
    @ToString.Exclude
    private Caracteristica caracteristica;

    @Column(name = "nivel_requerido", nullable = false)
    private Integer nivelRequerido = 3;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (nivelRequerido == null) nivelRequerido = 3;
    }
}
