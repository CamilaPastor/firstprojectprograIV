package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.SessionUser;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Cv;
import cr.una.bolsaempleo.model.OferenteCaracteristica;
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

    private SessionUser verificarSesion(HttpSession session, RedirectAttributes attributes) {
        if (!SessionUtil.isTipo(session, "oferente")) {
            SessionUtil.addErrorMessage(attributes, "Debes iniciar sesion como oferente");
            return null;
        }
        return SessionUtil.getSessionUser(session);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        Object habilidades = oferenteService.habilidades(user.getId());
        Optional<Cv> cv = oferenteService.obtenerCv(user.getId());

        model.addAttribute("usuario", user);
        model.addAttribute("totalHabilidades",
            habilidades instanceof List<?> ? ((List<?>) habilidades).size() : 0);
        model.addAttribute("tieneCv", cv.isPresent());
        return "oferente/dashboard";
    }

    @GetMapping("/habilidades")
    public String habilidades(@RequestParam(value = "actualId", required = false) Integer actualId,
                              HttpSession session, Model model, RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        List<OferenteCaracteristica> habilidades = (List<OferenteCaracteristica>) oferenteService.habilidades(user.getId());
        List<Caracteristica> raices = caracteristicaService.raices();

        List<Caracteristica> subcategorias = null;
        Caracteristica categoriaActual = null;

        if (actualId != null) {
            Optional<Caracteristica> cat = caracteristicaService.findById(actualId);
            if (cat.isPresent()) {
                categoriaActual = cat.get();
                subcategorias = caracteristicaService.hijos(actualId);
            }
        }

        List<Caracteristica> visibles = (subcategorias != null) ? subcategorias : raices;

        model.addAttribute("usuario", user);
        model.addAttribute("habilidades", habilidades);
        model.addAttribute("categorias", visibles);
        model.addAttribute("categoriaActual", categoriaActual);
        model.addAttribute("actualId", actualId);
        model.addAttribute("todas", caracteristicaService.todas());
        return "oferente/habilidades";
    }

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
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }
        return "redirect:/oferente/habilidades";
    }

    @PostMapping("/eliminar-habilidad")
    public String eliminarHabilidad(@RequestParam Integer idCaracteristica,
                                    HttpSession session,
                                    RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            oferenteService.eliminarHabilidad(user.getId(), idCaracteristica);
            SessionUtil.addSuccessMessage(attributes, "Habilidad eliminada");
        } catch (Exception e) {
            SessionUtil.addErrorMessage(attributes, "Error: " + e.getMessage());
        }
        return "redirect:/oferente/habilidades";
    }

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

    @PostMapping("/subir-cv")
    public String subirCv(@RequestParam("archivo") MultipartFile archivo,
                          HttpSession session,
                          RedirectAttributes attributes) {
        SessionUser user = verificarSesion(session, attributes);
        if (user == null) return "redirect:/login";

        try {
            if (archivo.isEmpty()) {
                SessionUtil.addErrorMessage(attributes, "Por favor selecciona un archivo");
                return "redirect:/oferente/mi-cv";
            }
            String contentType = archivo.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                SessionUtil.addErrorMessage(attributes, "Solo se aceptan archivos PDF");
                return "redirect:/oferente/mi-cv";
            }

            oferenteService.subirCv(user.getId(), archivo.getBytes(), archivo.getOriginalFilename());
            SessionUtil.addSuccessMessage(attributes, "CV subido exitosamente");
            return "redirect:/oferente/mi-cv";
        } catch (IOException e) {
            SessionUtil.addErrorMessage(attributes, "Error al subir el archivo: " + e.getMessage());
            return "redirect:/oferente/mi-cv";
        }
    }

    @GetMapping("/descargar-cv/{idOferente}")
    public ResponseEntity<byte[]> descargarCv(@PathVariable Integer idOferente) {
        try {
            Optional<Cv> cv = oferenteService.obtenerCv(idOferente);
            if (cv.isEmpty() || cv.get().getArchivoPdf() == null) {
                return ResponseEntity.notFound().build();
            }

            Cv cvData = cv.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cvData.getNombreArchivo() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .body(cvData.getArchivoPdf());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
