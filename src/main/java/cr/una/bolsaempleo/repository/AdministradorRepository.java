package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    /**
     * Busca un administrador por su identificación
     * @param identificacion identificación única del administrador
     * @return administrador encontrado o vacío
     */
    Optional<Administrador> findByIdentificacion(String identificacion);

    /**
     * Verifica si existe un administrador con la identificación especificada
     * @param identificacion identificación a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByIdentificacion(String identificacion);

    /**
     * Cuenta el número de administradores activos
     * @return cantidad de administradores activos
     */
    long countByActivoTrue();
}
