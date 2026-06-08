package pe.com.polyline.cotizador.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CotizacionRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 200)
    private String nombreCliente;

    @Size(max = 300)
    private String nombreProyecto;

    private String observaciones;

    @NotEmpty(message = "La cotización debe tener al menos un producto")
    @Valid
    private List<DetalleCotizacionRequest> detalles;
}
