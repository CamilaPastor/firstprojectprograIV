package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Oferente;
import cr.una.bolsaempleo.model.Cv;
import java.util.List;
import java.util.Optional;

public interface OferenteService {

    
    Oferente registrar(Oferente oferente);

    
    Optional<Oferente> loginPorCorreo(String correo, String password);

    
    List<Oferente> pendientes();

    
    Oferente aprobar(Integer idOferente);

    
    Optional<Oferente> findById(Integer idOferente);

    
    Object habilidades(Integer idOferente);

    
    Object agregarHabilidad(Integer idOferente, Integer idCaracteristica, Integer nivel);

    
    void eliminarHabilidad(Integer idOferente, Integer idCaracteristica);

    
    Cv subirCv(Integer idOferente, byte[] archivoPdf, String nombreArchivo);

    
    Optional<Cv> obtenerCv(Integer idOferente);

    
    List<Oferente> obtenerActivos();

    
    Oferente actualizar(Oferente oferente);

    
    void desactivar(Integer idOferente);
}
