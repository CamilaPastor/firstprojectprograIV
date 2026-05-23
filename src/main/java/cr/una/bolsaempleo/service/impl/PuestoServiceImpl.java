package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.PuestoCaracteristica;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.repository.PuestoRepository;
import cr.una.bolsaempleo.repository.EmpresaRepository;
import cr.una.bolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.una.bolsaempleo.repository.CaracteristicaRepository;
import cr.una.bolsaempleo.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PuestoServiceImpl implements PuestoService {

    private final PuestoRepository puestoRepository;
    private final EmpresaRepository empresaRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    @Override
    public Puesto publicar(Puesto puesto, List<Integer> idsCaracteristicas, List<Integer> nivelesRequeridos) {
        if (puesto.getEmpresa() == null || puesto.getEmpresa().getIdEmpresa() == null) {
            throw new IllegalArgumentException("Empresa no especificada");
        }

        Empresa empresa = empresaRepository.findById(puesto.getEmpresa().getIdEmpresa())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        puesto.setEmpresa(empresa);
        puesto.setActivo(true);

        Puesto puestoGuardado = puestoRepository.save(puesto);

        if (idsCaracteristicas != null && !idsCaracteristicas.isEmpty()) {
            for (int i = 0; i < idsCaracteristicas.size(); i++) {
                Integer idCaracteristica = idsCaracteristicas.get(i);
                Integer nivelRequerido = (i < nivelesRequeridos.size()) ? nivelesRequeridos.get(i) : 3;

                Caracteristica caracteristica = caracteristicaRepository.findById(idCaracteristica)
                        .orElseThrow(() -> new IllegalArgumentException("Característica no encontrada: " + idCaracteristica));

                PuestoCaracteristica pc = new PuestoCaracteristica();
                pc.setPuesto(puestoGuardado);
                pc.setCaracteristica(caracteristica);
                pc.setNivelRequerido(nivelRequerido);

                puestoCaracteristicaRepository.save(pc);
            }
        }

        return puestoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Puesto> ultimos5Publicos() {
        return puestoRepository.findTop5ByTipoPublicacionAndActivoTrueOrderByFechaRegistroDesc("publico");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Puesto> puestosDeEmpresa(Integer idEmpresa) {
        return puestoRepository.findByEmpresa_IdEmpresa(idEmpresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Puesto> findById(Integer idPuesto) {
        return puestoRepository.findById(idPuesto);
    }

    @Override
    public void desactivar(Integer idPuesto) {
        Puesto puesto = puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));

        puesto.setActivo(false);
        puestoRepository.save(puesto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Puesto> todosPublicosActivos() {
        return puestoRepository.findPublicosActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Puesto> todosActivos() {
        return puestoRepository.findActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Puesto> puestosActivosPorEmpresa(Integer idEmpresa) {
        return puestoRepository.findByEmpresa_IdEmpresaAndActivoTrue(idEmpresa);
    }
}
