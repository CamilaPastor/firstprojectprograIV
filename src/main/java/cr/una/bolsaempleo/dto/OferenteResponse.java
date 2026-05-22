package cr.una.bolsaempleo.dto;

import cr.una.bolsaempleo.model.Oferente;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OferenteResponse {
    private Integer idOferente;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String telefono;
    private String correo;
    private String residencia;
    private Boolean aprobado;
    private Boolean activo;
    private LocalDateTime fechaRegistro;

    public static OferenteResponse from(Oferente o) {
        return new OferenteResponse(
                o.getIdOferente(),
                o.getIdentificacion(),
                o.getNombre(),
                o.getApellido(),
                o.getNacionalidad(),
                o.getTelefono(),
                o.getCorreo(),
                o.getResidencia(),
                o.getAprobado(),
                o.getActivo(),
                o.getFechaRegistro()
        );
    }
}
