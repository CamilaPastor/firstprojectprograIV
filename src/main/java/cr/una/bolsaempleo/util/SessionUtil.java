package cr.una.bolsaempleo.util;

import cr.una.bolsaempleo.dto.SessionUser;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    private static final String SESSION_USER = "usuario";

    /**
     * Obtiene el usuario de la sesión
     * @param session sesión HTTP
     * @return SessionUser o null si no existe
     */
    public static SessionUser getSessionUser(HttpSession session) {
        return (SessionUser) session.getAttribute(SESSION_USER);
    }

    /**
     * Guarda un usuario en la sesión
     * @param session sesión HTTP
     * @param usuario usuario a guardar
     */
    public static void setSessionUser(HttpSession session, SessionUser usuario) {
        session.setAttribute(SESSION_USER, usuario);
    }

    /**
     * Verifica si hay usuario en sesión
     * @param session sesión HTTP
     * @return true si existe usuario en sesión
     */
    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER) != null;
    }

    /**
     * Verifica si el usuario es de un tipo específico
     * @param session sesión HTTP
     * @param tipoUsuario tipo a verificar ("empresa", "oferente", "admin")
     * @return true si el usuario en sesión es del tipo especificado
     */
    public static boolean isTipo(HttpSession session, String tipoUsuario) {
        SessionUser usuario = getSessionUser(session);
        return usuario != null && usuario.getTipoUsuario().equals(tipoUsuario);
    }

    /**
     * Invalida la sesión del usuario
     * @param session sesión HTTP
     */
    public static void invalidateSession(HttpSession session) {
        session.invalidate();
    }

    /**
     * Agrega mensaje de error flash
     * @param attributes redirect attributes
     * @param mensaje mensaje de error
     */
    public static void addErrorMessage(RedirectAttributes attributes, String mensaje) {
        attributes.addFlashAttribute("error", mensaje);
    }

    /**
     * Agrega mensaje de éxito flash
     * @param attributes redirect attributes
     * @param mensaje mensaje de éxito
     */
    public static void addSuccessMessage(RedirectAttributes attributes, String mensaje) {
        attributes.addFlashAttribute("success", mensaje);
    }
}
