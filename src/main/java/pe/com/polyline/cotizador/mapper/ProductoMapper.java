package pe.com.polyline.cotizador.mapper;

import pe.com.polyline.cotizador.dto.response.ProductoResponse;
import pe.com.polyline.cotizador.model.Producto;

public class ProductoMapper {

    private ProductoMapper() {}

    public static ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .imagenUrl(p.getImagenUrl())
                .unidad(p.getUnidad())
                .proveedor(p.getProveedor())
                .enStock(p.getEnStock())
                .activo(p.getActivo())
                .categoriaId(p.getCategoria().getId())
                .categoriaNombre(p.getCategoria().getNombre())
                .fechaCreacion(p.getFechaCreacion())
                .fechaActualizacion(p.getFechaActualizacion())
                .build();
    }
}
