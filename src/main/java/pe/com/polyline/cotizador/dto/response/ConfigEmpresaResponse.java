package pe.com.polyline.cotizador.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConfigEmpresaResponse {
    private Long id;
    private String nombreEmpresa;
    private String nombreRepresentante;
    private String telefono;
    private String correo;
    private String direccion;
    private String condicionesCotizacion;
    private String logoUrl;
    private LocalDateTime fechaActualizacion;
}
