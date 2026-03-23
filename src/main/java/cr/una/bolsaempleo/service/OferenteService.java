package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.Cv;
import java.util.List;
import java.util.Optional;

public interface OferenteService {

    /**
     * Registra un nuevo oferente con contraseña hasheada
     * @param oferente oferente a registrar (con passwordSinHashear)
     * @return oferente registrado con ID generado
     */
    Oferente registrar(Oferente oferente);

    /**
     * Login de oferente por correo y contraseña
     * @param correo correo del oferente
     * @param password contraseña sin hashear
     * @return oferente si credenciales son válidas, Optional vacío en caso contrario
     */
    Optional<Oferente> loginPorCorreo(String correo, String password);

    /**
     * Obtiene todos los oferentes pendientes de aprobación
     * @return lista de oferentes no aprobados
     */
    List<Oferente> pendientes();

    /**
     * Aprueba un oferente
     * @param idOferente ID del oferente a aprobar
     * @return oferente aprobado
     */
    Oferente aprobar(Integer idOferente);

    /**
     * Busca un oferente por ID
     * @param idOferente ID a buscar
     * @return oferente encontrado o Optional vacío
     */
    Optional<Oferente> findById(Integer idOferente);

    /**
     * Obtiene todas las habilidades (características) de un oferente
     * @param idOferente ID del oferente
     * @return lista de características del oferente
     */
    Object habilidades(Integer idOferente);

    /**
     * Agrega una habilidad (característica) a un oferente
     * @param idOferente ID del oferente
     * @param idCaracteristica ID de la característica
     * @param nivel nivel de dominio (1-5)
     * @return objeto OferenteCaracteristica creado
     */
    Object agregarHabilidad(Integer idOferente, Integer idCaracteristica, Integer nivel);

    /**
     * Elimina una habilidad de un oferente
     * @param idOferente ID del oferente
     * @param idCaracteristica ID de la característica
     */
    void eliminarHabilidad(Integer idOferente, Integer idCaracteristica);

    /**
     * Sube o actualiza el CV de un oferente
     * @param idOferente ID del oferente
     * @param archivoPdf bytes del PDF
     * @param nombreArchivo nombre del archivo
     * @return CV subido
     */
    Cv subirCv(Integer idOferente, byte[] archivoPdf, String nombreArchivo);

    /**
     * Obtiene el CV de un oferente
     * @param idOferente ID del oferente
     * @return CV encontrado o Optional vacío
     */
    Optional<Cv> obtenerCv(Integer idOferente);

    /**
     * Obtiene todos los oferentes aprobados y activos
     * @return lista de oferentes activos
     */
    List<Oferente> obtenerActivos();

    /**
     * Actualiza datos de un oferente
     * @param oferente oferente con datos actualizados
     * @return oferente actualizado
     */
    Oferente actualizar(Oferente oferente);

    /**
     * Desactiva un oferente
     * @param idOferente ID del oferente a desactivar
     */
    void desactivar(Integer idOferente);
}
