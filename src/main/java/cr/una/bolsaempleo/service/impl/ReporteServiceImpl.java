package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.repository.PuestoRepository;
import cr.una.bolsaempleo.repository.OferenteRepository;
import cr.una.bolsaempleo.service.ReporteService;
import cr.una.bolsaempleo.service.BusquedaService;
import cr.una.bolsaempleo.dto.ResultadoCandidato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final PuestoRepository puestoRepository;
    private final OferenteRepository oferenteRepository;
    private final BusquedaService busquedaService;

    @Override
    public byte[] generarReporteMensual(Integer anio, Integer mes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título
        document.add(new Paragraph("Reporte de Puestos")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        // Mes y año
        String mesAnio = String.format("Mes: %02d/%d", mes, anio);
        document.add(new Paragraph(mesAnio)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        // Obtener puestos del mes
        List<Puesto> puestos = puestoRepository.findByTipoPublicacionAndActivoTrue("publico").stream()
                .filter(p -> {
                    YearMonth yearMonth = YearMonth.from(p.getFechaRegistro());
                    return yearMonth.getYear() == anio && yearMonth.getMonthValue() == mes;
                })
                .toList();

        // Tabla de puestos
        Table table = new Table(5);
        table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

        // Encabezados
        String[] headers = {"ID", "Empresa", "Descripción", "Salario", "Tipo"};
        for (String header : headers) {
            Cell cell = new Cell();
            cell.add(new Paragraph(header).setBold());
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
        }

        // Datos
        for (Puesto puesto : puestos) {
            table.addCell(new Cell().add(new Paragraph(puesto.getIdPuesto().toString())));
            table.addCell(new Cell().add(new Paragraph(puesto.getEmpresa().getNombre())));
            table.addCell(new Cell().add(new Paragraph(
                    puesto.getDescripcion().substring(0, Math.min(50, puesto.getDescripcion().length())) + "..."
            )));
            table.addCell(new Cell().add(new Paragraph(
                    puesto.getSalario() != null ? "₡ " + puesto.getSalario().toString() : "N/A"
            )));
            table.addCell(new Cell().add(new Paragraph(puesto.getTipoPublicacion())));
        }

        document.add(table);

        // Estadísticas
        document.add(new Paragraph("\n\nEstadísticas:"));
        document.add(new Paragraph("Total de puestos: " + puestos.size()));

        document.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] generarReporteCandidatos(Integer idPuesto) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Obtener puesto
        Puesto puesto = puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));

        // Título
        document.add(new Paragraph("Reporte de Candidatos")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        document.add(new Paragraph("Puesto: " + puesto.getEmpresa().getNombre())
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        // Obtener candidatos
        List<ResultadoCandidato> candidatos = busquedaService.buscarCandidatos(idPuesto);

        // Tabla de candidatos
        Table table = new Table(5);
        table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

        // Encabezados
        String[] headers = {"Nombre", "Email", "Cumplidos", "Total", "Porcentaje"};
        for (String header : headers) {
            Cell cell = new Cell();
            cell.add(new Paragraph(header).setBold());
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
        }

        // Datos
        for (ResultadoCandidato rc : candidatos) {
            Oferente o = rc.getOferente();
            table.addCell(new Cell().add(new Paragraph(o.getNombre() + " " + o.getApellido())));
            table.addCell(new Cell().add(new Paragraph(o.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(rc.getCumplidos().toString())));
            table.addCell(new Cell().add(new Paragraph(rc.getTotal().toString())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f%%", rc.getPorcentaje()))));
        }

        document.add(table);

        document.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] generarReporteOferentes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Título
        document.add(new Paragraph("Reporte de Oferentes Aprobados")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        document.add(new Paragraph("Fecha: " + LocalDate.now().toString())
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        // Obtener oferentes aprobados
        List<Oferente> oferentes = oferenteRepository.findAprobadosConCv();

        // Tabla de oferentes
        Table table = new Table(5);
        table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

        // Encabezados
        String[] headers = {"Nombre", "Email", "Nacionalidad", "Residencia", "CV"};
        for (String header : headers) {
            Cell cell = new Cell();
            cell.add(new Paragraph(header).setBold());
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
        }

        // Datos
        for (Oferente o : oferentes) {
            table.addCell(new Cell().add(new Paragraph(o.getNombre() + " " + o.getApellido())));
            table.addCell(new Cell().add(new Paragraph(o.getCorreo())));
            table.addCell(new Cell().add(new Paragraph(o.getNacionalidad() != null ? o.getNacionalidad() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(o.getResidencia() != null ? o.getResidencia() : "N/A")));
            table.addCell(new Cell().add(new Paragraph(o.getCv() != null ? "Sí" : "No")));
        }

        document.add(table);

        // Estadísticas
        document.add(new Paragraph("\n\nEstadísticas:"));
        document.add(new Paragraph("Total de oferentes: " + oferentes.size()));

        document.close();
        return outputStream.toByteArray();
    }
}
