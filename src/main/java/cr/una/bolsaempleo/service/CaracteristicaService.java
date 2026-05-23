package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Caracteristica;
import java.util.List;

public interface CaracteristicaService {

    List<Caracteristica> raices();

    List<Caracteristica> hijos(Integer idPadre);

    List<Caracteristica> todas();

    Caracteristica crear(String nombre, String descripcion, Integer idPadre);

    void eliminar(Integer idCaracteristica);
}
