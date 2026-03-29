package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.dto.ResultadoCandidato;
import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.PuestoCaracteristica;
import cr.una.bolsaempleo.model.OferenteCaracteristica;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.repository.PuestoRepository;
import cr.una.bolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.una.bolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.una.bolsaempleo.repository.OferenteRepository;
import cr.una.bolsaempleo.service.BusquedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusquedaServiceImpl implements BusquedaService {

    private final PuestoRepository puestoRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepository;
    private final OferenteRepository oferenteRepository;

    @Override
    public List<ResultadoCandidato> buscarCandidatos(Integer idPuesto) {
        Puesto puesto = puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));

        List<PuestoCaracteristica> requeridas = puestoCaracteristicaRepository
                .findByPuesto_IdPuesto(idPuesto);

        if (requeridas.isEmpty()) {
            return Collections.emptyList();
        }

        List<Oferente> oferentes = oferenteRepository.findAllActivos();

        List<ResultadoCandidato> resultados = new ArrayList<>();

        for (Oferente oferente : oferentes) {
            int cumplidos = 0;

            for (PuestoCaracteristica pc : requeridas) {
                OferenteCaracteristica oc = oferenteCaracteristicaRepository
                        .findByOferenteAndCaracteristica(
                                oferente.getIdOferente(),
                                pc.getCaracteristica().getIdCaracteristica()
                        );

                if (oc != null && oc.getNivel() >= pc.getNivelRequerido()) {
                    cumplidos++;
                }
            }

            ResultadoCandidato resultado = new ResultadoCandidato(
                    oferente,
                    cumplidos,
                    requeridas.size()
            );

            resultados.add(resultado);
        }

        return resultados.stream()
                .sorted((a, b) -> b.getPorcentaje().compareTo(a.getPorcentaje()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> idsCaracteristicas) {
        if (idsCaracteristicas == null || idsCaracteristicas.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Puesto> puestos = new HashSet<>();

        for (Integer idCaracteristica : idsCaracteristicas) {
            List<PuestoCaracteristica> puestosCaracteristica = puestoCaracteristicaRepository
                    .findByCaracteristica_IdCaracteristica(idCaracteristica);

            for (PuestoCaracteristica pc : puestosCaracteristica) {
                if (pc.getPuesto().getActivo()) {
                    puestos.add(pc.getPuesto());
                }
            }
        }

        return new ArrayList<>(puestos);
    }

    @Override
    public List<Puesto> buscarPorRangoSalario(BigDecimal salarioMin, BigDecimal salarioMax) {
        return puestoRepository.findByRangoSalario(salarioMin, salarioMax);
    }

    @Override
    public List<Puesto> buscarPorKeyword(String keyword) {
        return puestoRepository.buscarPorKeyword(keyword);
    }
}
