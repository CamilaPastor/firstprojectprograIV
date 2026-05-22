package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.*;
import cr.una.bolsaempleo.model.Administrador;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.security.JwtUtil;
import cr.una.bolsaempleo.service.AdminService;
import cr.una.bolsaempleo.service.EmpresaService;
import cr.una.bolsaempleo.service.OferenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmpresaService empresaService;
    private final OferenteService oferenteService;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        Optional<Administrador> admin = adminService.login(req.getUsuario(), req.getPassword());
        if (admin.isPresent()) {
            Administrador a = admin.get();
            String token = jwtUtil.generate(a.getIdAdmin(), a.getIdentificacion(), "admin", "Admin", a.getIdentificacion());
            return ResponseEntity.ok(new LoginResponse(token, a.getIdAdmin(), "Admin", a.getIdentificacion(), "admin"));
        }

        Optional<Empresa> empresa = empresaService.loginPorCorreo(req.getUsuario(), req.getPassword());
        if (empresa.isPresent()) {
            Empresa e = empresa.get();
            if (!e.getAprobado()) {
                return ResponseEntity.status(403).body(MessageResponse.of("Tu cuenta de empresa aun no ha sido aprobada por el administrador."));
            }
            String token = jwtUtil.generate(e.getIdEmpresa(), e.getCorreo(), "empresa", e.getNombre(), e.getCorreo());
            return ResponseEntity.ok(new LoginResponse(token, e.getIdEmpresa(), e.getNombre(), e.getCorreo(), "empresa"));
        }

        Optional<Oferente> oferente = oferenteService.loginPorCorreo(req.getUsuario(), req.getPassword());
        if (oferente.isPresent()) {
            Oferente o = oferente.get();
            if (!o.getAprobado()) {
                return ResponseEntity.status(403).body(MessageResponse.of("Tu cuenta de oferente aun no ha sido aprobada por el administrador."));
            }
            String nombreCompleto = o.getNombre() + " " + o.getApellido();
            String token = jwtUtil.generate(o.getIdOferente(), o.getCorreo(), "oferente", nombreCompleto, o.getCorreo());
            return ResponseEntity.ok(new LoginResponse(token, o.getIdOferente(), nombreCompleto, o.getCorreo(), "oferente"));
        }

        return ResponseEntity.status(401).body(MessageResponse.of("Usuario o contraseña incorrectos."));
    }

    @PostMapping("/registro/empresa")
    public ResponseEntity<?> registroEmpresa(@Valid @RequestBody RegistroEmpresaRequest req) {
        try {
            Empresa empresa = new Empresa();
            empresa.setNombre(req.getNombre());
            empresa.setLocalizacion(req.getLocalizacion());
            empresa.setCorreo(req.getCorreo());
            empresa.setTelefono(req.getTelefono());
            empresa.setDescripcion(req.getDescripcion());
            empresa.setPasswordSinHashear(req.getPassword());
            empresaService.registrar(empresa);
            return ResponseEntity.ok(MessageResponse.of("Empresa registrada exitosamente. Espera la aprobacion del administrador."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }

    @PostMapping("/registro/oferente")
    public ResponseEntity<?> registroOferente(@Valid @RequestBody RegistroOferenteRequest req) {
        try {
            Oferente oferente = new Oferente();
            oferente.setIdentificacion(req.getIdentificacion());
            oferente.setNombre(req.getNombre());
            oferente.setApellido(req.getApellido());
            oferente.setNacionalidad(req.getNacionalidad());
            oferente.setTelefono(req.getTelefono());
            oferente.setCorreo(req.getCorreo());
            oferente.setResidencia(req.getResidencia());
            oferente.setPasswordSinHashear(req.getPassword());
            oferenteService.registrar(oferente);
            return ResponseEntity.ok(MessageResponse.of("Oferente registrado exitosamente. Espera la aprobacion del administrador."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(MessageResponse.of(e.getMessage()));
        }
    }
}
