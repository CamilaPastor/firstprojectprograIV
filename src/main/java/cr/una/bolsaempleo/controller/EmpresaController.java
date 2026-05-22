package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.*;
import cr.una.bolsaempleo.model.*;
import cr.una.bolsaempleo.security.AuthenticatedUser;
import cr.una.bolsaempleo.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final PuestoService puestoService;
    private final BusquedaService busquedaService;
    private final OferenteService oferenteService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@AuthenticationPrincipal AuthenticatedUser user) {
        return Map.of(
                "puestosActivos", puestoService.puestosActivosPorEmpresa(user.getId()).size()
        );
    }

    @GetMapping("/puestos")
    public List<PuestoResponse> misPuestos(@AuthenticationPrincipal AuthenticatedUser user) {
        return puestoService.puestosDeEmpresa(user.getId()).stream().map(PuestoResponse::from).toList();
    }

    @PostMapping("/puestos")
    public ResponseEntity<?> nuevoPuesto(@AuthenticationPrincipal AuthenticatedUser user,
                                         @Valid @RequestBody PuestoRequest req) {
        try {
            Puesto puesto = new Puesto();
            puesto.setDescripcion(req.getDescripcion());
            puesto.setSalario(req.getSalario());
            puesto.setTipoPublicacion(req.getTipoPublicacion() != null ? req.getTipoPublicacion() : "publico");

            Empresa empresa = new Empresa();
            empresa.setIdEmpresa(user.getId());
            puesto.setEmpresa(empresa);

            List<Integer> ids = new ArrayList<>();
            List<Integer> niveles = new ArrayList<>();
            if (req.getCaracteristicas() != null) {
                for (PuestoRequest.CaracteristicaPuesto c : req.getCaracteristicas()) {
                    ids.add(c.getIdCaracteristica());
                    niveles.add(c.getNivelRequerido() != null ? c.getNivelRequerido() : 3);
                }
            }

            Puesto guardado = puestoService.publicar(puesto, ids, niveles);
            return ResponseEntity.ok(PuestoResponse.from(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @PostMapping("/puestos/{idPuesto}/desactivar")
    public ResponseEntity<?> desactivar(@AuthenticationPrincipal AuthenticatedUser user,
                                        @PathVariable Integer idPuesto) {
        try {
            if (!esPuestoDeEmpresa(idPuesto, user.getId())) {
                return ResponseEntity.status(403).body(MessageResponse.of("No puedes modificar un puesto de otra empresa"));
            }
            puestoService.desactivar(idPuesto);
            return ResponseEntity.ok(MessageResponse.of("Puesto desactivado"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/puestos/{idPuesto}/candidatos")
    public ResponseEntity<?> buscarCandidatos(@AuthenticationPrincipal AuthenticatedUser user,
                                              @PathVariable Integer idPuesto) {
        try {
            if (!esPuestoDeEmpresa(idPuesto, user.getId())) {
                return ResponseEntity.status(403).body(MessageResponse.of("No puedes ver los candidatos de un puesto de otra empresa"));
            }
            List<ResultadoCandidato> candidatos = busquedaService.buscarCandidatos(idPuesto);
            Optional<Puesto> puesto = puestoService.findById(idPuesto);
            return ResponseEntity.ok(Map.of(
                    "puesto", puesto.map(PuestoResponse::from).orElse(null),
                    "candidatos", candidatos.stream().map(CandidatoResponse::from).toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @GetMapping("/oferentes/{idOferente}")
    public ResponseEntity<?> detalleCandidato(@PathVariable Integer idOferente) {
        Optional<Oferente> oferente = oferenteService.findById(idOferente);
        if (oferente.isEmpty()) return ResponseEntity.notFound().build();

        @SuppressWarnings("unchecked")
        List<OferenteCaracteristica> habilidades = (List<OferenteCaracteristica>) oferenteService.habilidades(idOferente);
        Optional<Cv> cv = oferenteService.obtenerCv(idOferente);

        return ResponseEntity.ok(Map.of(
                "oferente", OferenteResponse.from(oferente.get()),
                "habilidades", habilidades.stream().map(HabilidadResponse::from).toList(),
                "tieneCv", cv.isPresent()
        ));
    }

    @GetMapping("/oferentes/{idOferente}/cv")
    public ResponseEntity<byte[]> descargarCv(@PathVariable Integer idOferente) {
        Optional<Cv> cv = oferenteService.obtenerCv(idOferente);
        if (cv.isEmpty()) return ResponseEntity.notFound().build();
        Cv cvData = cv.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvData.getNombreArchivo() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(cvData.getArchivoPdf());
    }

    private boolean esPuestoDeEmpresa(Integer idPuesto, Integer idEmpresa) {
        return puestoService.findById(idPuesto)
                .map(p -> p.getEmpresa() != null && idEmpresa.equals(p.getEmpresa().getIdEmpresa()))
                .orElse(false);
    }
}
