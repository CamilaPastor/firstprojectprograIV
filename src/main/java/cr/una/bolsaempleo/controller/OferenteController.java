package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.*;
import cr.una.bolsaempleo.model.Cv;
import cr.una.bolsaempleo.model.OferenteCaracteristica;
import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.security.AuthenticatedUser;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.service.PuestoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/oferente")
@RequiredArgsConstructor
public class OferenteController {

    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final PuestoService puestoService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@AuthenticationPrincipal AuthenticatedUser user) {
        @SuppressWarnings("unchecked")
        List<OferenteCaracteristica> habilidades = (List<OferenteCaracteristica>) oferenteService.habilidades(user.getId());
        Optional<Cv> cv = oferenteService.obtenerCv(user.getId());
        return Map.of(
                "totalHabilidades", habilidades != null ? habilidades.size() : 0,
                "tieneCv", cv.isPresent()
        );
    }

    @GetMapping("/puestos")
    public List<PuestoResponse> puestos() {
        return puestoService.todosActivos().stream().map(PuestoResponse::from).toList();
    }

    @GetMapping("/puestos/buscar")
    public List<PuestoResponse> buscar(@RequestParam(value = "ids", required = false) List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return puestoService.todosActivos().stream()
                .filter(p -> p.getCaracteristicasRequeridas() != null
                        && p.getCaracteristicasRequeridas().stream()
                                .anyMatch(pc -> ids.contains(pc.getCaracteristica().getIdCaracteristica())))
                .map(PuestoResponse::from)
                .toList();
    }

    @GetMapping("/habilidades")
    public List<HabilidadResponse> habilidades(@AuthenticationPrincipal AuthenticatedUser user) {
        @SuppressWarnings("unchecked")
        List<OferenteCaracteristica> hs = (List<OferenteCaracteristica>) oferenteService.habilidades(user.getId());
        return hs.stream().map(HabilidadResponse::from).toList();
    }

    @GetMapping("/caracteristicas/arbol")
    public List<CaracteristicaResponse> arbol() {
        return caracteristicaService.raices().stream().map(c -> CaracteristicaResponse.from(c, true)).toList();
    }

    @PostMapping("/habilidades")
    public ResponseEntity<?> agregarHabilidad(@AuthenticationPrincipal AuthenticatedUser user,
                                              @Valid @RequestBody HabilidadRequest req) {
        try {
            oferenteService.agregarHabilidad(user.getId(), req.getIdCaracteristica(), req.getNivel());
            return ResponseEntity.ok(MessageResponse.of("Habilidad agregada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @DeleteMapping("/habilidades/{idCaracteristica}")
    public ResponseEntity<?> eliminarHabilidad(@AuthenticationPrincipal AuthenticatedUser user,
                                               @PathVariable Integer idCaracteristica) {
        try {
            oferenteService.eliminarHabilidad(user.getId(), idCaracteristica);
            return ResponseEntity.ok(MessageResponse.of("Habilidad eliminada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/cv")
    public ResponseEntity<?> miCv(@AuthenticationPrincipal AuthenticatedUser user) {
        Optional<Cv> cv = oferenteService.obtenerCv(user.getId());
        return ResponseEntity.ok(Map.of(
                "tieneCv", cv.isPresent(),
                "nombreArchivo", cv.map(Cv::getNombreArchivo).orElse(null),
                "fechaSubida", cv.map(Cv::getFechaSubida).orElse(null)
        ));
    }

    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirCv(@AuthenticationPrincipal AuthenticatedUser user,
                                     @RequestParam("archivo") MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body(MessageResponse.of("Por favor selecciona un archivo"));
            }
            String contentType = archivo.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest().body(MessageResponse.of("Solo se aceptan archivos PDF"));
            }
            oferenteService.subirCv(user.getId(), archivo.getBytes(), archivo.getOriginalFilename());
            return ResponseEntity.ok(MessageResponse.of("CV subido exitosamente"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(MessageResponse.of("Error al subir el archivo: " + e.getMessage()));
        }
    }

    @GetMapping("/cv/descargar")
    public ResponseEntity<byte[]> descargarCv(@AuthenticationPrincipal AuthenticatedUser user) {
        Optional<Cv> cv = oferenteService.obtenerCv(user.getId());
        if (cv.isEmpty() || cv.get().getArchivoPdf() == null) return ResponseEntity.notFound().build();
        Cv cvData = cv.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvData.getNombreArchivo() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(cvData.getArchivoPdf());
    }
}
