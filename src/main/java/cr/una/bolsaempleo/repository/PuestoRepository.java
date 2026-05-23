package cr.una.bolsaempleo.repository;

import cr.una.bolsaempleo.model.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    List<Puesto> findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc(String tipoPublicacion);

    List<Puesto> findByEmpresa_IdEmpresa(Integer idEmpresa);

    List<Puesto> findByTipoPublicacionAndActivoTrue(String tipoPublicacion);

    @Query("SELECT p FROM Puesto p WHERE p.tipoPublicacion = 'publico' AND p.activo = true ORDER BY p.fechaRegistro DESC")
    List<Puesto> findPublicosActivos();

    @Query("SELECT p FROM Puesto p WHERE p.activo = true ORDER BY p.fechaRegistro DESC")
    List<Puesto> findActivos();

    List<Puesto> findByEmpresa_IdEmpresaAndActivoTrue(Integer idEmpresa);
}
