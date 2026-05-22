package cr.una.bolsaempleo.dto;

import cr.una.bolsaempleo.model.Puesto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PuestoResponse {
    private Integer idPuesto;
    private String descripcion;
    private BigDecimal salario;
    private String tipoPublicacion;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private Integer idEmpresa;
    private String nombreEmpresa;
    private List<CaracteristicaPuestoResponse> caracteristicas;

    @Data
    @AllArgsConstructor
    public static class CaracteristicaPuestoResponse {
        private Integer idCaracteristica;
        private String nombre;
        private Integer nivelRequerido;
    }

    public static PuestoResponse from(Puesto p) {
        List<CaracteristicaPuestoResponse> cs = p.getCaracteristicasRequeridas() == null
                ? List.of()
                : p.getCaracteristicasRequeridas().stream()
                    .map(pc -> new CaracteristicaPuestoResponse(
                            pc.getCaracteristica().getIdCaracteristica(),
                            pc.getCaracteristica().getNombre(),
                            pc.getNivelRequerido()))
                    .toList();
        return new PuestoResponse(
                p.getIdPuesto(),
                p.getDescripcion(),
                p.getSalario(),
                p.getTipoPublicacion(),
                p.getActivo(),
                p.getFechaRegistro(),
                p.getEmpresa() != null ? p.getEmpresa().getIdEmpresa() : null,
                p.getEmpresa() != null ? p.getEmpresa().getNombre() : null,
                cs
        );
    }
}
