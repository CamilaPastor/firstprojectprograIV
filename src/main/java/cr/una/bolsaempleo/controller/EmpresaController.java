package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.dto.ResultadoCandidato;
import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.service.PuestoService;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.BusquedaService;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
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

    /**
     * Verifica que el usuario sea empresa
     */
    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "empresa")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesión como empresa");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    /**
     * Dashboard de empresa
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        Optional<Puesto> puesto = puestoService.findById(user.getId());
        model.addAttribute("usuario", user);
        model.addAttribute("puestosActivos", puestoService.puestosActivosPorEmpresa(user.getId()).size());

        return "empresa/dashboard";
    }

    /**
     * Listar mis puestos
     */
    @GetMapping("/mis-puestos")
    public String misPuestos(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Puesto> puestos = puestoService.puestosDeEmpresa(user.getId());
        model.addAttribute("usuario", user);
        model.addAttribute("puestos", puestos);

        return "empresa/mis-puestos";
    }

    /**
     * Formulario para crear nuevo puesto
     */
    @GetMapping("/nuevo-puesto")
    public String formNuevoPuesto(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> categorias = caracteristicaService.raices();
        model.addAttribute("usuario", user);
        model.addAttribute("puesto", new Puesto());
        model.addAttribute("categorias", categorias);

        return "empresa/nuevo-puesto";
    }

    /**
     * Crear nuevo puesto con características
     */
    @PostMapping("/nuevo-puesto")
    public String crearNuevoPuesto(@ModelAttribute Puesto puesto,
                                  @RequestParam(value = "caracteristicas", required = false) List<Integer> caracteristicas,
                                  @RequestParam(value = "niveles", required = false) List<Integer> niveles,
                                  HttpSession session,
                                  Model model,
                                  RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            puesto.setEmpresa(new cr.una.bolsaempleo.model.Empresa());
            puesto.getEmpresa().setIdEmpresa(user.getId());

            List<Integer> idsCaracteristicas = caracteristicas != null ? caracteristicas : List.of();
            List<Integer> nivelesRequeridos = niveles != null ? niveles : List.of();

            puestoService.publicar(puesto, idsCaracteristicas, nivelesRequeridos);

            SessionUtil.addSuccessMessage(attributes, "Puesto publicado exitosamente");
            return "redirect:/empresa/mis-puestos";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "empresa/nuevo-puesto";
        }
    }

    /**
     * Desactivar un puesto
     */
    @PostMapping("/desactivar")
    public String desactivarPuesto(@RequestParam Integer idPuesto,
                                  HttpSession session,
                                  RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            puestoService.desactivar(idPuesto);
            SessionUtil.addSuccessMessage(attributes, "Puesto desactivado");
            return "redirect:/empresa/mis-puestos";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error al desactivar: " + e.getMessage());
            return "redirect:/empresa/mis-puestos";
        }
    }

    /**
     * Buscar candidatos para un puesto
     */
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

    /**
     * Ver detalles de un candidato
     */
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
            Optional<?> cv = oferenteService.obtenerCv(idOferente);

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
}
