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

    /**
     * Busca el CV de un oferente por su ID
     * @param idOferente ID del oferente
     * @return CV encontrado o vacío
     */
    Optional<Cv> findByOferente_IdOferente(Integer idOferente);

    /**
     * Verifica si un oferente tiene CV subido
     * @param idOferente ID del oferente
     * @return true si tiene CV, false en caso contrario
     */
    boolean existsByOferente_IdOferente(Integer idOferente);

    /**
     * Obtiene el total de CVs subidos
     * @return cantidad de CVs
     */
    long count();

    /**
     * Obtiene los CVs subidos recientemente (últimos días)
     * @param fecha fecha desde la cual buscar
     * @return lista de CVs recientes
     */
    @Query("SELECT c FROM Cv c WHERE c.fechaSubida >= :fecha ORDER BY c.fechaSubida DESC")
    List<Cv> findRecentCvs(@Param("fecha") LocalDateTime fecha);

    /**
     * Obtiene todos los CVs de oferentes aprobados
     * @return lista de CVs de oferentes aprobados
     */
    @Query("SELECT c FROM Cv c WHERE c.oferente.aprobado = true ORDER BY c.fechaSubida DESC")
    List<Cv> findCvsFromAprobados();

    /**
     * Obtiene todos los CVs ordenados por fecha descendente
     * @return lista de CVs ordenados
     */
    @Query("SELECT c FROM Cv c ORDER BY c.fechaSubida DESC")
    List<Cv> findAllOrderByFecha();

    /**
     * Busca un CV por nombre de archivo
     * @param nombreArchivo nombre del archivo
     * @return CV encontrado o vacío
     */
    Optional<Cv> findByNombreArchivo(String nombreArchivo);

    /**
     * Obtiene los CVs de una nacionalidad específica
     * @param nacionalidad nacionalidad a buscar
     * @return lista de CVs
     */
    @Query("SELECT c FROM Cv c WHERE c.oferente.nacionalidad = :nacionalidad ORDER BY c.fechaSubida DESC")
    List<Cv> findByNacionalidad(@Param("nacionalidad") String nacionalidad);

    /**
     * Obtiene el CV más reciente subido
     * @return CV más reciente o vacío
     */
    @Query("SELECT c FROM Cv c ORDER BY c.fechaSubida DESC LIMIT 1")
    Optional<Cv> findLatestCv();

    /**
     * Busca CVs que han sido actualizados recientemente
     * @param fecha fecha desde la cual buscar
     * @return lista de CVs actualizados
     */
    @Query("SELECT c FROM Cv c WHERE c.fechaActualizacion >= :fecha ORDER BY c.fechaActualizacion DESC")
    List<Cv> findRecentlyUpdated(@Param("fecha") LocalDateTime fecha);
}
