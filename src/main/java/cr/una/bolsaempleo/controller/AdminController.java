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
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;
    private final ReporteService reporteService;

    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "admin")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesion como administrador");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        model.addAttribute("empresasPendientes", empresaService.pendientes().size());
        model.addAttribute("oferentesPendientes", oferenteService.pendientes().size());
        model.addAttribute("totalCaracteristicas", caracteristicaService.todas().size());
        return "admin/dashboard";
    }

    @GetMapping("/empresas-pendientes")
    public String empresasPendientes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        model.addAttribute("empresas", empresaService.pendientes());
        return "admin/empresas-pendientes";
    }

    @PostMapping("/aprobar-empresa")
    public String aprobarEmpresa(@RequestParam Integer idEmpresa,
                                 HttpSession session,
                                 RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            empresaService.aprobar(idEmpresa);
            SessionUtil.addSuccessMessage(attributes, "Empresa aprobada y clave generada exitosamente");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }
        return "redirect:/admin/empresas-pendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String oferentesPendientes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        model.addAttribute("oferentes", oferenteService.pendientes());
        return "admin/oferentes-pendientes";
    }

    @PostMapping("/aprobar-oferente")
    public String aprobarOferente(@RequestParam Integer idOferente,
                                  HttpSession session,
                                  RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            oferenteService.aprobar(idOferente);
            SessionUtil.addSuccessMessage(attributes, "Oferente aprobado exitosamente");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }
        return "redirect:/admin/oferentes-pendientes";
    }

    @GetMapping("/caracteristicas")
    public String caracteristicas(@RequestParam(value = "actualId", required = false) Integer actualId,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> raices = caracteristicaService.raices();
        List<Caracteristica> subcategorias = null;
        Caracteristica categoriaActual = null;

        if (actualId != null) {
            Optional<Caracteristica> cat = caracteristicaService.findById(actualId);
            if (cat.isPresent()) {
                categoriaActual = cat.get();
                subcategorias = caracteristicaService.hijos(actualId);
            }
        }

        model.addAttribute("usuario", user);
        model.addAttribute("raices", raices);
        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("categoriaActual", categoriaActual);
        model.addAttribute("actualId", actualId);
        return "admin/caracteristicas";
    }

    @PostMapping("/crear-caracteristica")
    public String crearCaracteristica(@RequestParam String nombre,
                                      @RequestParam(value = "idPadre", required = false) Integer idPadre,
                                      HttpSession session,
                                      RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            caracteristicaService.crear(nombre, idPadre);
            SessionUtil.addSuccessMessage(attributes, "Caracteristica creada exitosamente");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }

        if (idPadre != null) {
            return "redirect:/admin/caracteristicas?actualId=" + idPadre;
        }
        return "redirect:/admin/caracteristicas";
    }

    @PostMapping("/eliminar-caracteristica")
    public String eliminarCaracteristica(@RequestParam Integer idCaracteristica,
                                         HttpSession session,
                                         RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            SessionUtil.addSuccessMessage(attributes, "Caracteristica eliminada");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }
        return "redirect:/admin/caracteristicas";
    }

    @GetMapping("/reportes")
    public String reportes(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        return "admin/reportes";
    }

    @GetMapping("/generar-reporte")
    public ResponseEntity<byte[]> generarReporte(@RequestParam Integer anio,
                                                 @RequestParam Integer mes,
                                                 HttpSession session) {
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
