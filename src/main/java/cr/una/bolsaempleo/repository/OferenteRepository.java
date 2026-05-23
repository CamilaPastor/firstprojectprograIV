package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OferenteRepository extends JpaRepository<Oferente, Integer> {

    Optional<Oferente> findByCorreo(String correo);

    List<Oferente> findByAprobadoFalse();

    Optional<Oferente> findByIdentificacion(String identificacion);

    boolean existsByCorreo(String correo);

    boolean existsByIdentificacion(String identificacion);

    @Query("SELECT o FROM Oferente o WHERE o.activo = true AND o.aprobado = true ORDER BY o.nombre, o.apellido ASC")
    List<Oferente> findAllActivos();
}
