package pe.com.polyline.cotizador.model;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "categorias")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String icono;

    private String color;

    @Column(nullable = false)
    private Boolean activo = true;
}

