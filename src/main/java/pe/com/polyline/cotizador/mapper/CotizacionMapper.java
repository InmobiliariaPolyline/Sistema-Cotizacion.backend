package pe.com.polyline.cotizador.mapper;

import pe.com.polyline.cotizador.dto.response.CotizacionResponse;
import pe.com.polyline.cotizador.dto.response.DetalleCotizacionResponse;
import pe.com.polyline.cotizador.model.Cotizacion;
import pe.com.polyline.cotizador.model.DetalleCotizacion;

import java.util.List;

public class CotizacionMapper {

    private CotizacionMapper() {}

    public static CotizacionResponse toResponse(Cotizacion c) {
        List<DetalleCotizacionResponse> detalles = c.getDetalles().stream()
                .map(CotizacionMapper::toDetalleResponse)
                .toList();

        return CotizacionResponse.builder()
                .id(c.getId())
                .numero(c.getNumero())
                .nombreCliente(c.getNombreCliente())
                .nombreProyecto(c.getNombreProyecto())
                .observaciones(c.getObservaciones())
                .subtotal(c.getSubtotal())
                .igv(c.getIgv())
                .total(c.getTotal())
                .estado(c.getEstado())
                .usuarioId(c.getUsuario().getId())
                .usuarioNombre(c.getUsuario().getNombre())
                .detalles(detalles)
                .fechaCreacion(c.getFechaCreacion())
                .fechaActualizacion(c.getFechaActualizacion())
                .build();
    }

    private static DetalleCotizacionResponse toDetalleResponse(DetalleCotizacion d) {
        return DetalleCotizacionResponse.builder()
                .id(d.getId())
                .productoId(d.getProducto().getId())
                .productoNombre(d.getProducto().getNombre())
                .productoUnidad(d.getProducto().getUnidad())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .build();
    }
}
