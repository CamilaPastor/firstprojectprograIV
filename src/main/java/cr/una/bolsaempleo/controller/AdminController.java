package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.*;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.EmpresaService;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final ReporteService reporteService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return Map.of(
                "empresasPendientes", empresaService.pendientes().size(),
                "oferentesPendientes", oferenteService.pendientes().size(),
                "totalCaracteristicas", caracteristicaService.todas().size()
        );
    }

    @GetMapping("/empresas-pendientes")
    public List<EmpresaResponse> empresasPendientes() {
        return empresaService.pendientes().stream().map(EmpresaResponse::from).toList();
    }

    @PostMapping("/empresas/{idEmpresa}/aprobar")
    public ResponseEntity<?> aprobarEmpresa(@PathVariable Integer idEmpresa) {
        try {
            empresaService.aprobar(idEmpresa);
            return ResponseEntity.ok(MessageResponse.of("Empresa aprobada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/oferentes-pendientes")
    public List<OferenteResponse> oferentesPendientes() {
        return oferenteService.pendientes().stream().map(OferenteResponse::from).toList();
    }

    @PostMapping("/oferentes/{idOferente}/aprobar")
    public ResponseEntity<?> aprobarOferente(@PathVariable Integer idOferente) {
        try {
            oferenteService.aprobar(idOferente);
            return ResponseEntity.ok(MessageResponse.of("Oferente aprobado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/caracteristicas")
    public List<CaracteristicaResponse> caracteristicas(@RequestParam(value = "idPadre", required = false) Integer idPadre) {
        List<Caracteristica> lista = idPadre == null
                ? caracteristicaService.raices()
                : caracteristicaService.hijos(idPadre);
        return lista.stream().map(c -> CaracteristicaResponse.from(c, false)).toList();
    }

    @GetMapping("/caracteristicas/arbol")
    public List<CaracteristicaResponse> arbol() {
        return caracteristicaService.raices().stream().map(c -> CaracteristicaResponse.from(c, true)).toList();
    }

    @PostMapping("/caracteristicas")
    public ResponseEntity<?> crearCaracteristica(@Valid @RequestBody CaracteristicaRequest req) {
        try {
            Caracteristica c = caracteristicaService.crear(req.getNombre(), req.getDescripcion(), req.getIdPadre());
            return ResponseEntity.ok(CaracteristicaResponse.from(c, false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @DeleteMapping("/caracteristicas/{id}")
    public ResponseEntity<?> eliminarCaracteristica(@PathVariable("id") Integer id) {
        try {
            caracteristicaService.eliminar(id);
            return ResponseEntity.ok(MessageResponse.of("Caracteristica eliminada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/reportes/mensual")
    public ResponseEntity<byte[]> generarReporte(@RequestParam Integer anio, @RequestParam Integer mes) {
        try {
            byte[] pdfBytes = reporteService.generarReporteMensual(anio, mes);
            String nombreArchivo = String.format("Reporte_Puestos_%04d_%02d.pdf", anio, mes);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
