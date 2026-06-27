package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.com.polyline.cotizador.dto.response.CategoriaDashboardResponse;
import pe.com.polyline.cotizador.model.Categoria;
import pe.com.polyline.cotizador.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    @Query("""
    SELECT new pe.com.polyline.cotizador.dto.response.CategoriaDashboardResponse(
        c.id,
        c.nombre,
        c.icono,
        c.color,
        COUNT(p)
    )
    FROM Producto p
    JOIN p.categoria c
    GROUP BY c.id, c.nombre, c.icono, c.color
    """)
    List<CategoriaDashboardResponse> countProductosPorCategoria();
}