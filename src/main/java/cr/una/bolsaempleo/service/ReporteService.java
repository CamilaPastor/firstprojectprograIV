package cr.una.bolsaempleo.service;

import java.io.IOException;

public interface ReporteService {

    /**
     * Genera un reporte en PDF con todos los puestos del mes especificado
     * El PDF contiene una tabla con información de los puestos
     * @param anio año del reporte
     * @param mes mes del reporte (1-12)
     * @return bytes del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    byte[] generarReporteMensual(Integer anio, Integer mes) throws IOException;

    /**
     * Genera un reporte PDF de candidatos para un puesto
     * @param idPuesto ID del puesto
     * @return bytes del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    byte[] generarReporteCandidatos(Integer idPuesto) throws IOException;

    /**
     * Genera un reporte PDF de oferentes aprobados
     * @return bytes del PDF generado
     * @throws IOException si ocurre un error al generar el PDF
     */
    byte[] generarReporteOferentes() throws IOException;
}
