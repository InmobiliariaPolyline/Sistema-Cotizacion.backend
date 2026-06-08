package pe.com.polyline.cotizador.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal precio;

    @Size(max = 500)
    private String imagenUrl;

    @NotBlank(message = "La unidad es obligatoria")
    @Size(max = 50)
    private String unidad;

    @NotBlank(message = "El proveedor es obligatorio")
    @Size(max = 150)
    private String proveedor;

    private Boolean enStock = true;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
}
