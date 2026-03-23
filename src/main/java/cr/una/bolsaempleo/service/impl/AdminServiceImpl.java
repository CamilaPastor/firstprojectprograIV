package cr.una.bolsaempleo.service.impl;

import cr.una.bolsaempleo.model.Administrador;
import cr.una.bolsaempleo.repository.AdministradorRepository;
import cr.una.bolsaempleo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Administrador> login(String identificacion, String password) {
        Optional<Administrador> admin = administradorRepository.findByIdentificacion(identificacion);

        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPasswordHash())) {
            return admin;
        }

        return Optional.empty();
    }

    @Override
    public Optional<Administrador> findById(Integer idAdmin) {
        return administradorRepository.findById(idAdmin);
    }
}
