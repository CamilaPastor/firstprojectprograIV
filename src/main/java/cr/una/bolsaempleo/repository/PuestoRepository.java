package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    /**
     * Obtiene los 5 puestos más recientes de tipo público y activos
     * @return lista de 5 puestos ordenados por fecha descendente
     */
    List<Puesto> findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc(String tipoPublicacion);

    /**
     * Busca puestos por empresa
     * @param idEmpresa ID de la empresa
     * @return lista de puestos de esa empresa
     */
    List<Puesto> findByEmpresa_IdEmpresa(Integer idEmpresa);

    /**
     * Busca puestos públicos o privados que estén activos
     * @param tipoPublicacion tipo de publicación
     * @return lista de puestos del tipo especificado activos
     */
    List<Puesto> findByTipoPublicacionAndActivoTrue(String tipoPublicacion);

    /**
     * Obtiene todos los puestos públicos activos ordenados por fecha descendente
     * @return lista de puestos públicos
     */
    @Query("SELECT p FROM Puesto p WHERE p.tipoPublicacion = 'publico' AND p.activo = true ORDER BY p.fechaRegistro DESC")
    List<Puesto> findPublicosActivos();

    /**
     * Obtiene todos los puestos privados de una empresa
     * @param idEmpresa ID de la empresa
     * @return lista de puestos privados
     */
    @Query("SELECT p FROM Puesto p WHERE p.empresa.idEmpresa = :idEmpresa AND p.tipoPublicacion = 'privado'")
    List<Puesto> findPrivadosByEmpresa(@Param("idEmpresa") Integer idEmpresa);

    /**
     * Obtiene todos los puestos activos de una empresa
     * @param idEmpresa ID de la empresa
     * @return lista de puestos activos
     */
    List<Puesto> findByEmpresa_IdEmpresaAndActivoTrue(Integer idEmpresa);

    /**
     * Cuenta el número de puestos activos
     * @return cantidad de puestos activos
     */
    long countByActivoTrue();

    /**
     * Cuenta el número de puestos públicos activos
     * @return cantidad de puestos públicos
     */
    long countByTipoPublicacionAndActivoTrue(String tipoPublicacion);

    /**
     * Busca puestos que tengan salario en un rango especificado
     * @param salarioMin salario mínimo
     * @param salarioMax salario máximo
     * @return lista de puestos en ese rango
     */
    @Query("SELECT p FROM Puesto p WHERE p.salario >= :salarioMin AND p.salario <= :salarioMax AND p.activo = true")
    List<Puesto> findByRangoSalario(@Param("salarioMin") java.math.BigDecimal salarioMin, 
                                     @Param("salarioMax") java.math.BigDecimal salarioMax);

    /**
     * Busca puestos por palabra clave en descripción
     * @param keyword palabra a buscar
     * @return lista de puestos que contienen la palabra
     */
    @Query("SELECT p FROM Puesto p WHERE (p.descripcion LIKE CONCAT('%', :keyword, '%') OR p.empresa.nombre LIKE CONCAT('%', :keyword, '%')) AND p.activo = true")
    List<Puesto> buscarPorKeyword(@Param("keyword") String keyword);
}
