package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.Cv;
import cr.una.bolsaempleo.model.OferenteCaracteristica;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.repository.OferenteRepository;
import cr.una.bolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.una.bolsaempleo.repository.CvRepository;
import cr.una.bolsaempleo.repository.CaracteristicaRepository;
import cr.una.bolsaempleo.service.OferenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OferenteServiceImpl implements OferenteService {

    private final OferenteRepository oferenteRepository;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepository;
    private final CvRepository cvRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Oferente registrar(Oferente oferente) {
        if (oferenteRepository.existsByCorreo(oferente.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (oferenteRepository.existsByIdentificacion(oferente.getIdentificacion())) {
            throw new IllegalArgumentException("La identificación ya está registrada");
        }

        // Hashear la contraseña
        if (oferente.getPasswordSinHashear() != null && !oferente.getPasswordSinHashear().isEmpty()) {
            oferente.setPasswordHash(passwordEncoder.encode(oferente.getPasswordSinHashear()));
        }

        oferente.setAprobado(false);
        oferente.setActivo(true);

        return oferenteRepository.save(oferente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Oferente> loginPorCorreo(String correo, String password) {
        Optional<Oferente> oferente = oferenteRepository.findByCorreo(correo);

        if (oferente.isPresent() && passwordEncoder.matches(password, oferente.get().getPasswordHash())) {
            return oferente;
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Oferente> pendientes() {
        return oferenteRepository.findByAprobadoFalse();
    }

    @Override
    public Oferente aprobar(Integer idOferente) {
        Oferente oferente = oferenteRepository.findById(idOferente)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));

        oferente.setAprobado(true);
        return oferenteRepository.save(oferente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Oferente> findById(Integer idOferente) {
        return oferenteRepository.findById(idOferente);
    }

    @Override
    @Transactional(readOnly = true)
    public Object habilidades(Integer idOferente) {
        return oferenteCaracteristicaRepository.findByOferente_IdOferente(idOferente);
    }

    @Override
    public Object agregarHabilidad(Integer idOferente, Integer idCaracteristica, Integer nivel) {
        Oferente oferente = oferenteRepository.findById(idOferente)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));

        Caracteristica caracteristica = caracteristicaRepository.findById(idCaracteristica)
                .orElseThrow(() -> new IllegalArgumentException("Característica no encontrada"));

        if (oferenteCaracteristicaRepository.existsByOferente_IdOferenteAndCaracteristica_IdCaracteristica(idOferente, idCaracteristica)) {
            throw new IllegalArgumentException("El oferente ya tiene esta característica");
        }

        OferenteCaracteristica oc = new OferenteCaracteristica();
        oc.setOferente(oferente);
        oc.setCaracteristica(caracteristica);
        oc.setNivel(nivel);

        return oferenteCaracteristicaRepository.save(oc);
    }

    @Override
    public void eliminarHabilidad(Integer idOferente, Integer idCaracteristica) {
        OferenteCaracteristica oc = oferenteCaracteristicaRepository
                .findByOferenteAndCaracteristica(idOferente, idCaracteristica);

        if (oc == null) {
            throw new IllegalArgumentException("Habilidad no encontrada para este oferente");
        }

        oferenteCaracteristicaRepository.delete(oc);
    }

    @Override
    public Cv subirCv(Integer idOferente, byte[] archivoPdf, String nombreArchivo) {
        Oferente oferente = oferenteRepository.findById(idOferente)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));

        Cv cv = cvRepository.findByOferente_IdOferente(idOferente)
                .orElse(new Cv());

        cv.setOferente(oferente);
        cv.setArchivoPdf(archivoPdf);
        cv.setNombreArchivo(nombreArchivo);

        return cvRepository.save(cv);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cv> obtenerCv(Integer idOferente) {
        return cvRepository.findByOferente_IdOferente(idOferente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Oferente> obtenerActivos() {
        return oferenteRepository.findAllActivos();
    }

    @Override
    public Oferente actualizar(Oferente oferente) {
        Oferente existente = oferenteRepository.findById(oferente.getIdOferente())
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));

        existente.setNombre(oferente.getNombre());
        existente.setApellido(oferente.getApellido());
        existente.setNacionalidad(oferente.getNacionalidad());
        existente.setTelefono(oferente.getTelefono());
        existente.setResidencia(oferente.getResidencia());

        // Si se proporciona nueva contraseña
        if (oferente.getPasswordSinHashear() != null && !oferente.getPasswordSinHashear().isEmpty()) {
            existente.setPasswordHash(passwordEncoder.encode(oferente.getPasswordSinHashear()));
        }

        return oferenteRepository.save(existente);
    }

    @Override
    public void desactivar(Integer idOferente) {
        Oferente oferente = oferenteRepository.findById(idOferente)
                .orElseThrow(() -> new IllegalArgumentException("Oferente no encontrado"));

        oferente.setActivo(false);
        oferenteRepository.save(oferente);
    }
}
