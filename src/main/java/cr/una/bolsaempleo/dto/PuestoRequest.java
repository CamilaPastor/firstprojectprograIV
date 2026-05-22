package cr.una.bolsaempleo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PuestoRequest {
    @NotBlank
    private String descripcion;
    private BigDecimal salario;
    private String tipoPublicacion;
    private List<CaracteristicaPuesto> caracteristicas;

    @Data
    public static class CaracteristicaPuesto {
        private Integer idCaracteristica;
        private Integer nivelRequerido;
    }
}
