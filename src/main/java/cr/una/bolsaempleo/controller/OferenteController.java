package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Cv;
import cr.una.bolsaempleo.service.OferenteService;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/oferente")
@RequiredArgsConstructor
public class OferenteController {

    private final OferenteService oferenteService;
    private final CaracteristicaService caracteristicaService;

    /**
     * Verifica que el usuario sea oferente
     */
    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "oferente")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesión como oferente");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    /**
     * Dashboard de oferente
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        Object habilidades = oferenteService.habilidades(user.getId());
        Optional<?> cv = oferenteService.obtenerCv(user.getId());

        model.addAttribute("usuario", user);
        model.addAttribute("totalHabilidades", 
            habilidades instanceof List<?> ? ((List<?>) habilidades).size() : 0);
        model.addAttribute("tieneCv", cv.isPresent());

        return "oferente/dashboard";
    }

    /**
     * Ver mis habilidades
     */
    @GetMapping("/habilidades")
    public String habilidades(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        Object habilidades = oferenteService.habilidades(user.getId());
        List<Caracteristica> categorias = caracteristicaService.raices();

        model.addAttribute("usuario", user);
        model.addAttribute("habilidades", habilidades);
        model.addAttribute("categorias", categorias);

        return "oferente/habilidades";
    }

    /**
     * Formulario para agregar habilidad
     */
    @GetMapping("/agregar-habilidad")
    public String formAgregarHabilidad(@RequestParam(value = "idPadre", required = false) Integer idPadre,
                                      HttpSession session,
                                      Model model,
                                      RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<Caracteristica> caracteristicas;
        if (idPadre != null) {
            caracteristicas = caracteristicaService.hijos(idPadre);
        } else {
            caracteristicas = caracteristicaService.raices();
        }

        model.addAttribute("usuario", user);
        model.addAttribute("caracteristicas", caracteristicas);
        model.addAttribute("idPadre", idPadre);

        return "oferente/agregar-habilidad";
    }

    /**
     * Agregar habilidad
     */
    @PostMapping("/agregar-habilidad")
    public String agregarHabilidad(@RequestParam Integer idCaracteristica,
                                  @RequestParam Integer nivel,
                                  HttpSession session,
                                  RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            oferenteService.agregarHabilidad(user.getId(), idCaracteristica, nivel);
            SessionUtil.addSuccessMessage(attributes, "Habilidad agregada exitosamente");
            return "redirect:/oferente/habilidades";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/oferente/habilidades";
        }
    }

    /**
     * Eliminar habilidad
     */
    @PostMapping("/eliminar-habilidad")
    public String eliminarHabilidad(@RequestParam Integer idCaracteristica,
                                   HttpSession session,
                                   RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            oferenteService.eliminarHabilidad(user.getId(), idCaracteristica);
            SessionUtil.addSuccessMessage(attributes, "Habilidad eliminada");
            return "redirect:/oferente/habilidades";
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
            return "redirect:/oferente/habilidades";
        }
    }

    /**
     * Ver mi CV
     */
    @GetMapping("/mi-cv")
    public String miCv(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        Optional<Cv> cv = oferenteService.obtenerCv(user.getId());

        model.addAttribute("usuario", user);
        model.addAttribute("cv", cv.orElse(null));
        model.addAttribute("tieneCv", cv.isPresent());

        return "oferente/mi-cv";
    }

    /**
     * Formulario para subir CV
     */
    @GetMapping("/subir-cv")
    public String formSubirCv(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        model.addAttribute("usuario", user);
        return "oferente/subir-cv";
    }

    /**
     * Subir CV (archivo PDF)
     */
    @PostMapping("/subir-cv")
    public String subirCv(@RequestParam("archivo") MultipartFile archivo,
                         HttpSession session,
                         RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            if (archivo.isEmpty()) {
                SessionUtil.addErrorMessage(attributes, "Por favor selecciona un archivo");
                return "redirect:/oferente/subir-cv";
            }

            if (!archivo.getContentType().equals("application/pdf")) {
                SessionUtil.addErrorMessage(attributes, "Solo se aceptan archivos PDF");
                return "redirect:/oferente/subir-cv";
            }

            byte[] pdfBytes = archivo.getBytes();
            String nombreArchivo = archivo.getOriginalFilename();

            oferenteService.subirCv(user.getId(), pdfBytes, nombreArchivo);
            SessionUtil.addSuccessMessage(attributes, "CV subido exitosamente");
            return "redirect:/oferente/mi-cv";
        } catch (IOException e) {
            SessionUtil.addErrorMessage(attributes, "Error al subir el archivo: " + e.getMessage());
            return "redirect:/oferente/subir-cv";
        }
    }

    /**
     * Descargar CV de un oferente
     */
    @GetMapping("/descargar-cv/{idOferente}")
    public ResponseEntity<byte[]> descargarCv(@PathVariable Integer idOferente) {
        try {
            Optional<Cv> cv = oferenteService.obtenerCv(idOferente);

            if (cv.isEmpty() || cv.get().getArchivoPdf() == null) {
                return ResponseEntity.notFound().build();
            }

            Cv cvData = cv.get();
            byte[] contenido = cvData.getArchivoPdf();
            String nombreArchivo = cvData.getNombreArchivo();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(contenido);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
