package cr.una.bolsaempleo.dto;

import cr.una.bolsaempleo.model.Caracteristica;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CaracteristicaResponse {
    private Integer idCaracteristica;
    private String nombre;
    private String descripcion;
    private Integer idPadre;
    private Integer nivelMinimo;
    private Integer nivelMaximo;
    private List<CaracteristicaResponse> hijos;

    public static CaracteristicaResponse from(Caracteristica c, boolean incluirHijos) {
        List<CaracteristicaResponse> hijos = null;
        if (incluirHijos && c.getHijos() != null) {
            hijos = c.getHijos().stream()
                    .filter(h -> Boolean.TRUE.equals(h.getActivo()))
                    .map(h -> from(h, true))
                    .toList();
        }
        return new CaracteristicaResponse(
                c.getIdCaracteristica(),
                c.getNombre(),
                c.getDescripcion(),
                c.getPadre() != null ? c.getPadre().getIdCaracteristica() : null,
                c.getNivelMinimo(),
                c.getNivelMaximo(),
                hijos
        );
    }
}
