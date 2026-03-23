package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.service.PuestoService;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.EmpresaService;
import cr.una.bolsaempleo.service.OferenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;
    private final EmpresaService empresaService;
    private final OferenteService oferenteService;

    /**
     * Página de inicio - muestra últimos 5 puestos públicos
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Puesto> puestosRecientes = puestoService.ultimos5Publicos();
        model.addAttribute("puestos", puestosRecientes);
        return "public/index";
    }

    /**
     * Buscar puestos por características
     */
    @GetMapping("/puestos/buscar-por-caracteristicas")
    public String buscarPorCaracteristicas(@RequestParam(value = "ids", required = false) List<Integer> ids, Model model) {
        List<Caracteristica> categorias = caracteristicaService.raices();
        model.addAttribute("categorias", categorias);

        if (ids != null && !ids.isEmpty()) {
            List<Puesto> resultados = puestoService.todosPublicosActivos().stream()
                    .filter(p -> {
                        List<Caracteristica> caracteristicas = p.getCaracteristicasRequeridas().stream()
                                .map(pc -> pc.getCaracteristica())
                                .toList();
                        return caracteristicas.stream()
                                .anyMatch(c -> ids.contains(c.getIdCaracteristica()));
                    })
                    .toList();
            model.addAttribute("puestos", resultados);
            model.addAttribute("idsSeleccionados", ids);
        }

        return "puestos/buscar-caracteristicas";
    }

    /**
     * Formulario de registro de empresa
     */
    @GetMapping("/registro/empresa")
    public String formRegistroEmpresa(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "registro/empresa";
    }

    /**
     * Registrar nueva empresa
     */
    @PostMapping("/registro/empresa")
    public String registroEmpresa(@ModelAttribute Empresa empresa, Model model) {
        try {
            empresaService.registrar(empresa);
            model.addAttribute("success", "Empresa registrada exitosamente. Por favor inicia sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro/empresa";
        }
    }

    /**
     * Formulario de registro de oferente
     */
    @GetMapping("/registro/oferente")
    public String formRegistroOferente(Model model) {
        model.addAttribute("oferente", new Oferente());
        return "registro/oferente";
    }

    /**
     * Registrar nuevo oferente
     */
    @PostMapping("/registro/oferente")
    public String registroOferente(@ModelAttribute Oferente oferente, Model model) {
        try {
            oferenteService.registrar(oferente);
            model.addAttribute("success", "Oferente registrado exitosamente. Por favor inicia sesión.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro/oferente";
        }
    }
}