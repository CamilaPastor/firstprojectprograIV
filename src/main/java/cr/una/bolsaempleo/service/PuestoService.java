package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Puesto;
import java.util.List;
import java.util.Optional;

public interface PuestoService {

    Puesto publicar(Puesto puesto, List<Integer> idsCaracteristicas, List<Integer> nivelesRequeridos);

    List<Puesto> ultimos5Publicos();

    List<Puesto> puestosDeEmpresa(Integer idEmpresa);

    Optional<Puesto> findById(Integer idPuesto);

    void desactivar(Integer idPuesto);

    List<Puesto> todosPublicosActivos();

    List<Puesto> todosActivos();

    List<Puesto> puestosActivosPorEmpresa(Integer idEmpresa);
}
