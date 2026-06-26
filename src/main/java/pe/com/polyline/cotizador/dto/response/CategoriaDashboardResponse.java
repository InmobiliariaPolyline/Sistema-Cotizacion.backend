package pe.com.polyline.cotizador.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaDashboardResponse {

    private Long id;
    private String nombre;
    private String icono;
    private String color;
    private Long total;

}
