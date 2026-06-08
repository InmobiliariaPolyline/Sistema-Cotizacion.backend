package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.polyline.cotizador.model.Cotizacion;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;

import java.math.BigDecimal;
import java.util.List;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    List<Cotizacion> findAllByOrderByFechaCreacionDesc();

    List<Cotizacion> findByEstadoOrderByFechaCreacionDesc(EstadoCotizacion estado);

    @Query("SELECT COALESCE(MAX(c.numero), 0) FROM Cotizacion c")
    Integer findMaxNumero();

    @Query("SELECT COALESCE(SUM(c.total), 0) FROM Cotizacion c")
    BigDecimal sumAllTotales();

    Long countByEstado(EstadoCotizacion estado);
}
