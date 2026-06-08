package pe.com.polyline.cotizador.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaResponse {
    private Long id;
    private String nombre;
    private String icono;
    private String color;
    private Boolean activo;
}
