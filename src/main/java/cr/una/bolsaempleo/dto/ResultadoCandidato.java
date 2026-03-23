package cr.una.bolsaempleo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cr.una.bolsaempleo.model.Oferente;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoCandidato {

    private Oferente oferente;
    
    private Integer cumplidos;
    
    private Integer total;
    
    private Double porcentaje;

    /**
     * Constructor con cálculo automático de porcentaje
     */
    public ResultadoCandidato(Oferente oferente, Integer cumplidos, Integer total) {
        this.oferente = oferente;
        this.cumplidos = cumplidos;
        this.total = total;
        this.porcentaje = total > 0 ? (cumplidos * 100.0) / total : 0.0;
    }
}
