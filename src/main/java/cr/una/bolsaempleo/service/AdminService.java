package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Administrador;
import java.util.Optional;

public interface AdminService {

    
    Optional<Administrador> login(String identificacion, String password);

    
    Optional<Administrador> findById(Integer idAdmin);
}
