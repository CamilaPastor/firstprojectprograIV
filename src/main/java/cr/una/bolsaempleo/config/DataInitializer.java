package cr.una.bolsaempleo.config;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import cr.una.bolsaempleo.model.Administrador;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Cv;
import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.OferenteCaracteristica;
import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.model.PuestoCaracteristica;
import cr.una.bolsaempleo.repository.AdministradorRepository;
import cr.una.bolsaempleo.repository.CaracteristicaRepository;
import cr.una.bolsaempleo.repository.CvRepository;
import cr.una.bolsaempleo.repository.EmpresaRepository;
import cr.una.bolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.una.bolsaempleo.repository.OferenteRepository;
import cr.una.bolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.una.bolsaempleo.repository.PuestoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdministradorRepository adminRepo;
    private final EmpresaRepository empresaRepo;
    private final OferenteRepository oferenteRepo;
    private final CaracteristicaRepository caracteristicaRepo;
    private final PuestoRepository puestoRepo;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepo;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepo;
    private final CvRepository cvRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("=== DataInitializer: verificando datos de prueba ===");

        seedAdmin();
        seedEmpresas();
        seedOferentes();
        seedCaracteristicas();
        seedPuestos();
        seedHabilidades();
        seedCvs();

        log.info("=== DataInitializer: completado ===");
    }

    private void seedAdmin() {
        if (!adminRepo.existsByIdentificacion("admin")) {
            Administrador admin = new Administrador();
            admin.setIdentificacion("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setActivo(true);
            adminRepo.save(admin);
        }
    }

    private void seedEmpresas() {
        crearEmpresa("TechSolutions S.A.", "San José, Costa Rica", "info@techsolutions.cr",
                "+506-2234-5678", "Empresa de consultoría en tecnología de la información", true, true);
        crearEmpresa("Innovate Digital", "San José, Costa Rica", "contact@innovate.cr",
                "+506-8765-4321", "Desarrolladora de soluciones web y móviles", true, true);
        crearEmpresa("CloudServices Inc", "Heredia, Costa Rica", "support@cloudservices.cr",
                "+506-2345-6789", "Proveedor de servicios en la nube", true, true);
        crearEmpresa("DataCorp Analytics", "Cartago, Costa Rica", "hr@datacorp.cr",
                "+506-2456-7890", "Consultora de análisis de datos e inteligencia de negocios", false, true);
        crearEmpresa("StartUp Labs", "Alajuela, Costa Rica", "hola@startuplabs.cr",
                "+506-2567-8901", "Incubadora de startups tecnológicas", false, true);
    }

    private void seedOferentes() {
        crearOferente("118456789", "Juan", "Pérez López", "Costarricense", "+506-8765-4321",
                "juan.perez@gmail.com", "San José", true);
        crearOferente("117234567", "María", "González Rodríguez", "Costarricense", "+506-8234-5678",
                "maria.gonzalez@gmail.com", "Heredia", true);
        crearOferente("119876543", "Carlos", "Ramírez Méndez", "Nicaragüense", "+506-8987-6543",
                "carlos.ramirez@gmail.com", "San Pedro", true);
        crearOferente("116543210", "Ana", "Vargas Solano", "Costarricense", "+506-8123-4567",
                "ana.vargas@gmail.com", "Curridabat", true);
        crearOferente("115678901", "Luis", "Castro Jiménez", "Costarricense", "+506-8456-7890",
                "luis.castro@gmail.com", "Alajuela", true);
        crearOferente("114321098", "Sofía", "Mora Chacón", "Costarricense", "+506-8678-9012",
                "sofia.mora@gmail.com", "Cartago", false);
        crearOferente("113210987", "Diego", "Hernández Rojas", "Panameño", "+506-8890-1234",
                "diego.hernandez@gmail.com", "Escazú", false);
    }

    private void seedCaracteristicas() {
        if (caracteristicaRepo.count() > 0) return;

        Caracteristica lenguajes = crearRaiz("Lenguajes de Programación", "Lenguajes de programación de software");
        Caracteristica bases = crearRaiz("Bases de Datos", "Motores y lenguajes de bases de datos");
        Caracteristica frameworks = crearRaiz("Frameworks y Tecnologías", "Frameworks, librerías y tecnologías");
        Caracteristica blandas = crearRaiz("Habilidades Blandas", "Competencias interpersonales y de gestión");

        crearHijo("Java", "Lenguaje de programación Java", lenguajes);
        crearHijo("Python", "Lenguaje de programación Python", lenguajes);
        crearHijo("JavaScript", "Lenguaje de programación JavaScript/TypeScript", lenguajes);
        crearHijo("C#", "Lenguaje de programación C#", lenguajes);
        crearHijo("PHP", "Lenguaje de programación PHP", lenguajes);

        crearHijo("MySQL", "Sistema de gestión de bases de datos MySQL", bases);
        crearHijo("PostgreSQL", "Sistema de gestión de bases de datos PostgreSQL", bases);
        crearHijo("MongoDB", "Base de datos NoSQL MongoDB", bases);
        crearHijo("Oracle", "Sistema de gestión de bases de datos Oracle", bases);

        crearHijo("Spring Boot", "Framework Java para desarrollo web", frameworks);
        crearHijo("React", "Librería JavaScript para interfaces de usuario", frameworks);
        crearHijo("Angular", "Framework TypeScript para aplicaciones web", frameworks);
        crearHijo("Docker", "Plataforma de contenedores", frameworks);

        crearHijo("Trabajo en Equipo", "Capacidad de colaborar en equipo", blandas);
        crearHijo("Comunicación", "Habilidades de comunicación efectiva", blandas);
        crearHijo("Resolución de Problemas", "Capacidad analítica y resolución de problemas", blandas);
    }

    private void seedPuestos() {
        if (puestoRepo.count() > 0) return;

        crearPuesto("info@techsolutions.cr", "Desarrollador Backend Java Senior\nDiseño y desarrollo de servicios REST y microservicios.",
                new BigDecimal("1500000"), "publico", true,
                car("Java", 4), car("Spring Boot", 4), car("PostgreSQL", 3), car("Trabajo en Equipo", 3));
        crearPuesto("info@techsolutions.cr", "Ingeniero de Datos\nConstrucción de pipelines de datos y modelado analítico.",
                new BigDecimal("1300000"), "privado", true,
                car("Python", 4), car("PostgreSQL", 4), car("MongoDB", 3));
        crearPuesto("info@techsolutions.cr", "Practicante de QA\nApoyo en pruebas manuales y automatizadas.",
                new BigDecimal("450000"), "publico", false,
                car("Java", 2), car("Resolución de Problemas", 3));

        crearPuesto("contact@innovate.cr", "Desarrollador Frontend React\nDesarrollo de interfaces modernas y responsivas.",
                new BigDecimal("1200000"), "publico", true,
                car("JavaScript", 4), car("React", 4), car("Comunicación", 3));
        crearPuesto("contact@innovate.cr", "Full Stack Developer\nDesarrollo de extremo a extremo en proyectos web.",
                new BigDecimal("1400000"), "privado", true,
                car("Java", 3), car("React", 3), car("Spring Boot", 3), car("Trabajo en Equipo", 4));
        crearPuesto("contact@innovate.cr", "Diseñador UI\nDiseño de componentes e interacción de usuario.",
                new BigDecimal("900000"), "publico", false,
                car("JavaScript", 2), car("Comunicación", 4));

        crearPuesto("support@cloudservices.cr", "DevOps Engineer\nAutomatización de despliegues e infraestructura como código.",
                new BigDecimal("1600000"), "publico", true,
                car("Docker", 4), car("PostgreSQL", 3), car("Python", 3));
        crearPuesto("support@cloudservices.cr", "Administrador de Base de Datos\nGestión, tuning y respaldo de bases de datos.",
                new BigDecimal("1350000"), "privado", true,
                car("PostgreSQL", 5), car("MySQL", 3), car("Oracle", 3));
        crearPuesto("support@cloudservices.cr", "Arquitecto de Soluciones Cloud\nDiseño de arquitecturas escalables en la nube.",
                new BigDecimal("2000000"), "publico", true,
                car("Docker", 5), car("Spring Boot", 4), car("Trabajo en Equipo", 5), car("Comunicación", 4));
    }

    private void seedHabilidades() {
        if (oferenteCaracteristicaRepo.count() > 0) return;

        agregarHabilidades("118456789", car("Java", 5), car("Spring Boot", 4), car("PostgreSQL", 4), car("Docker", 3), car("Trabajo en Equipo", 4));
        agregarHabilidades("117234567", car("JavaScript", 5), car("React", 5), car("Comunicación", 4), car("Trabajo en Equipo", 3));
        agregarHabilidades("119876543", car("Python", 4), car("PostgreSQL", 5), car("MongoDB", 3), car("Docker", 4));
        agregarHabilidades("116543210", car("Java", 3), car("React", 4), car("Spring Boot", 3), car("Trabajo en Equipo", 4));
        agregarHabilidades("115678901", car("Docker", 5), car("PostgreSQL", 4), car("Python", 4), car("Comunicación", 3));
    }

    private void seedCvs() {
        if (cvRepo.count() > 0) return;
        crearCv("118456789", "cv-juan-perez.pdf", "Juan Pérez López");
        crearCv("117234567", "cv-maria-gonzalez.pdf", "María González Rodríguez");
    }

    private void crearEmpresa(String nombre, String localizacion, String correo,
                              String telefono, String descripcion, boolean aprobado, boolean activo) {
        if (empresaRepo.existsByCorreo(correo)) return;
        Empresa e = new Empresa();
        e.setNombre(nombre);
        e.setLocalizacion(localizacion);
        e.setCorreo(correo);
        e.setTelefono(telefono);
        e.setDescripcion(descripcion);
        e.setPasswordHash(passwordEncoder.encode("empresa123"));
        e.setAprobado(aprobado);
        e.setActivo(activo);
        empresaRepo.save(e);
    }

    private void crearOferente(String identificacion, String nombre, String apellido,
                               String nacionalidad, String telefono, String correo,
                               String residencia, boolean aprobado) {
        if (oferenteRepo.existsByIdentificacion(identificacion)) return;
        Oferente o = new Oferente();
        o.setIdentificacion(identificacion);
        o.setNombre(nombre);
        o.setApellido(apellido);
        o.setNacionalidad(nacionalidad);
        o.setTelefono(telefono);
        o.setCorreo(correo);
        o.setResidencia(residencia);
        o.setPasswordHash(passwordEncoder.encode("oferente123"));
        o.setAprobado(aprobado);
        o.setActivo(true);
        oferenteRepo.save(o);
    }

    private Caracteristica crearRaiz(String nombre, String descripcion) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setNivelMinimo(1);
        c.setNivelMaximo(5);
        c.setActivo(true);
        return caracteristicaRepo.save(c);
    }

    private void crearHijo(String nombre, String descripcion, Caracteristica padre) {
        Caracteristica c = new Caracteristica();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setPadre(padre);
        c.setNivelMinimo(1);
        c.setNivelMaximo(5);
        c.setActivo(true);
        caracteristicaRepo.save(c);
    }

    private void crearPuesto(String correoEmpresa, String descripcion, BigDecimal salario,
                             String tipoPublicacion, boolean activo, NivelCaracteristica... requeridas) {
        Empresa empresa = empresaRepo.findByCorreo(correoEmpresa).orElse(null);
        if (empresa == null) return;

        Puesto puesto = new Puesto();
        puesto.setEmpresa(empresa);
        puesto.setDescripcion(descripcion);
        puesto.setSalario(salario);
        puesto.setTipoPublicacion(tipoPublicacion);
        puesto.setActivo(activo);
        Puesto guardado = puestoRepo.save(puesto);

        for (NivelCaracteristica nc : requeridas) {
            Caracteristica caracteristica = caracteristicaRepo.findByNombre(nc.nombre).orElse(null);
            if (caracteristica == null) continue;
            PuestoCaracteristica pc = new PuestoCaracteristica();
            pc.setPuesto(guardado);
            pc.setCaracteristica(caracteristica);
            pc.setNivelRequerido(nc.nivel);
            puestoCaracteristicaRepo.save(pc);
        }
    }

    private void agregarHabilidades(String identificacion, NivelCaracteristica... habilidades) {
        Oferente oferente = oferenteRepo.findByIdentificacion(identificacion).orElse(null);
        if (oferente == null) return;
        for (NivelCaracteristica nc : habilidades) {
            Caracteristica caracteristica = caracteristicaRepo.findByNombre(nc.nombre).orElse(null);
            if (caracteristica == null) continue;
            OferenteCaracteristica oc = new OferenteCaracteristica();
            oc.setOferente(oferente);
            oc.setCaracteristica(caracteristica);
            oc.setNivel(nc.nivel);
            oferenteCaracteristicaRepo.save(oc);
        }
    }

    private void crearCv(String identificacion, String nombreArchivo, String nombreCompleto) {
        Oferente oferente = oferenteRepo.findByIdentificacion(identificacion).orElse(null);
        if (oferente == null) return;
        Cv cv = new Cv();
        cv.setOferente(oferente);
        cv.setNombreArchivo(nombreArchivo);
        cv.setArchivoPdf(generarPdf(nombreCompleto));
        cvRepo.save(cv);
    }

    private byte[] generarPdf(String nombreCompleto) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdf);
        doc.add(new Paragraph("Curriculum Vitae"));
        doc.add(new Paragraph(nombreCompleto));
        doc.add(new Paragraph("Documento de prueba generado automáticamente."));
        doc.close();
        return baos.toByteArray();
    }

    private static NivelCaracteristica car(String nombre, int nivel) {
        return new NivelCaracteristica(nombre, nivel);
    }

    private record NivelCaracteristica(String nombre, int nivel) {
    }
}
