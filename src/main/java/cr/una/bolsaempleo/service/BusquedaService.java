package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.dto.ResultadoCandidato;
import cr.una.bolsaempleo.model.Puesto;
import java.util.List;

public interface BusquedaService {

    
    List<ResultadoCandidato> buscarCandidatos(Integer idPuesto);

    
    List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> idsCaracteristicas);

    
    List<Puesto> buscarPorRangoSalario(java.math.BigDecimal salarioMin, java.math.BigDecimal salarioMax);

    
    List<Puesto> buscarPorKeyword(String keyword);
}
