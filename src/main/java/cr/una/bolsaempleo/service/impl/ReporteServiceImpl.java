package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.repository.PuestoRepository;
import cr.una.bolsaempleo.service.ReporteService;
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
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final PuestoRepository puestoRepository;

    @Override
    public byte[] generarReporteMensual(Integer anio, Integer mes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Reporte de Puestos")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        String mesAnio = String.format("Mes: %02d/%d", mes, anio);
        document.add(new Paragraph(mesAnio)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        List<Puesto> puestos = puestoRepository.findByTipoPublicacionAndActivoTrue("publico").stream()
                .filter(p -> {
                    YearMonth yearMonth = YearMonth.from(p.getFechaRegistro());
                    return yearMonth.getYear() == anio && yearMonth.getMonthValue() == mes;
                })
                .toList();

        Table table = new Table(5);
        table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

        String[] headers = {"ID", "Empresa", "Descripción", "Salario", "Tipo"};
        for (String header : headers) {
            Cell cell = new Cell();
            cell.add(new Paragraph(header).setBold());
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
        }

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

        document.add(new Paragraph("\n\nEstadísticas:"));
        document.add(new Paragraph("Total de puestos: " + puestos.size()));

        document.close();
        return outputStream.toByteArray();
    }
}
