package cr.una.bolsaempleo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidatoResponse {
    private Integer idOferente;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String correo;
    private Integer cumplidos;
    private Integer total;
    private Double porcentaje;

    public static CandidatoResponse from(ResultadoCandidato r) {
        return new CandidatoResponse(
                r.getOferente().getIdOferente(),
                r.getOferente().getIdentificacion(),
                r.getOferente().getNombre(),
                r.getOferente().getApellido(),
                r.getOferente().getCorreo(),
                r.getCumplidos(),
                r.getTotal(),
                r.getPorcentaje()
        );
    }
}
