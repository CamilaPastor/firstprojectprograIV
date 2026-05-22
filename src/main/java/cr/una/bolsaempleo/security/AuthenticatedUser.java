package cr.una.bolsaempleo.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticatedUser {
    private Integer id;
    private String nombre;
    private String correo;
    private String role;
}
