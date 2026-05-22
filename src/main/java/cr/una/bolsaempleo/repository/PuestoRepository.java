package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    
    List<Puesto> findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc(String tipoPublicacion);

    
    List<Puesto> findByEmpresa_IdEmpresa(Integer idEmpresa);

    
    List<Puesto> findByTipoPublicacionAndActivoTrue(String tipoPublicacion);

    
    @Query("SELECT p FROM Puesto p WHERE p.tipoPublicacion = 'publico' AND p.activo = true ORDER BY p.fechaRegistro DESC")
    List<Puesto> findPublicosActivos();

    @Query("SELECT p FROM Puesto p WHERE p.activo = true ORDER BY p.fechaRegistro DESC")
    List<Puesto> findActivos();

    
    @Query("SELECT p FROM Puesto p WHERE p.empresa.idEmpresa = :idEmpresa AND p.tipoPublicacion = 'privado'")
    List<Puesto> findPrivadosByEmpresa(@Param("idEmpresa") Integer idEmpresa);

    
    List<Puesto> findByEmpresa_IdEmpresaAndActivoTrue(Integer idEmpresa);

    
    long countByActivoTrue();

    
    long countByTipoPublicacionAndActivoTrue(String tipoPublicacion);

    
    @Query("SELECT p FROM Puesto p WHERE p.salario >= :salarioMin AND p.salario <= :salarioMax AND p.activo = true")
    List<Puesto> findByRangoSalario(@Param("salarioMin") java.math.BigDecimal salarioMin, 
                                     @Param("salarioMax") java.math.BigDecimal salarioMax);

    
    @Query("SELECT p FROM Puesto p WHERE (p.descripcion LIKE CONCAT('%', :keyword, '%') OR p.empresa.nombre LIKE CONCAT('%', :keyword, '%')) AND p.activo = true")
    List<Puesto> buscarPorKeyword(@Param("keyword") String keyword);
}
