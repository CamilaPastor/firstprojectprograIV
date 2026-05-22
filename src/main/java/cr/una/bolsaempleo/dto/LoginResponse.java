package cr.una.bolsaempleo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer id;
    private String nombre;
    private String correo;
    private String role;
}
