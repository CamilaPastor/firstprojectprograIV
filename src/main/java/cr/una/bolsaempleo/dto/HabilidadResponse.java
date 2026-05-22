package cr.una.bolsaempleo.dto;

import cr.una.bolsaempleo.model.OferenteCaracteristica;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabilidadResponse {
    private Integer id;
    private Integer idCaracteristica;
    private String nombreCaracteristica;
    private Integer nivel;

    public static HabilidadResponse from(OferenteCaracteristica oc) {
        return new HabilidadResponse(
                oc.getId(),
                oc.getCaracteristica().getIdCaracteristica(),
                oc.getCaracteristica().getNombre(),
                oc.getNivel()
        );
    }
}
