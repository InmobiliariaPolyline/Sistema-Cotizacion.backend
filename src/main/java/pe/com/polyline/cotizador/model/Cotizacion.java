package pe.com.polyline.cotizador.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizaciones")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número correlativo visible al usuario (COT-0001, COT-0002, etc.)
    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false, length = 200)
    private String nombreCliente;

    @Column(length = 300)
    private String nombreProyecto;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Subtotal sin IGV
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;

    // IGV calculado (18% sobre el subtotal).
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal igv;

    // Total con IGV incluido.
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCotizacion estado = EstadoCotizacion.BORRADOR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleCotizacion> detalles = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    // Método de ayuda para mantener la relación bidireccional entre Cotizacion y DetalleCotizacion
    public void agregarDetalle(DetalleCotizacion detalle) {
        detalles.add(detalle);
        detalle.setCotizacion(this);
    }

    public void removerDetalle(DetalleCotizacion detalle) {
        detalles.remove(detalle);
        detalle.setCotizacion(null);
    }
}
