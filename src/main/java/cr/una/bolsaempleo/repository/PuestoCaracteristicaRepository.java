package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Integer> {

    /**
     * Obtiene todas las características requeridas para un puesto
     * @param idPuesto ID del puesto
     * @return lista de características requeridas
     */
    List<PuestoCaracteristica> findByPuesto_IdPuesto(Integer idPuesto);

    /**
     * Obtiene todas las características requeridas para una característica
     * @param idCaracteristica ID de la característica
     * @return lista de puestos que requieren esa característica
     */
    List<PuestoCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica);

    /**
     * Busca una característica específica requerida en un puesto
     * @param idPuesto ID del puesto
     * @param idCaracteristica ID de la característica
     * @return característica requerida o vacío
     */
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto AND pc.caracteristica.idCaracteristica = :idCaracteristica")
    PuestoCaracteristica findByPuestoAndCaracteristica(@Param("idPuesto") Integer idPuesto, 
                                                       @Param("idCaracteristica") Integer idCaracteristica);

    /**
     * Cuenta el número de características requeridas para un puesto
     * @param idPuesto ID del puesto
     * @return cantidad de características requeridas
     */
    long countByPuesto_IdPuesto(Integer idPuesto);

    /**
     * Obtiene las características con nivel requerido mínimo especificado
     * @param idPuesto ID del puesto
     * @param nivelMinimo nivel mínimo requerido
     * @return lista de características con ese nivel o superior
     */
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto AND pc.nivelRequerido >= :nivelMinimo")
    List<PuestoCaracteristica> findByNivelRequerido(@Param("idPuesto") Integer idPuesto, 
                                                    @Param("nivelMinimo") Integer nivelMinimo);

    /**
     * Obtiene todas las características requeridas ordenadas por nivel descendente
     * @param idPuesto ID del puesto
     * @return lista de características ordenadas por nivel
     */
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto ORDER BY pc.nivelRequerido DESC")
    List<PuestoCaracteristica> findByPuestoOrderedByNivel(@Param("idPuesto") Integer idPuesto);

    /**
     * Busca puestos que requieren una característica específica con un nivel mínimo
     * @param idCaracteristica ID de la característica
     * @param nivelMinimo nivel mínimo requerido
     * @return lista de puestos que requieren esa característica
     */
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.caracteristica.idCaracteristica = :idCaracteristica AND pc.nivelRequerido >= :nivelMinimo")
    List<PuestoCaracteristica> findPuestosWithMinNivel(@Param("idCaracteristica") Integer idCaracteristica, 
                                                       @Param("nivelMinimo") Integer nivelMinimo);

    /**
     * Verifica si una característica es requerida para un puesto
     * @param idPuesto ID del puesto
     * @param idCaracteristica ID de la característica
     * @return true si es requerida, false en caso contrario
     */
    boolean existsByPuesto_IdPuestoAndCaracteristica_IdCaracteristica(Integer idPuesto, Integer idCaracteristica);
}
