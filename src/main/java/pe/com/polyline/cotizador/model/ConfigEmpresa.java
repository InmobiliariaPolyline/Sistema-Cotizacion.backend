package pe.com.polyline.cotizador.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion_empresa")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigEmpresa {

    @Id
    private Long id;

    @Column(nullable = false, length = 150)
    @Builder.Default
    private String nombreEmpresa = "Polyline SAC";

    @Column(length = 150)
    @Builder.Default
    private String nombreRepresentante = "Arq. Luis Alberto Salas Castro";

    @Column(length = 30)
    @Builder.Default
    private String telefono = "+51 943 812 536";

    @Column(length = 150)
    @Builder.Default
    private String correo = "polylinesac@yahoo.com";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String direccion = "Av. Benavides 3008. Lima";

    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String condicionesCotizacion =
            "Precios vigentes a la fecha de emisión. " +
            "Validez de 15 días hábiles. " +
            "Incluye IGV del 18%. " +
            "Sujeto a disponibilidad de stock.";

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
}
