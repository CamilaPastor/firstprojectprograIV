package cr.una.bolsaempleo.config;

import cr.una.bolsaempleo.model.Administrador;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.repository.AdministradorRepository;
import cr.una.bolsaempleo.repository.EmpresaRepository;
import cr.una.bolsaempleo.repository.OferenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdministradorRepository adminRepo;
    private final EmpresaRepository empresaRepo;
    private final OferenteRepository oferenteRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("=== DataInitializer: verificando datos de prueba ===");

        if (!adminRepo.existsByIdentificacion("admin")) {
            Administrador admin = new Administrador();
            admin.setIdentificacion("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setActivo(true);
            adminRepo.save(admin);
            log.info("✓ Admin creado: identificacion='admin', password='admin123'");
        } else {

            adminRepo.findByIdentificacion("admin").ifPresent(a -> {
                a.setPasswordHash(passwordEncoder.encode("admin123"));
                a.setActivo(true);
                adminRepo.save(a);
                log.info("✓ Admin existente actualizado: identificacion='admin'");
            });
        }

        crearEmpresaSiNoExiste(
                "TechSolutions S.A.",
                "San José, Costa Rica",
                "info@techsolutions.cr",
                "+506-2234-5678",
                "Empresa de consultoría en tecnología de la información"
        );
        crearEmpresaSiNoExiste(
                "Innovate Digital",
                "San José, Costa Rica",
                "contact@innovate.cr",
                "+506-8765-4321",
                "Desarrolladora de soluciones web y móviles"
        );
        crearEmpresaSiNoExiste(
                "CloudServices Inc",
                "Heredia, Costa Rica",
                "support@cloudservices.cr",
                "+506-2345-6789",
                "Proveedor de servicios en la nube"
        );

        crearOferenteSiNoExiste("118456789", "Juan",   "Pérez López",       "Costarricense", "+506-8765-4321", "juan.perez@gmail.com",    "San José");
        crearOferenteSiNoExiste("117234567", "María",  "González Rodríguez","Costarricense", "+506-8234-5678", "maria.gonzalez@gmail.com", "Heredia");
        crearOferenteSiNoExiste("119876543", "Carlos", "Ramírez Méndez",    "Nicaragüense",  "+506-8987-6543", "carlos.ramirez@gmail.com", "San Pedro");

        log.info("=== DataInitializer: completado ===");
    }

    private void crearEmpresaSiNoExiste(String nombre, String localizacion,
                                        String correo, String telefono, String descripcion) {
        if (!empresaRepo.existsByCorreo(correo)) {
            Empresa e = new Empresa();
            e.setNombre(nombre);
            e.setLocalizacion(localizacion);
            e.setCorreo(correo);
            e.setTelefono(telefono);
            e.setDescripcion(descripcion);
            e.setPasswordHash(passwordEncoder.encode("empresa123"));
            e.setAprobado(true);
            e.setActivo(true);
            empresaRepo.save(e);
            log.info("✓ Empresa creada: {} ({})", nombre, correo);
        } else {
            empresaRepo.findByCorreo(correo).ifPresent(em -> {
                em.setPasswordHash(passwordEncoder.encode("empresa123"));
                em.setAprobado(true);
                em.setActivo(true);
                empresaRepo.save(em);
                log.info("✓ Empresa existente actualizada: {}", correo);
            });
        }
    }

    private void crearOferenteSiNoExiste(String identificacion, String nombre, String apellido,
                                          String nacionalidad, String telefono,
                                          String correo, String residencia) {
        if (!oferenteRepo.existsByIdentificacion(identificacion)) {
            Oferente o = new Oferente();
            o.setIdentificacion(identificacion);
            o.setNombre(nombre);
            o.setApellido(apellido);
            o.setNacionalidad(nacionalidad);
            o.setTelefono(telefono);
            o.setCorreo(correo);
            o.setResidencia(residencia);
            o.setPasswordHash(passwordEncoder.encode("oferente123"));
            o.setAprobado(true);
            o.setActivo(true);
            oferenteRepo.save(o);
            log.info("✓ Oferente creado: {} {} ({})", nombre, apellido, correo);
        } else {
            oferenteRepo.findByIdentificacion(identificacion).ifPresent(of -> {
                of.setPasswordHash(passwordEncoder.encode("oferente123"));
                of.setAprobado(true);
                of.setActivo(true);
                oferenteRepo.save(of);
                log.info("✓ Oferente existente actualizado: {}", correo);
            });
        }
    }
}
