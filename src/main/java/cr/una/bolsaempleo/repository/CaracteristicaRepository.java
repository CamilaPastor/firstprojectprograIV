package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

    
    List<Caracteristica> findByPadreIsNull();

    
    List<Caracteristica> findByPadre_IdCaracteristica(Integer idPadre);

    
    Optional<Caracteristica> findByNombre(String nombre);

    
    List<Caracteristica> findByNombreContainingIgnoreCase(String nombre);

    
    List<Caracteristica> findByActivoTrue();

    
    @Query("SELECT c FROM Caracteristica c WHERE c.padre IS NULL AND c.activo = true ORDER BY c.nombre ASC")
    List<Caracteristica> findAllCategoriasPrincipales();

    
    @Query("SELECT c FROM Caracteristica c ORDER BY c.padre.idCaracteristica ASC, c.nombre ASC")
    List<Caracteristica> findHierarchy();

    
    @Query("SELECT c FROM Caracteristica c WHERE c.padre.idCaracteristica = :idPadre AND c.activo = true ORDER BY c.nombre ASC")
    List<Caracteristica> findActiveHijos(@Param("idPadre") Integer idPadre);

    
    long countByPadre_IdCaracteristica(Integer idPadre);

    
    boolean existsByNombre(String nombre);
}
