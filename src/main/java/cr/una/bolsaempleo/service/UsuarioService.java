package cr.una.bolsaempleo.service;

import cr.una.bolsaempleo.model.Usuario;
import cr.una.bolsaempleo.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.debug("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene solo usuarios activos
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerActivos() {
        log.debug("Obteniendo usuarios activos");
        return usuarioRepository.findActivos();
    }

    /**
     * Obtiene un usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Long id) {
        log.debug("Obteniendo usuario con ID: {}", id);
        return usuarioRepository.findById(id);
    }

    /**
     * Obtiene un usuario por email
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorEmail(String email) {
        log.debug("Obteniendo usuario con email: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene un usuario por cédula
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorCedula(String cedula) {
        log.debug("Obteniendo usuario con cédula: {}", cedula);
        return usuarioRepository.findByCedula(cedula);
    }

    /**
     * Obtiene usuarios por tipo
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorTipo(Usuario.TipoUsuario tipoUsuario) {
        log.debug("Obteniendo usuarios del tipo: {}", tipoUsuario);
        return usuarioRepository.findByTipoUsuarioAndActivoTrue(tipoUsuario);
    }

    /**
     * Busca usuarios por nombre
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNombre(String nombre) {
        log.debug("Buscando usuarios con nombre: {}", nombre);
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Guarda o actualiza un usuario
     */
    public Usuario guardar(Usuario usuario) {
        log.info("Guardando usuario: {}", usuario.getEmail());
        
        // Encriptar contraseña si es nueva
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Guarda múltiples usuarios
     */
    public List<Usuario> guardarTodos(List<Usuario> usuarios) {
        log.info("Guardando {} usuarios", usuarios.size());
        usuarios.forEach(u -> {
            if (u.getPassword() != null && !u.getPassword().isEmpty()) {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
            }
        });
        return usuarioRepository.saveAll(usuarios);
    }

    /**
     * Elimina un usuario por ID
     */
    public void eliminarPorId(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        usuarioRepository.deleteById(id);
    }

    /**
     * Desactiva un usuario sin eliminarlo
     */
    public Usuario desactivar(Long id) {
        log.info("Desactivando usuario con ID: {}", id);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            u.setActivo(false);
            return usuarioRepository.save(u);
        }
        return null;
    }

    /**
     * Activa un usuario
     */
    public Usuario activar(Long id) {
        log.info("Activando usuario con ID: {}", id);
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario u = usuario.get();
            u.setActivo(true);
            return usuarioRepository.save(u);
        }
        return null;
    }

    /**
     * Verifica si el email ya existe
     */
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Cuenta usuarios activos
     */
    @Transactional(readOnly = true)
    public long contarActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    /**
     * Verifica si la contraseña es correcta
     */
    @Transactional(readOnly = true)
    public boolean verificarPassword(Usuario usuario, String passwordPlain) {
        return passwordEncoder.matches(passwordPlain, usuario.getPassword());
    }

}
