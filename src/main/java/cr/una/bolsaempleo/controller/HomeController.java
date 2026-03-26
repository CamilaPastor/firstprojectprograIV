package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PuestoService puestoService;

    @GetMapping("/")
    public String index(Model model) {
        List<Puesto> puestos = puestoService.ultimos5Publicos();
        model.addAttribute("puestos", puestos);
        return "public/inicio";
    }
}
