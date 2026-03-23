package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.service.EmpresaService;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.ReporteService;
import cr.una.bolsaempleo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final ReporteService reporteService;

    /**
     * Verifica que el usuario sea admin
     */
    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "admin")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesión como administrador");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    /**
     * Dashboard de administrador
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        long empresasPendientes = empresaService.pendientes().size();
        long oferentesPendientes = oferenteService.pendientes().size();
        long totalCaracteristicas = caracteristicaService.todas().size();

        model.addAttribute("usuario", user);
        model.addAttribute("empresasPendientes", empresasPendientes);
        model.addAttribute("oferentesPendientes", oferentesPendientes);
        model.addAttribute("totalCaracteristicas", totalCaracteristicas);

        return "admin/dashboard";
    }

    /**
     * Listar empresas pendientes de aprobación
     */
    @GetMapping("/empresas-pendientes")
    public String empresasPendientes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Empresa> pendientes = empresaService.pendientes();
        model.addAttribute("usuario", user);
        model.addAttribute("empresas", pendientes);

        return "admin/empresas-pendientes";
    }

    /**
     * Aprobar una empresa
     */
    @PostMapping("/aprobar-empresa")
    public String aprobarEmpresa(@RequestParam Integer idEmpresa,
                                HttpSession session,
                                RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            empresaService.aprobar(idEmpresa);
            SessionUtil.addSuccessMessage(attributes, "Empresa aprobada exitosamente");
            return "redirect:/admin/empresas-pendientes";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/admin/empresas-pendientes";
        }
    }

    /**
     * Listar oferentes pendientes de aprobación
     */
    @GetMapping("/oferentes-pendientes")
    public String oferentesPendientes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Oferente> pendientes = oferenteService.pendientes();
        model.addAttribute("usuario", user);
        model.addAttribute("oferentes", pendientes);

        return "admin/oferentes-pendientes";
    }

    /**
     * Aprobar un oferente
     */
    @PostMapping("/aprobar-oferente")
    public String aprobarOferente(@RequestParam Integer idOferente,
                                 HttpSession session,
                                 RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            oferenteService.aprobar(idOferente);
            SessionUtil.addSuccessMessage(attributes, "Oferente aprobado exitosamente");
            return "redirect:/admin/oferentes-pendientes";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/admin/oferentes-pendientes";
        }
    }

    /**
     * Gestionar características
     */
    @GetMapping("/caracteristicas")
    public String caracteristicas(@RequestParam(value = "idPadre", required = false) Integer idPadre,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> caracteristicas;
        if (idPadre != null) {
            caracteristicas = caracteristicaService.hijos(idPadre);
        } else {
            caracteristicas = caracteristicaService.raices();
        }

        List<Caracteristica> raices = caracteristicaService.raices();

        model.addAttribute("usuario", user);
        model.addAttribute("caracteristicas", caracteristicas);
        model.addAttribute("raices", raices);
        model.addAttribute("idPadre", idPadre);

        return "admin/caracteristicas";
    }

    /**
     * Formulario para crear característica
     */
    @GetMapping("/crear-caracteristica")
    public String formCrearCaracteristica(@RequestParam(value = "idPadre", required = false) Integer idPadre,
                                         HttpSession session,
                                         Model model,
                                         RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> raices = caracteristicaService.raices();

        model.addAttribute("usuario", user);
        model.addAttribute("raices", raices);
        model.addAttribute("idPadre", idPadre);

        return "admin/crear-caracteristica";
    }

    /**
     * Crear característica
     */
    @PostMapping("/crear-caracteristica")
    public String crearCaracteristica(@RequestParam String nombre,
                                     @RequestParam(value = "descripcion", required = false) String descripcion,
                                     @RequestParam(value = "idPadre", required = false) Integer idPadre,
                                     HttpSession session,
                                     RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            if (descripcion != null && !descripcion.isEmpty()) {
                caracteristicaService.crear(nombre, descripcion, idPadre);
            } else {
                caracteristicaService.crear(nombre, idPadre);
            }

            SessionUtil.addSuccessMessage(attributes, "Característica creada exitosamente");

            if (idPadre != null) {
                return "redirect:/admin/caracteristicas?idPadre=" + idPadre;
            } else {
                return "redirect:/admin/caracteristicas";
            }
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            if (idPadre != null) {
                return "redirect:/admin/crear-caracteristica?idPadre=" + idPadre;
            } else {
                return "redirect:/admin/crear-caracteristica";
            }
        }
    }

    /**
     * Página de reportes
     */
    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        return "admin/reportes";
    }

    /**
     * Generar reporte mensual en PDF
     */
    @GetMapping("/generar-reporte")
    public ResponseEntity<byte[]> generarReporte(@RequestParam Integer anio,
                                                @RequestParam Integer mes,
                                                HttpSession session,
                                                RedirectAttributes attributes) {
        SessionUser user = SessionUtil.getSessionUser(session);
        if (!SessionUtil.isTipo(session, "admin")) {
            return ResponseEntity.status(401).build();
        }

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
