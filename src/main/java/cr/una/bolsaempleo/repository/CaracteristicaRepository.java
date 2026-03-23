package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

    /**
     * Obtiene todas las características principales (sin padre)
     * @return lista de características raíz
     */
    List<Caracteristica> findByPadreIsNull();

    /**
     * Obtiene todas las subcategorías de una característica padre
     * @param idPadre ID de la característica padre
     * @return lista de subcategorías
     */
    List<Caracteristica> findByPadre_IdCaracteristica(Integer idPadre);

    /**
     * Busca una característica por nombre
     * @param nombre nombre de la característica
     * @return característica encontrada o vacío
     */
    Optional<Caracteristica> findByNombre(String nombre);

    /**
     * Busca características por nombre (búsqueda parcial)
     * @param nombre parte del nombre
     * @return lista de características que coinciden
     */
    List<Caracteristica> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Obtiene todas las características activas
     * @return lista de características activas
     */
    List<Caracteristica> findByActivoTrue();

    /**
     * Obtiene todas las categorías principales activas
     * @return lista de características raíz activas
     */
    @Query("SELECT c FROM Caracteristica c WHERE c.padre IS NULL AND c.activo = true ORDER BY c.nombre ASC")
    List<Caracteristica> findAllCategoriasPrincipales();

    /**
     * Obtiene el árbol completo de características (padre e hijos)
     * @return lista ordenada de características con jerarquía
     */
    @Query("SELECT c FROM Caracteristica c ORDER BY c.padre.idCaracteristica ASC, c.nombre ASC")
    List<Caracteristica> findHierarchy();

    /**
     * Obtiene subcategorías activas de un padre
     * @param idPadre ID del padre
     * @return lista de subcategorías activas
     */
    @Query("SELECT c FROM Caracteristica c WHERE c.padre.idCaracteristica = :idPadre AND c.activo = true ORDER BY c.nombre ASC")
    List<Caracteristica> findActiveHijos(@Param("idPadre") Integer idPadre);

    /**
     * Cuenta el número de subcategorías de una característica
     * @param idPadre ID del padre
     * @return cantidad de hijos
     */
    long countByPadre_IdCaracteristica(Integer idPadre);

    /**
     * Verifica si existe una característica con el nombre especificado
     * @param nombre nombre a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
