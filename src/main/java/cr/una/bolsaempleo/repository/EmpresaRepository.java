package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    
    Optional<Empresa> findByCorreo(String correo);

    
    List<Empresa> findByAprobadoFalse();

    
    List<Empresa> findByAprobadoTrue();

    
    List<Empresa> findByAprobadoFalseAndActivoTrue();

    
    boolean existsByCorreo(String correo);

    
    long countByAprobadoTrue();

    
    long countByAprobadoFalse();

    
    List<Empresa> findByNombreContainingIgnoreCase(String nombre);

    
    @Query("SELECT e FROM Empresa e WHERE e.activo = true ORDER BY e.nombre ASC")
    List<Empresa> findAllActivas();
}
