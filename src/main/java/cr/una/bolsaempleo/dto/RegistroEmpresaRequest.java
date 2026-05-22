package cr.una.bolsaempleo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroEmpresaRequest {
    @NotBlank
    private String nombre;
    private String localizacion;
    @NotBlank
    @Email
    private String correo;
    private String telefono;
    private String descripcion;
    @NotBlank
    @Size(min = 4)
    private String password;
}
