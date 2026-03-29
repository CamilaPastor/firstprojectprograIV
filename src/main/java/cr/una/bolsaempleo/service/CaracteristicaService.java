package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Caracteristica;
import java.util.List;
import java.util.Optional;

public interface CaracteristicaService {

    
    List<Caracteristica> raices();

    
    List<Caracteristica> hijos(Integer idPadre);

    
    List<Caracteristica> todas();

    
    Optional<Caracteristica> findById(Integer idCaracteristica);

    
    Caracteristica crear(String nombre, Integer idPadre);

    
    Caracteristica crear(String nombre, String descripcion, Integer idPadre);

    
    List<Caracteristica> obtenerHierarchy();

    
    List<Caracteristica> hijosActivos(Integer idPadre);

    
    void eliminar(Integer idCaracteristica);
}
