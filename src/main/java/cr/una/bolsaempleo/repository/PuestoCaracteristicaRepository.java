package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Integer> {

    
    List<PuestoCaracteristica> findByPuesto_IdPuesto(Integer idPuesto);

    
    List<PuestoCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica);

    
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto AND pc.caracteristica.idCaracteristica = :idCaracteristica")
    PuestoCaracteristica findByPuestoAndCaracteristica(@Param("idPuesto") Integer idPuesto, 
                                                       @Param("idCaracteristica") Integer idCaracteristica);

    
    long countByPuesto_IdPuesto(Integer idPuesto);

    
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto AND pc.nivelRequerido >= :nivelMinimo")
    List<PuestoCaracteristica> findByNivelRequerido(@Param("idPuesto") Integer idPuesto, 
                                                    @Param("nivelMinimo") Integer nivelMinimo);

    
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.puesto.idPuesto = :idPuesto ORDER BY pc.nivelRequerido DESC")
    List<PuestoCaracteristica> findByPuestoOrderedByNivel(@Param("idPuesto") Integer idPuesto);

    
    @Query("SELECT pc FROM PuestoCaracteristica pc WHERE pc.caracteristica.idCaracteristica = :idCaracteristica AND pc.nivelRequerido >= :nivelMinimo")
    List<PuestoCaracteristica> findPuestosWithMinNivel(@Param("idCaracteristica") Integer idCaracteristica, 
                                                       @Param("nivelMinimo") Integer nivelMinimo);

    
    boolean existsByPuesto_IdPuestoAndCaracteristica_IdCaracteristica(Integer idPuesto, Integer idCaracteristica);
}
