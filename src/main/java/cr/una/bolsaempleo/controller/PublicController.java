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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;
    private final EmpresaService empresaService;
    private final OferenteService oferenteService;

    @GetMapping("/puestos/buscar-por-caracteristicas")
    public String buscarPorCaracteristicas(@RequestParam(value = "ids", required = false) List<Integer> ids, Model model) {
        List<Caracteristica> categorias = caracteristicaService.raices();
        model.addAttribute("categorias", categorias);

        if (ids != null && !ids.isEmpty()) {
            List<Puesto> resultados = puestoService.todosPublicosActivos().stream()
                    .filter(p -> {
                        if (p.getCaracteristicasRequeridas() == null) return false;
                        return p.getCaracteristicasRequeridas().stream()
                                .anyMatch(pc -> ids.contains(pc.getCaracteristica().getIdCaracteristica()));
                    })
                    .toList();
            model.addAttribute("puestos", resultados);
            model.addAttribute("idsSeleccionados", ids);
        }

        return "public/buscar-puestos";
    }

    @GetMapping("/registro/empresa")
    public String formRegistroEmpresa() {
        return "public/registro-empresa";
    }

    @PostMapping("/registro/empresa")
    public String registroEmpresa(@RequestParam String nombre,
                                  @RequestParam(required = false) String localizacion,
                                  @RequestParam String correo,
                                  @RequestParam(required = false) String telefono,
                                  @RequestParam(required = false) String descripcion,
                                  @RequestParam String password,
                                  Model model,
                                  RedirectAttributes attributes) {
        try {
            Empresa empresa = new Empresa();
            empresa.setNombre(nombre);
            empresa.setLocalizacion(localizacion);
            empresa.setCorreo(correo);
            empresa.setTelefono(telefono);
            empresa.setDescripcion(descripcion);
            empresa.setPasswordSinHashear(password);

            empresaService.registrar(empresa);
            attributes.addFlashAttribute("success", "Empresa registrada exitosamente. Espera la aprobacion del administrador.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "public/registro-empresa";
        }
    }

    @GetMapping("/registro/oferente")
    public String formRegistroOferente() {
        return "public/registro-oferente";
    }

    @PostMapping("/registro/oferente")
    public String registroOferente(@RequestParam String identificacion,
                                   @RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam(required = false) String nacionalidad,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam String correo,
                                   @RequestParam(required = false) String residencia,
                                   @RequestParam String password,
                                   Model model,
                                   RedirectAttributes attributes) {
        try {
            Oferente oferente = new Oferente();
            oferente.setIdentificacion(identificacion);
            oferente.setNombre(nombre);
            oferente.setApellido(apellido);
            oferente.setNacionalidad(nacionalidad);
            oferente.setTelefono(telefono);
            oferente.setCorreo(correo);
            oferente.setResidencia(residencia);
            oferente.setPasswordSinHashear(password);

            oferenteService.registrar(oferente);
            attributes.addFlashAttribute("success", "Oferente registrado exitosamente. Espera la aprobacion del administrador.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "public/registro-oferente";
        }
    }
}
