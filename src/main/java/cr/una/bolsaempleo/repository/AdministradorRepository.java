package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    Optional<Administrador> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);
}
