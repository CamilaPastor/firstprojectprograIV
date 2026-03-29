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
    public String login(@RequestParam String usuario,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {

            Optional<Administrador> admin = adminService.login(usuario, password);
            if (admin.isPresent()) {
                Administrador a = admin.get();
                SessionUser sessionUser = new SessionUser(a.getIdAdmin(), "Admin", a.getIdentificacion(), "admin");
                SessionUtil.setSessionUser(session, sessionUser);
                return "redirect:/admin/dashboard";
            }

            Optional<Empresa> empresa = empresaService.loginPorCorreo(usuario, password);
            if (empresa.isPresent()) {
                Empresa e = empresa.get();
                if (!e.getAprobado()) {
                    model.addAttribute("error", "Tu cuenta de empresa aun no ha sido aprobada por el administrador.");
                    return "public/login";
                }
                SessionUser sessionUser = new SessionUser(e.getIdEmpresa(), e.getNombre(), e.getCorreo(), "empresa");
                SessionUtil.setSessionUser(session, sessionUser);
                return "redirect:/empresa/dashboard";
            }

            Optional<Oferente> oferente = oferenteService.loginPorCorreo(usuario, password);
            if (oferente.isPresent()) {
                Oferente o = oferente.get();
                if (!o.getAprobado()) {
                    model.addAttribute("error", "Tu cuenta de oferente aun no ha sido aprobada por el administrador.");
                    return "public/login";
                }
                SessionUser sessionUser = new SessionUser(o.getIdOferente(),
                        o.getNombre() + " " + o.getApellido(), o.getCorreo(), "oferente");
                SessionUtil.setSessionUser(session, sessionUser);
                return "redirect:/oferente/dashboard";
            }

            model.addAttribute("error", "Usuario o contraseña incorrectos.");
            return "public/login";

        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar sesion: " + e.getMessage());
            return "public/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.invalidateSession(session);
        return "redirect:/";
    }
}
