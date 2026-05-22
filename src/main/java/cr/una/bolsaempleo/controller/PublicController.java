package cr.una.bolsaempleo.controller;

import cr.una.bolsaempleo.dto.CaracteristicaResponse;
import cr.una.bolsaempleo.dto.PuestoResponse;
import cr.una.bolsaempleo.model.Caracteristica;
import cr.una.bolsaempleo.model.Puesto;
import cr.una.bolsaempleo.service.CaracteristicaService;
import cr.una.bolsaempleo.service.PuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final PuestoService puestoService;
    private final CaracteristicaService caracteristicaService;

    @GetMapping("/puestos/recientes")
    public List<PuestoResponse> recientes() {
        return puestoService.ultimos5Publicos().stream().map(PuestoResponse::from).toList();
    }

    @GetMapping("/caracteristicas/arbol")
    public List<CaracteristicaResponse> arbol() {
        List<Caracteristica> raices = caracteristicaService.raices();
        return raices.stream().map(c -> CaracteristicaResponse.from(c, true)).toList();
    }

    @GetMapping("/puestos/buscar")
    public List<PuestoResponse> buscar(@RequestParam(value = "ids", required = false) List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        List<Puesto> resultados = puestoService.todosPublicosActivos().stream()
                .filter(p -> {
                    if (p.getCaracteristicasRequeridas() == null) return false;
                    return p.getCaracteristicasRequeridas().stream()
                            .anyMatch(pc -> ids.contains(pc.getCaracteristica().getIdCaracteristica()));
                })
                .toList();
        return resultados.stream().map(PuestoResponse::from).toList();
    }
}
