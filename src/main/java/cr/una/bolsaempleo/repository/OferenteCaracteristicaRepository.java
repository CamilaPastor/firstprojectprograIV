package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.OferenteCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OferenteCaracteristicaRepository extends JpaRepository<OferenteCaracteristica, Integer> {

    /**
     * Obtiene todas las características de un oferente
     * @param idOferente ID del oferente
     * @return lista de características del oferente
     */
    List<OferenteCaracteristica> findByOferente_IdOferente(Integer idOferente);

    /**
     * Obtiene todos los oferentes que tienen una característica
     * @param idCaracteristica ID de la característica
     * @return lista de oferentes con esa característica
     */
    List<OferenteCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica);

    /**
     * Busca una característica específica de un oferente
     * @param idOferente ID del oferente
     * @param idCaracteristica ID de la característica
     * @return característica del oferente o vacío
     */
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente AND oc.caracteristica.idCaracteristica = :idCaracteristica")
    OferenteCaracteristica findByOferenteAndCaracteristica(@Param("idOferente") Integer idOferente, 
                                                           @Param("idCaracteristica") Integer idCaracteristica);

    /**
     * Cuenta el número de características de un oferente
     * @param idOferente ID del oferente
     * @return cantidad de características
     */
    long countByOferente_IdOferente(Integer idOferente);

    /**
     * Obtiene las características de un oferente con nivel mínimo especificado
     * @param idOferente ID del oferente
     * @param nivelMinimo nivel mínimo
     * @return lista de características con ese nivel o superior
     */
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente AND oc.nivel >= :nivelMinimo")
    List<OferenteCaracteristica> findByNivelMinimo(@Param("idOferente") Integer idOferente, 
                                                   @Param("nivelMinimo") Integer nivelMinimo);

    /**
     * Obtiene las características de un oferente ordenadas por nivel descendente
     * @param idOferente ID del oferente
     * @return lista de características ordenadas por nivel
     */
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente ORDER BY oc.nivel DESC")
    List<OferenteCaracteristica> findByOferenteOrderedByNivel(@Param("idOferente") Integer idOferente);

    /**
     * Obtiene los oferentes que tienen una característica con un nivel mínimo
     * @param idCaracteristica ID de la característica
     * @param nivelMinimo nivel mínimo
     * @return lista de oferentes con esa característica en ese nivel
     */
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.caracteristica.idCaracteristica = :idCaracteristica AND oc.nivel >= :nivelMinimo")
    List<OferenteCaracteristica> findOferentesWithMinNivel(@Param("idCaracteristica") Integer idCaracteristica, 
                                                           @Param("nivelMinimo") Integer nivelMinimo);

    /**
     * Busca oferentes aprobados que tienen una característica específica
     * @param idCaracteristica ID de la característica
     * @return lista de oferentes aprobados con esa característica
     */
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.caracteristica.idCaracteristica = :idCaracteristica AND oc.oferente.aprobado = true")
    List<OferenteCaracteristica> findAprobadosWithCaracteristica(@Param("idCaracteristica") Integer idCaracteristica);

    /**
     * Verifica si un oferente tiene una característica específica
     * @param idOferente ID del oferente
     * @param idCaracteristica ID de la característica
     * @return true si la tiene, false en caso contrario
     */
    boolean existsByOferente_IdOferenteAndCaracteristica_IdCaracteristica(Integer idOferente, Integer idCaracteristica);

    /**
     * Obtiene el total de oferentes con una característica
     * @param idCaracteristica ID de la característica
     * @return cantidad de oferentes
     */
    long countByCaracteristica_IdCaracteristica(Integer idCaracteristica);
}
