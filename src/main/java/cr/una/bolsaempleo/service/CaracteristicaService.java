package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Caracteristica;
import java.util.List;
import java.util.Optional;

public interface CaracteristicaService {

    /**
     * Obtiene todas las características raíz (sin padre)
     * @return lista de categorías principales
     */
    List<Caracteristica> raices();

    /**
     * Obtiene todas las subcategorías de una característica padre
     * @param idPadre ID de la característica padre
     * @return lista de hijos
     */
    List<Caracteristica> hijos(Integer idPadre);

    /**
     * Obtiene todas las características
     * @return lista completa de características
     */
    List<Caracteristica> todas();

    /**
     * Busca una característica por ID
     * @param idCaracteristica ID a buscar
     * @return característica encontrada o Optional vacío
     */
    Optional<Caracteristica> findById(Integer idCaracteristica);

    /**
     * Crea una nueva característica (puede ser raíz o subcategoría)
     * @param nombre nombre de la característica
     * @param idPadre ID del padre (null si es categoría raíz)
     * @return característica creada
     */
    Caracteristica crear(String nombre, Integer idPadre);

    /**
     * Crea una nueva característica con descripción
     * @param nombre nombre de la característica
     * @param descripcion descripción de la característica
     * @param idPadre ID del padre (null si es categoría raíz)
     * @return característica creada
     */
    Caracteristica crear(String nombre, String descripcion, Integer idPadre);

    /**
     * Obtiene el árbol completo de características
     * @return lista ordenada de características con jerarquía
     */
    List<Caracteristica> obtenerHierarchy();

    /**
     * Obtiene las subcategorías activas de un padre
     * @param idPadre ID del padre
     * @return lista de subcategorías activas
     */
    List<Caracteristica> hijosActivos(Integer idPadre);
}
