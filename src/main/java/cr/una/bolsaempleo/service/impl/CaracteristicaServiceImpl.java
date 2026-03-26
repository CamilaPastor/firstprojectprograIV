package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.repository.CaracteristicaRepository;
import cr.una.bolsaempleo.service.CaracteristicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CaracteristicaServiceImpl implements CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> raices() {
        return caracteristicaRepository.findByPadreIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> hijos(Integer idPadre) {
        return caracteristicaRepository.findByPadre_IdCaracteristica(idPadre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> todas() {
        return caracteristicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Caracteristica> findById(Integer idCaracteristica) {
        return caracteristicaRepository.findById(idCaracteristica);
    }

    @Override
    public Caracteristica crear(String nombre, Integer idPadre) {
        return crear(nombre, null, idPadre);
    }

    @Override
    public Caracteristica crear(String nombre, String descripcion, Integer idPadre) {
        if (caracteristicaRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una característica con este nombre");
        }

        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNombre(nombre);
        caracteristica.setDescripcion(descripcion);
        caracteristica.setActivo(true);

        if (idPadre != null) {
            Caracteristica padre = caracteristicaRepository.findById(idPadre)
                    .orElseThrow(() -> new IllegalArgumentException("Característica padre no encontrada"));
            caracteristica.setPadre(padre);
        }

        return caracteristicaRepository.save(caracteristica);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> obtenerHierarchy() {
        return caracteristicaRepository.findHierarchy();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caracteristica> hijosActivos(Integer idPadre) {
        return caracteristicaRepository.findActiveHijos(idPadre);
    }

    @Override
    public void eliminar(Integer idCaracteristica) {
        Caracteristica caracteristica = caracteristicaRepository.findById(idCaracteristica)
                .orElseThrow(() -> new IllegalArgumentException("Característica no encontrada"));

        // Soft delete: desactivar en lugar de borrar
        caracteristica.setActivo(false);
        caracteristicaRepository.save(caracteristica);
    }
}
