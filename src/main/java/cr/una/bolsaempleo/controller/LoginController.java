package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.Administrador;
import cr.una.bolsaempleo.service.EmpresaService;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.service.AdminService;
import cr.una.bolsaempleo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final AdminService adminService;

    @GetMapping("/login")
    public String formLogin() {
        return "public/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String tipo,
                       @RequestParam String usuario,
                       @RequestParam String password,
                       HttpSession session,
                       Model model,
                       RedirectAttributes attributes) {
        try {
            SessionUser sessionUser = null;

            if ("empresa".equals(tipo)) {
                Optional<Empresa> empresa = empresaService.loginPorCorreo(usuario, password);
                if (empresa.isPresent() && empresa.get().getAprobado()) {
                    Empresa e = empresa.get();
                    sessionUser = new SessionUser(e.getIdEmpresa(), e.getNombre(), e.getCorreo(), "empresa");
                } else if (empresa.isPresent() && !empresa.get().getAprobado()) {
                    model.addAttribute("error", "Tu cuenta de empresa aun no ha sido aprobada por el administrador");
                    return "public/login";
                } else {
                    model.addAttribute("error", "Correo o contraseña incorrectos");
                    return "public/login";
                }
            } else if ("oferente".equals(tipo)) {
                Optional<Oferente> oferente = oferenteService.loginPorCorreo(usuario, password);
                if (oferente.isPresent() && oferente.get().getAprobado()) {
                    Oferente o = oferente.get();
                    sessionUser = new SessionUser(o.getIdOferente(), o.getNombre() + " " + o.getApellido(), o.getCorreo(), "oferente");
                } else if (oferente.isPresent() && !oferente.get().getAprobado()) {
                    model.addAttribute("error", "Tu cuenta de oferente aun no ha sido aprobada por el administrador");
                    return "public/login";
                } else {
                    model.addAttribute("error", "Correo o contraseña incorrectos");
                    return "public/login";
                }
            } else if ("admin".equals(tipo)) {
                Optional<Administrador> admin = adminService.login(usuario, password);
                if (admin.isPresent()) {
                    Administrador a = admin.get();
                    sessionUser = new SessionUser(a.getIdAdmin(), "Admin", a.getIdentificacion(), "admin");
                } else {
                    model.addAttribute("error", "Identificacion o contraseña incorrectos");
                    return "public/login";
                }
            } else {
                model.addAttribute("error", "Tipo de usuario invalido");
                return "public/login";
            }

            if (sessionUser != null) {
                SessionUtil.setSessionUser(session, sessionUser);
                return switch (sessionUser.getTipoUsuario()) {
                    case "empresa" -> "redirect:/empresa/dashboard";
                    case "oferente" -> "redirect:/oferente/dashboard";
                    case "admin" -> "redirect:/admin/dashboard";
                    default -> "redirect:/";
                };
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar sesion: " + e.getMessage());
        }
        return "public/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.invalidateSession(session);
        return "redirect:/";
    }
}
