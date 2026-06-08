package pe.com.polyline.cotizador.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    // NOMBRE DEL PRODUCTO
    @Column(nullable = false, length = 200)
    private String nombre;

    // DESCRIPCIÓN DETALLADA
    @Column(columnDefinition = "TEXT")
    private String descipcion;

    // PRECIO SIN IGV
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    // IMAGE URL (Cloudinary)
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    // UNIDAD (m2, unidad, caja, etc.)
    @Column(nullable = false, length = 50)
    private String unidad;

    // PROVEEDOR
    @Column(nullable = false, length = 150)
    private String proveedor;

    // STOCK
    @Column(nullable = false)
    @Builder.Default
    private Boolean enStock = true;

    // SOFT DELETE
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

}
