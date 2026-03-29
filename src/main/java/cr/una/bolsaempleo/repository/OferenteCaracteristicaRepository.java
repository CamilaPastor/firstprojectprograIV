package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.OferenteCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OferenteCaracteristicaRepository extends JpaRepository<OferenteCaracteristica, Integer> {

    
    List<OferenteCaracteristica> findByOferente_IdOferente(Integer idOferente);

    
    List<OferenteCaracteristica> findByCaracteristica_IdCaracteristica(Integer idCaracteristica);

    
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente AND oc.caracteristica.idCaracteristica = :idCaracteristica")
    OferenteCaracteristica findByOferenteAndCaracteristica(@Param("idOferente") Integer idOferente, 
                                                           @Param("idCaracteristica") Integer idCaracteristica);

    
    long countByOferente_IdOferente(Integer idOferente);

    
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente AND oc.nivel >= :nivelMinimo")
    List<OferenteCaracteristica> findByNivelMinimo(@Param("idOferente") Integer idOferente, 
                                                   @Param("nivelMinimo") Integer nivelMinimo);

    
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.oferente.idOferente = :idOferente ORDER BY oc.nivel DESC")
    List<OferenteCaracteristica> findByOferenteOrderedByNivel(@Param("idOferente") Integer idOferente);

    
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.caracteristica.idCaracteristica = :idCaracteristica AND oc.nivel >= :nivelMinimo")
    List<OferenteCaracteristica> findOferentesWithMinNivel(@Param("idCaracteristica") Integer idCaracteristica, 
                                                           @Param("nivelMinimo") Integer nivelMinimo);

    
    @Query("SELECT oc FROM OferenteCaracteristica oc WHERE oc.caracteristica.idCaracteristica = :idCaracteristica AND oc.oferente.aprobado = true")
    List<OferenteCaracteristica> findAprobadosWithCaracteristica(@Param("idCaracteristica") Integer idCaracteristica);

    
    boolean existsByOferente_IdOferenteAndCaracteristica_IdCaracteristica(Integer idOferente, Integer idCaracteristica);

    
    long countByCaracteristica_IdCaracteristica(Integer idCaracteristica);
}
