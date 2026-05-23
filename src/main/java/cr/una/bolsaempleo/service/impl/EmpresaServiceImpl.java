package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Empresa;
import cr.una.bolsaempleo.repository.EmpresaRepository;
import cr.una.bolsaempleo.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Empresa registrar(Empresa empresa) {
        if (empresaRepository.existsByCorreo(empresa.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (empresa.getPasswordSinHashear() != null && !empresa.getPasswordSinHashear().isEmpty()) {
            empresa.setPasswordHash(passwordEncoder.encode(empresa.getPasswordSinHashear()));
        }

        empresa.setAprobado(false);
        empresa.setActivo(true);

        return empresaRepository.save(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empresa> loginPorCorreo(String correo, String password) {
        Optional<Empresa> empresa = empresaRepository.findByCorreo(correo);

        if (empresa.isPresent() && passwordEncoder.matches(password, empresa.get().getPasswordHash())) {
            return empresa;
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> pendientes() {
        return empresaRepository.findByAprobadoFalse();
    }

    @Override
    public Empresa aprobar(Integer idEmpresa) {
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));

        empresa.setAprobado(true);
        return empresaRepository.save(empresa);
    }
}
