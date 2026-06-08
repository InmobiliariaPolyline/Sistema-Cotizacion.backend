package pe.com.polyline.cotizador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConfigEmpresaRequest {

    @Size(max = 150)
    private String nombreEmpresa;

    @Size(max = 150)
    private String nombreRepresentante;

    @Size(max = 30)
    private String telefono;

    @Email(message = "Formato de correo inválido")
    @Size(max = 150)
    private String correo;

    private String direccion;

    private String condicionesCotizacion;

    @Size(max = 500)
    private String logoUrl;
}
