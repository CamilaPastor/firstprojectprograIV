package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Integer> {

    List<PuestoCaracteristica> findByPuesto_IdPuesto(Integer idPuesto);
}
