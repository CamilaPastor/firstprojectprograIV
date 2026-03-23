package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Puesto;
import java.util.List;
import java.util.Optional;

public interface PuestoService {

    /**
     * Publica un nuevo puesto de empleo con sus características requeridas
     * @param puesto puesto a publicar
     * @param idsCaracteristicas lista de IDs de características requeridas
     * @param nivelesRequeridos lista de niveles requeridos (mismo orden que idsCaracteristicas)
     * @return puesto publicado
     */
    Puesto publicar(Puesto puesto, List<Integer> idsCaracteristicas, List<Integer> nivelesRequeridos);

    /**
     * Obtiene los 5 puestos públicos más recientes activos
     * @return lista de hasta 5 puestos
     */
    List<Puesto> ultimos5Publicos();

    /**
     * Obtiene todos los puestos de una empresa
     * @param idEmpresa ID de la empresa
     * @return lista de puestos de esa empresa
     */
    List<Puesto> puestosDeEmpresa(Integer idEmpresa);

    /**
     * Busca un puesto por ID
     * @param idPuesto ID a buscar
     * @return puesto encontrado o Optional vacío
     */
    Optional<Puesto> findById(Integer idPuesto);

    /**
     * Desactiva un puesto de empleo
     * @param idPuesto ID del puesto a desactivar
     */
    void desactivar(Integer idPuesto);

    /**
     * Obtiene todos los puestos públicos activos
     * @return lista de puestos públicos
     */
    List<Puesto> todosPublicosActivos();

    /**
     * Obtiene los puestos activos de una empresa
     * @param idEmpresa ID de la empresa
     * @return lista de puestos activos
     */
    List<Puesto> puestosActivosPorEmpresa(Integer idEmpresa);

    /**
     * Actualiza un puesto existente
     * @param puesto puesto con datos actualizados
     * @return puesto actualizado
     */
    Puesto actualizar(Puesto puesto);
}
