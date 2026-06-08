package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.response.DashboardResponse;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;
import pe.com.polyline.cotizador.repository.CategoriaRepository;
import pe.com.polyline.cotizador.repository.CotizacionRepository;
import pe.com.polyline.cotizador.repository.ProductoRepository;
import pe.com.polyline.cotizador.service.DashboardService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final CotizacionRepository cotizacionRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public DashboardResponse obtenerResumen() {

        return DashboardResponse.builder()
                .totalCotizaciones(cotizacionRepository.count())
                .cotizacionesBorrador(cotizacionRepository.countByEstado(EstadoCotizacion.BORRADOR))
                .cotizacionesGeneradas(cotizacionRepository.countByEstado(EstadoCotizacion.GENERADA))
                .cotizacionesEnviadas(cotizacionRepository.countByEstado(EstadoCotizacion.ENVIADA))
                .totalFacturado(
                        cotizacionRepository.sumAllTotales() != null
                                ? cotizacionRepository.sumAllTotales()
                                : BigDecimal.ZERO
                )
                .totalProductos(productoRepository.count())
                .totalCategorias(categoriaRepository.count())
                .build();
    }

}
