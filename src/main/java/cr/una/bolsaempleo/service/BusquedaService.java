package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.dto.ResultadoCandidato;
import cr.una.bolsaempleo.model.Puesto;
import java.util.List;

public interface BusquedaService {

    /**
     * Busca candidatos calificados para un puesto
     * Retorna oferentes ordenados por porcentaje de cumplimiento (mayor a menor)
     * @param idPuesto ID del puesto
     * @return lista de resultados con oferentes y porcentaje de cumplimiento
     */
    List<ResultadoCandidato> buscarCandidatos(Integer idPuesto);

    /**
     * Busca puestos que requieren las características especificadas
     * @param idsCaracteristicas lista de IDs de características
     * @return lista de puestos que requieren al menos una de esas características
     */
    List<Puesto> buscarPuestosPorCaracteristicas(List<Integer> idsCaracteristicas);

    /**
     * Busca puestos activos en un rango de salario
     * @param salarioMin salario mínimo
     * @param salarioMax salario máximo
     * @return lista de puestos en ese rango
     */
    List<Puesto> buscarPorRangoSalario(java.math.BigDecimal salarioMin, java.math.BigDecimal salarioMax);

    /**
     * Búsqueda general por palabra clave
     * @param keyword palabra a buscar
     * @return lista de puestos que coinciden
     */
    List<Puesto> buscarPorKeyword(String keyword);
}
