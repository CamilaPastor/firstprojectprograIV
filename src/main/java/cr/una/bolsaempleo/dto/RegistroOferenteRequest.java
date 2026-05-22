package cr.una.bolsaempleo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroOferenteRequest {
    @NotBlank
    private String identificacion;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    private String nacionalidad;
    private String telefono;
    @NotBlank
    @Email
    private String correo;
    private String residencia;
    @NotBlank
    @Size(min = 4)
    private String password;
}
