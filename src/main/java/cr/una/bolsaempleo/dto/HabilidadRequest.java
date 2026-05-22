package cr.una.bolsaempleo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabilidadRequest {
    @NotNull
    private Integer idCaracteristica;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer nivel;
}
