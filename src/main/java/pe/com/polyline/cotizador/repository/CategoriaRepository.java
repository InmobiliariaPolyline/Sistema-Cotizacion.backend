package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.polyline.cotizador.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
