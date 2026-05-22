package cr.una.bolsaempleo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaracteristicaRequest {
    @NotBlank
    private String nombre;
    private String descripcion;
    private Integer idPadre;
}
