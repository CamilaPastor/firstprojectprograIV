package cr.una.bolsaempleo.util;

import cr.una.bolsaempleo.dto.SessionUser;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String SESSION_USER = "usuario";

    
    public static SessionUser getSessionUser(HttpSession session) {
        return (SessionUser) session.getAttribute(SESSION_USER);
    }

    
    public static void setSessionUser(HttpSession session, SessionUser usuario) {
        session.setAttribute(SESSION_USER, usuario);
    }

    
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER) != null;
    }

    
    public static boolean isTipo(HttpSession session, String tipoUsuario) {
        SessionUser usuario = getSessionUser(session);
        return usuario != null && usuario.getTipoUsuario().equals(tipoUsuario);
    }

    
    public static void invalidateSession(HttpSession session) {
        session.invalidate();
    }

    
    public static void addErrorMessage(RedirectAttributes attributes, String mensaje) {
        attributes.addFlashAttribute("error", mensaje);
    }

    
    public static void addSuccessMessage(RedirectAttributes attributes, String mensaje) {
        attributes.addFlashAttribute("success", mensaje);
    }
}
