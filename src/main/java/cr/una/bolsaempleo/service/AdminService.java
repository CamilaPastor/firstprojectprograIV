package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Administrador;
import java.util.Optional;

public interface AdminService {

    /**
     * Login de administrador por identificación y contraseña
     * @param identificacion identificación del administrador
     * @param password contraseña sin hashear
     * @return administrador si credenciales son válidas, Optional vacío en caso contrario
     */
    Optional<Administrador> login(String identificacion, String password);

    /**
     * Obtiene un administrador por ID
     * @param idAdmin ID del administrador
     * @return administrador encontrado o Optional vacío
     */
    Optional<Administrador> findById(Integer idAdmin);
}
