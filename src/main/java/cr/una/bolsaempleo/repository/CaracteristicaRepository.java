package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {

    List<Caracteristica> findByPadreIsNull();

    List<Caracteristica> findByPadre_IdCaracteristica(Integer idPadre);

    Optional<Caracteristica> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
