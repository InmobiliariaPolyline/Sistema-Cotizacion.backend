package pe.com.polyline.cotizador.dto.response;

import lombok.Builder;
import lombok.Data;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CotizacionResponse {
    private Long id;
    private Integer numero;
    private String nombreCliente;
    private String nombreProyecto;
    private String observaciones;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private EstadoCotizacion estado;
    private Long usuarioId;
    private String usuarioNombre;
    private List<DetalleCotizacionResponse> detalles;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
