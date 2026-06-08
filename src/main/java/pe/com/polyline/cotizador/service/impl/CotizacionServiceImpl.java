package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.request.CotizacionRequest;
import pe.com.polyline.cotizador.dto.response.CotizacionResponse;
import pe.com.polyline.cotizador.exception.NegocioException;
import pe.com.polyline.cotizador.exception.RecursoNoEncontradoException;
import pe.com.polyline.cotizador.mapper.CotizacionMapper;
import pe.com.polyline.cotizador.model.Cotizacion;
import pe.com.polyline.cotizador.model.DetalleCotizacion;
import pe.com.polyline.cotizador.model.Producto;
import pe.com.polyline.cotizador.model.Usuario;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;
import pe.com.polyline.cotizador.repository.CotizacionRepository;
import pe.com.polyline.cotizador.repository.UsuarioRepository;
import pe.com.polyline.cotizador.service.CotizacionService;
import pe.com.polyline.cotizador.service.ProductoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CotizacionServiceImpl implements CotizacionService {

    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    private final CotizacionRepository cotizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;

    @Override
    public List<CotizacionResponse> listarTodas() {
        return cotizacionRepository.findAllByOrderByFechaCreacionDesc().stream()
                .map(CotizacionMapper::toResponse)
                .toList();
    }

    @Override
    public List<CotizacionResponse> listarPorEstado(EstadoCotizacion estado) {
        return cotizacionRepository.findByEstadoOrderByFechaCreacionDesc(estado).stream()
                .map(CotizacionMapper::toResponse)
                .toList();
    }

    @Override
    public CotizacionResponse obtenerPorId(Long id) {
        return cotizacionRepository.findById(id)
                .map(CotizacionMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotización", id));
    }

    @Override
    @Transactional
    public CotizacionResponse crear(CotizacionRequest request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        int siguienteNumero = cotizacionRepository.findMaxNumero() + 1;

        Cotizacion cotizacion = Cotizacion.builder()
                .numero(siguienteNumero)
                .nombreCliente(request.getNombreCliente().trim())
                .nombreProyecto(request.getNombreProyecto())
                .observaciones(request.getObservaciones())
                .usuario(usuario)
                .estado(EstadoCotizacion.BORRADOR)
                .subtotal(BigDecimal.ZERO)
                .igv(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (var detalleReq : request.getDetalles()) {

            Producto producto = productoService.obtenerEntidadActiva(detalleReq.getProductoId());

            BigDecimal precioHistorico = producto.getPrecio();

            BigDecimal subtotalItem = precioHistorico
                    .multiply(BigDecimal.valueOf(detalleReq.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            DetalleCotizacion detalle = DetalleCotizacion.builder()
                    .producto(producto)
                    .cantidad(detalleReq.getCantidad())
                    .precioUnitario(precioHistorico)
                    .subtotal(subtotalItem)
                    .build();

            cotizacion.agregarDetalle(detalle);
            subtotalAcumulado = subtotalAcumulado.add(subtotalItem);
        }

        BigDecimal igv = subtotalAcumulado.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalAcumulado.add(igv).setScale(2, RoundingMode.HALF_UP);

        cotizacion.setSubtotal(subtotalAcumulado);
        cotizacion.setIgv(igv);
        cotizacion.setTotal(total);

        return CotizacionMapper.toResponse(cotizacionRepository.save(cotizacion));
    }

    @Override
    @Transactional
    public CotizacionResponse cambiarEstado(Long id, EstadoCotizacion nuevoEstado) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotización", id));

        validarTransicionEstado(cotizacion.getEstado(), nuevoEstado);

        cotizacion.setEstado(nuevoEstado);

        return CotizacionMapper.toResponse(cotizacionRepository.save(cotizacion));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cotizacion cotizacion = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotización", id));

        if (cotizacion.getEstado() != EstadoCotizacion.BORRADOR) {
            throw new NegocioException("Solo se pueden eliminar cotizaciones en estado BORRADOR");
        }

        cotizacionRepository.delete(cotizacion);
    }

    // 🔥 lógica de negocio importante
    private void validarTransicionEstado(EstadoCotizacion actual, EstadoCotizacion nuevo) {
        boolean valido = switch (actual) {
            case BORRADOR -> nuevo == EstadoCotizacion.GENERADA;
            case GENERADA -> nuevo == EstadoCotizacion.ENVIADA;
            case ENVIADA -> false;
        };

        if (!valido) {
            throw new NegocioException(
                    String.format("No se puede cambiar el estado de %s a %s", actual, nuevo)
            );
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario autenticado no encontrado"));
    }

}
