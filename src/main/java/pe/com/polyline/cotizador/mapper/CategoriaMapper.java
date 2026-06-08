package pe.com.polyline.cotizador.mapper;

import pe.com.polyline.cotizador.dto.response.CategoriaResponse;
import pe.com.polyline.cotizador.model.Categoria;

public class CategoriaMapper {

    private CategoriaMapper() {}

    public static CategoriaResponse toResponse(Categoria c) {
        return CategoriaResponse.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .icono(c.getIcono())
                .color(c.getColor())
                .activo(c.getActivo())
                .build();
    }
}
