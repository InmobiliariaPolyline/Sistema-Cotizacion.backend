package pe.com.polyline.cotizador.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DetalleCotizacionResponse {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoUnidad;
    private Integer cantidad;
    // Precio historico del producto al momento de la cotización
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
