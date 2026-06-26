package pe.com.polyline.cotizador.dto.response;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CotizacionDashboardResponse {

    private Integer numero;
    private String nombreCliente;
    private LocalDateTime fechaCreacion;
    private BigDecimal total;
    private String estado;

}
