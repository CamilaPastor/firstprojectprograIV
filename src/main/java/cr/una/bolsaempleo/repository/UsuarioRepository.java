package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por cédula
     */
    Optional<Usuario> findByCedula(String cedula);

    /**
     * Busca usuarios activos por tipo
     */
    List<Usuario> findByTipoUsuarioAndActivoTrue(Usuario.TipoUsuario tipoUsuario);

    /**
     * Busca usuarios cuyo nombre contiene el texto especificado
     */
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca usuarios activos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    List<Usuario> findActivos();

    /**
     * Busca usuarios inactivos
     */
    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    List<Usuario> findInactivos();

    /**
     * Cuenta usuarios activos
     */
    long countByActivoTrue();

    /**
     * Verifica si existe un usuario con el email especificado
     */
    boolean existsByEmail(String email);

}
