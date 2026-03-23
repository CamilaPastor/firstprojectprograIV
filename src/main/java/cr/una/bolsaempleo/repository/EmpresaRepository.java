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

    /**
     * Busca una empresa por su correo electrónico
     * @param correo correo único de la empresa
     * @return empresa encontrada o vacío
     */
    Optional<Empresa> findByCorreo(String correo);

    /**
     * Obtiene todas las empresas que no han sido aprobadas
     * @return lista de empresas pendientes de aprobación
     */
    List<Empresa> findByAprobadoFalse();

    /**
     * Obtiene todas las empresas aprobadas
     * @return lista de empresas aprobadas
     */
    List<Empresa> findByAprobadoTrue();

    /**
     * Busca empresas pendientes de aprobación y activas
     * @return lista de empresas pendientes activas
     */
    List<Empresa> findByAprobadoFalseAndActivoTrue();

    /**
     * Verifica si existe una empresa con el correo especificado
     * @param correo correo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorreo(String correo);

    /**
     * Obtiene el conteo de empresas aprobadas
     * @return cantidad de empresas aprobadas
     */
    long countByAprobadoTrue();

    /**
     * Obtiene el conteo de empresas pendientes de aprobación
     * @return cantidad de empresas sin aprobar
     */
    long countByAprobadoFalse();

    /**
     * Busca empresas por nombre (búsqueda parcial)
     * @param nombre parte del nombre de la empresa
     * @return lista de empresas que coinciden
     */
    List<Empresa> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Obtiene todas las empresas activas
     * @return lista de empresas activas
     */
    @Query("SELECT e FROM Empresa e WHERE e.activo = true ORDER BY e.nombre ASC")
    List<Empresa> findAllActivas();
}
