package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface CvRepository extends JpaRepository<Cv, Integer> {

    
    Optional<Cv> findByOferente_IdOferente(Integer idOferente);

    
    boolean existsByOferente_IdOferente(Integer idOferente);

    
    long count();

    
    @Query("SELECT c FROM Cv c WHERE c.fechaSubida >= :fecha ORDER BY c.fechaSubida DESC")
    List<Cv> findRecentCvs(@Param("fecha") LocalDateTime fecha);

    
    @Query("SELECT c FROM Cv c WHERE c.oferente.aprobado = true ORDER BY c.fechaSubida DESC")
    List<Cv> findCvsFromAprobados();

    
    @Query("SELECT c FROM Cv c ORDER BY c.fechaSubida DESC")
    List<Cv> findAllOrderByFecha();

    
    Optional<Cv> findByNombreArchivo(String nombreArchivo);

    
    @Query("SELECT c FROM Cv c WHERE c.oferente.nacionalidad = :nacionalidad ORDER BY c.fechaSubida DESC")
    List<Cv> findByNacionalidad(@Param("nacionalidad") String nacionalidad);

    
    @Query("SELECT c FROM Cv c ORDER BY c.fechaSubida DESC LIMIT 1")
    Optional<Cv> findLatestCv();

    
    @Query("SELECT c FROM Cv c WHERE c.fechaActualizacion >= :fecha ORDER BY c.fechaActualizacion DESC")
    List<Cv> findRecentlyUpdated(@Param("fecha") LocalDateTime fecha);
}
