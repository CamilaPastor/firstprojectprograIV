package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CvRepository extends JpaRepository<Cv, Integer> {

    Optional<Cv> findByOferente_IdOferente(Integer idOferente);
}
