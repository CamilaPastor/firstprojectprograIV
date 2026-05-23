package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    Optional<Empresa> findByCorreo(String correo);

    List<Empresa> findByAprobadoFalse();

    boolean existsByCorreo(String correo);
}
