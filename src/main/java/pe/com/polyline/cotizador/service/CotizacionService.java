package pe.com.polyline.cotizador.service;

import org.springframework.data.domain.Page;
import pe.com.polyline.cotizador.dto.request.CotizacionRequest;
import pe.com.polyline.cotizador.dto.response.CotizacionResponse;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;

import java.util.List;

public interface CotizacionService {

    List<CotizacionResponse> listarTodas();

    List<CotizacionResponse> listarPorEstado(EstadoCotizacion estado);

    CotizacionResponse obtenerPorId(Long id);

    CotizacionResponse crear(CotizacionRequest request);

    CotizacionResponse cambiarEstado(Long id, EstadoCotizacion nuevoEstado);

    void eliminar(Long id);

    byte[] generarPdf(Long id);

    Page<CotizacionResponse> listarPaginado(int page, int size);
}
