package cr.una.bolsaempleo.dto;

import cr.una.bolsaempleo.model.Empresa;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmpresaResponse {
    private Integer idEmpresa;
    private String nombre;
    private String localizacion;
    private String correo;
    private String telefono;
    private String descripcion;
    private Boolean aprobado;
    private Boolean activo;
    private LocalDateTime fechaRegistro;

    public static EmpresaResponse from(Empresa e) {
        return new EmpresaResponse(
                e.getIdEmpresa(),
                e.getNombre(),
                e.getLocalizacion(),
                e.getCorreo(),
                e.getTelefono(),
                e.getDescripcion(),
                e.getAprobado(),
                e.getActivo(),
                e.getFechaRegistro()
        );
    }
}
