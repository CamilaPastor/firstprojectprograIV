package cr.una.bolsaempleo.service;

import java.io.IOException;

public interface ReporteService {

    byte[] generarReporteMensual(Integer anio, Integer mes) throws IOException;
}
