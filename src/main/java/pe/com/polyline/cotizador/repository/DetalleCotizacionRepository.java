package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.polyline.cotizador.model.DetalleCotizacion;

public interface DetalleCotizacionRepository extends JpaRepository<DetalleCotizacion, Long> {
}
