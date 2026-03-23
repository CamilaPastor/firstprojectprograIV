package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaService {

    /**
     * Registra una nueva empresa con contraseña hasheada
     * @param empresa empresa a registrar (con passwordSinHashear)
     * @return empresa registrada con ID generado
     */
    Empresa registrar(Empresa empresa);

    /**
     * Login de empresa por correo y contraseña
     * @param correo correo de la empresa
     * @param password contraseña sin hashear
     * @return empresa si credenciales son válidas, Optional vacío en caso contrario
     */
    Optional<Empresa> loginPorCorreo(String correo, String password);

    /**
     * Obtiene todas las empresas pendientes de aprobación
     * @return lista de empresas no aprobadas
     */
    List<Empresa> pendientes();

    /**
     * Aprueba una empresa
     * @param idEmpresa ID de la empresa a aprobar
     * @return empresa aprobada
     */
    Empresa aprobar(Integer idEmpresa);

    /**
     * Busca una empresa por ID
     * @param idEmpresa ID a buscar
     * @return empresa encontrada o Optional vacío
     */
    Optional<Empresa> findById(Integer idEmpresa);

    /**
     * Obtiene todas las empresas aprobadas y activas
     * @return lista de empresas activas
     */
    List<Empresa> obtenerActivas();

    /**
     * Actualiza datos de una empresa
     * @param empresa empresa con datos actualizados
     * @return empresa actualizada
     */
    Empresa actualizar(Empresa empresa);

    /**
     * Desactiva una empresa
     * @param idEmpresa ID de la empresa a desactivar
     */
    void desactivar(Integer idEmpresa);
}
