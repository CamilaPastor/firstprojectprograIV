package cr.una.bolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping(value = {
            "/",
            "/login",
            "/registro/empresa",
            "/registro/oferente",
            "/buscar",
            "/admin/{path:[^\\.]*}",
            "/admin",
            "/empresa/{path:[^\\.]*}",
            "/empresa",
            "/oferente/{path:[^\\.]*}",
            "/oferente"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
