package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OferenteRepository extends JpaRepository<Oferente, Integer> {

    /**
     * Busca un oferente por su correo electrónico
     * @param correo correo único del oferente
     * @return oferente encontrado o vacío
     */
    Optional<Oferente> findByCorreo(String correo);

    /**
     * Obtiene todos los oferentes que no han sido aprobados
     * @return lista de oferentes pendientes de aprobación
     */
    List<Oferente> findByAprobadoFalse();

    /**
     * Obtiene todos los oferentes aprobados
     * @return lista de oferentes aprobados
     */
    List<Oferente> findByAprobadoTrue();

    /**
     * Busca oferentes pendientes de aprobación y activos
     * @return lista de oferentes pendientes activos
     */
    List<Oferente> findByAprobadoFalseAndActivoTrue();

    /**
     * Busca un oferente por su identificación (cédula)
     * @param identificacion identificación del oferente
     * @return oferente encontrado o vacío
     */
    Optional<Oferente> findByIdentificacion(String identificacion);

    /**
     * Verifica si existe un oferente con el correo especificado
     * @param correo correo a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorreo(String correo);

    /**
     * Verifica si existe un oferente con la identificación especificada
     * @param identificacion identificación a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByIdentificacion(String identificacion);

    /**
     * Obtiene el conteo de oferentes aprobados
     * @return cantidad de oferentes aprobados
     */
    long countByAprobadoTrue();

    /**
     * Obtiene el conteo de oferentes pendientes de aprobación
     * @return cantidad de oferentes sin aprobar
     */
    long countByAprobadoFalse();

    /**
     * Busca oferentes por nombre o apellido (búsqueda parcial)
     * @param nombre parte del nombre
     * @return lista de oferentes que coinciden
     */
    List<Oferente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

    /**
     * Obtiene todos los oferentes activos
     * @return lista de oferentes activos
     */
    @Query("SELECT o FROM Oferente o WHERE o.activo = true ORDER BY o.nombre, o.apellido ASC")
    List<Oferente> findAllActivos();

    /**
     * Busca oferentes por nacionalidad
     * @param nacionalidad nacionalidad a buscar
     * @return lista de oferentes de esa nacionalidad
     */
    List<Oferente> findByNacionalidad(String nacionalidad);

    /**
     * Busca oferentes aprobados que tengan CV
     * @return lista de oferentes aprobados con CV
     */
    @Query("SELECT o FROM Oferente o WHERE o.aprobado = true AND o.cv IS NOT NULL")
    List<Oferente> findAprobadosConCv();
}
