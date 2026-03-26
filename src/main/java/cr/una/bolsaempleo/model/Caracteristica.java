package cr.una.bolsaempleo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "caracteristica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caracteristica")
    private Integer idCaracteristica;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_padre")
    @ToString.Exclude
    private Caracteristica padre;

    @OneToMany(mappedBy = "padre", fetch = FetchType.EAGER)
    @OrderBy("nombre ASC")
    @ToString.Exclude
    private List<Caracteristica> hijos;

    @Column(name = "nivel_minimo", nullable = false)
    private Integer nivelMinimo = 1;

    @Column(name = "nivel_maximo", nullable = false)
    private Integer nivelMaximo = 5;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
        if (nivelMinimo == null) nivelMinimo = 1;
        if (nivelMaximo == null) nivelMaximo = 5;
        if (activo == null) activo = true;
    }
}
