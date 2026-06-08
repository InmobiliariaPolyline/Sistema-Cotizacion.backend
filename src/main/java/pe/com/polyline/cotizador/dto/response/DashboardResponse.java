package pe.com.polyline.cotizador.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardResponse {
    private Long totalCotizaciones;
    private Long cotizacionesBorrador;
    private Long cotizacionesGeneradas;
    private Long cotizacionesEnviadas;
    private BigDecimal totalFacturado;
    private Long totalProductos;
    private Long totalCategorias;
}
