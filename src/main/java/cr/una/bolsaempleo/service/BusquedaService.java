package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.dto.ResultadoCandidato;
import java.util.List;

public interface BusquedaService {

    List<ResultadoCandidato> buscarCandidatos(Integer idPuesto);
}
