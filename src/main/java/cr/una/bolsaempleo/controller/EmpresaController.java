package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.dto.ResultadoCandidato;
import cr.una.bolsaempleo.model.*;
import cr.una.bolsaempleo.service.*;
import cr.una.bolsaempleo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;
    private final BusquedaService busquedaService;
    private final OferenteService oferenteService;

    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "empresa")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesion como empresa");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        model.addAttribute("puestosActivos", puestoService.puestosActivosPorEmpresa(user.getId()).size());
        return "empresa/dashboard";
    }

    @GetMapping("/mis-puestos")
    public String misPuestos(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Puesto> puestos = puestoService.puestosDeEmpresa(user.getId());
        model.addAttribute("usuario", user);
        model.addAttribute("puestos", puestos);
        return "empresa/mis-puestos";
    }

    @GetMapping("/nuevo-puesto")
    public String formNuevoPuesto(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> categorias = caracteristicaService.raices();
        model.addAttribute("usuario", user);
        model.addAttribute("categorias", categorias);
        return "empresa/nuevo-puesto";
    }

    @PostMapping("/nuevo-puesto")
    public String crearNuevoPuesto(@RequestParam String descripcion,
                                   @RequestParam(required = false) String salario,
                                   @RequestParam(required = false) String tipoPublicacion,
                                   @RequestParam(value = "caracteristicas", required = false) List<Integer> caracteristicas,
                                   HttpServletRequest request,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            Puesto puesto = new Puesto();
            puesto.setDescripcion(descripcion);
            if (salario != null && !salario.isEmpty()) {
                puesto.setSalario(new java.math.BigDecimal(salario));
            }
            puesto.setTipoPublicacion(tipoPublicacion != null ? tipoPublicacion : "publico");

            Empresa empresa = new Empresa();
            empresa.setIdEmpresa(user.getId());
            puesto.setEmpresa(empresa);

            List<Integer> idsCaracteristicas = caracteristicas != null ? caracteristicas : List.of();
            List<Integer> nivelesRequeridos = new ArrayList<>();

            // Para cada caracteristica seleccionada, obtener su nivel individual
            for (Integer idCar : idsCaracteristicas) {
                String nivelStr = request.getParameter("nivel_" + idCar);
                nivelesRequeridos.add(nivelStr != null ? Integer.parseInt(nivelStr) : 3);
            }

            puestoService.publicar(puesto, idsCaracteristicas, nivelesRequeridos);
            SessionUtil.addSuccessMessage(attributes, "Puesto publicado exitosamente");
            return "redirect:/empresa/mis-puestos";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/empresa/nuevo-puesto";
        }
    }

    @PostMapping("/desactivar")
    public String desactivarPuesto(@RequestParam Integer idPuesto,
                                   HttpSession session,
                                   RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            puestoService.desactivar(idPuesto);
            SessionUtil.addSuccessMessage(attributes, "Puesto desactivado");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error al desactivar: " + e.getMessage());
        }
        return "redirect:/empresa/mis-puestos";
    }

    @GetMapping("/buscar-candidatos")
    public String buscarCandidatos(@RequestParam Integer idPuesto,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            List<ResultadoCandidato> candidatos = busquedaService.buscarCandidatos(idPuesto);
            Optional<Puesto> puesto = puestoService.findById(idPuesto);

            model.addAttribute("usuario", user);
            model.addAttribute("puesto", puesto.orElse(null));
            model.addAttribute("candidatos", candidatos);
            return "empresa/candidatos";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/empresa/dashboard";
        }
    }

    @GetMapping("/detalle-candidato/{idOferente}")
    public String detalleCandidato(@PathVariable Integer idOferente,
                                   HttpSession session,
                                   Model model,
                                   RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            Optional<Oferente> oferente = oferenteService.findById(idOferente);
            if (oferente.isEmpty()) {
                SessionUtil.addErrorMessage(attributes, "Oferente no encontrado");
                return "redirect:/empresa/dashboard";
            }

            Object habilidades = oferenteService.habilidades(idOferente);
            Optional<Cv> cv = oferenteService.obtenerCv(idOferente);

            model.addAttribute("usuario", user);
            model.addAttribute("oferente", oferente.get());
            model.addAttribute("habilidades", habilidades);
            model.addAttribute("tieneCv", cv.isPresent());
            return "empresa/detalle-candidato";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/empresa/dashboard";
        }
    }

    @GetMapping("/descargar-cv/{idOferente}")
    public ResponseEntity<byte[]> descargarCvOferente(@PathVariable Integer idOferente,
                                                       HttpSession session) {
        if (!SessionUtil.isTipo(session, "empresa")) {
            return ResponseEntity.status(401).build();
        }
        try {
            Optional<Cv> cv = oferenteService.obtenerCv(idOferente);
            if (cv.isEmpty()) return ResponseEntity.notFound().build();

            Cv cvData = cv.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvData.getNombreArchivo() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(cvData.getArchivoPdf());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
