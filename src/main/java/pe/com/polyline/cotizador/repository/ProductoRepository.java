package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.polyline.cotizador.model.Categoria;
import pe.com.polyline.cotizador.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findByCategoriaAndActivoTrue(Categoria categoria);

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
}
