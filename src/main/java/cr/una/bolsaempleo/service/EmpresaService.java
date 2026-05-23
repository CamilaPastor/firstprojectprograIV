package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Empresa;
import java.util.List;
import java.util.Optional;

public interface EmpresaService {

    Empresa registrar(Empresa empresa);

    Optional<Empresa> loginPorCorreo(String correo, String password);

    List<Empresa> pendientes();

    Empresa aprobar(Integer idEmpresa);
}
