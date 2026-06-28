package pe.com.polyline.cotizador.service.impl;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.request.CotizacionRequest;
import pe.com.polyline.cotizador.dto.response.CotizacionResponse;
import pe.com.polyline.cotizador.exception.NegocioException;
import pe.com.polyline.cotizador.exception.RecursoNoEncontradoException;
import pe.com.polyline.cotizador.mapper.CotizacionMapper;
import pe.com.polyline.cotizador.model.*;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;
import pe.com.polyline.cotizador.repository.CotizacionRepository;
import pe.com.polyline.cotizador.repository.UsuarioRepository;
import pe.com.polyline.cotizador.service.ConfigEmpresaService;
import pe.com.polyline.cotizador.service.CotizacionService;
import pe.com.polyline.cotizador.service.ProductoService;

import java.time.format.DateTimeFormatter;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CotizacionServiceImpl implements CotizacionService {

    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    private final CotizacionRepository cotizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;
    private final ConfigEmpresaService configEmpresaService;

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


        BigDecimal total = subtotalAcumulado;

        BigDecimal base = subtotalAcumulado
                .divide(new BigDecimal("1.18"), 2, RoundingMode.HALF_UP);

        BigDecimal igv = total.subtract(base);


        cotizacion.setSubtotal(base);
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

    // lógica de negocio importante
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

    @Override
    public byte[] generarPdf(Long id) {

        Cotizacion cot = cotizacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cotización", id));

        ConfigEmpresa emp = configEmpresaService.obtenerEntidad();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            String numero = String.format("%04d", cot.getNumero());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
            String fechaFormateada = cot.getFechaCreacion().format(formatter);


            if (emp.getLogoUrl() != null && !emp.getLogoUrl().isEmpty()) {

                ImageData imageData = ImageDataFactory.create(emp.getLogoUrl());
                Image logo = new Image(imageData);

                logo.scaleToFit(80, 80);

                document.add(logo);
            }

            Table header = new Table(2);
            header.setWidth(UnitValue.createPercentValue(100));

            Cell left = new Cell().setBorder(null);
            left.add(new Paragraph(emp.getNombreEmpresa()).setBold().setFontSize(18));
            left.add(new Paragraph(emp.getNombreRepresentante()).setFontSize(10));
            left.add(new Paragraph("Tel: " + emp.getTelefono()).setFontSize(10));
            left.add(new Paragraph(emp.getCorreo()).setFontSize(10));
            left.add(new Paragraph(emp.getDireccion()).setFontSize(10));

            Cell right = new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT);
            right.add(new Paragraph("COTIZACIÓN").setBold().setFontSize(14));
            right.add(new Paragraph(numero).setFontSize(20).setBold());

            header.addCell(left);
            header.addCell(right);

            document.add(header);

            document.add(new Paragraph("\n"));

            SolidLine solidLine = new SolidLine();
            solidLine.setLineWidth(1f);
            LineSeparator line = new LineSeparator(solidLine);

            document.add(line);
            document.add(new Paragraph("\n"));

            Table clienteTable = new Table(2);
            clienteTable.setWidth(UnitValue.createPercentValue(100));

            Cell cliente = new Cell().setBorder(null);
            cliente.add(new Paragraph("CLIENTE").setBold().setFontSize(10));
            cliente.add(new Paragraph(cot.getNombreCliente()).setFontSize(11));
            cliente.add(new Paragraph("Proyecto: " + (cot.getNombreProyecto() != null ? cot.getNombreProyecto() : "-")).setFontSize(10));

            Cell fecha = new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT);
            fecha.add(new Paragraph("FECHA").setBold().setFontSize(10));
            fecha.add(new Paragraph(fechaFormateada).setFontSize(11));

            clienteTable.addCell(cliente);
            clienteTable.addCell(fecha);

            document.add(clienteTable);
            document.add(new Paragraph("\n"));

            Table table = new Table(5);
            table.setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"#", "PRODUCTO", "CANT.", "P. UNIT.", "SUBTOTAL"};
            System.out.println("DETALLES SIZE: " + cot.getDetalles().size());/////////////vamos a borrar depues recuerda

            for (String h : headers) {
                Cell cell = new Cell().add(new Paragraph(h).setBold().setFontSize(9));
                cell.setBackgroundColor(new DeviceRgb(240, 240, 240));
                cell.setTextAlignment(TextAlignment.CENTER);
                cell.setPadding(6);
                table.addCell(cell);
            }

            int i = 1;
            for (DetalleCotizacion d : cot.getDetalles()) {

                table.addCell(new Cell().add(new Paragraph(String.valueOf(i++))).setTextAlignment(TextAlignment.CENTER).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(d.getProducto().getNombre()).setFontSize(9)).setPadding(6));
                table.addCell(new Cell().add(new Paragraph(d.getCantidad() + " " + d.getProducto().getUnidad()).setFontSize(9)).setTextAlignment(TextAlignment.CENTER).setPadding(6));
                table.addCell(new Cell().add(new Paragraph("S/. " + String.format("%.2f", d.getPrecioUnitario())).setFontSize(9)).setTextAlignment(TextAlignment.RIGHT).setPadding(6));
                table.addCell(new Cell().add(new Paragraph("S/. " + String.format("%.2f", d.getSubtotal())).setFontSize(9)).setTextAlignment(TextAlignment.RIGHT).setPadding(6));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            Table resumen = new Table(2);
            resumen.setWidth(UnitValue.createPercentValue(40));
            resumen.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            resumen.addCell(new Cell().setBorder(null)
                    .add(new Paragraph("SUBTOTAL (Base Imp.)").setFontSize(10)));

            resumen.addCell(new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph("S/. " + String.format("%.2f", cot.getSubtotal())).setFontSize(10)));

            resumen.addCell(new Cell().setBorder(null)
                    .add(new Paragraph("IGV (18%)").setFontSize(10)));

            resumen.addCell(new Cell().setBorder(null).setTextAlignment(TextAlignment.RIGHT)
                    .add(new Paragraph("S/. " + String.format("%.2f", cot.getIgv())).setFontSize(10)));

            resumen.addCell(
                    new Cell().setBorderTop(new SolidBorder(1))
                            .add(new Paragraph("TOTAL").setBold().setFontSize(12))
            );

            resumen.addCell(
                    new Cell().setTextAlignment(TextAlignment.RIGHT)
                            .setBorderTop(new SolidBorder(1))
                            .add(new Paragraph("S/. " + String.format("%.2f", cot.getTotal())).setBold().setFontSize(12))
            );

            document.add(resumen);
            document.add(new Paragraph("\n"));

            document.add(line);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("CONDICIONES").setBold().setFontSize(10));
            document.add(new Paragraph(
                    emp.getCondicionesCotizacion() != null ? emp.getCondicionesCotizacion() : "-"
            ).setFontSize(9));

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("NOTAS").setBold().setFontSize(10));
            document.add(new Paragraph(
                    cot.getObservaciones() != null && !cot.getObservaciones().isEmpty()
                            ? cot.getObservaciones()
                            : "-"
            ).setFontSize(9));

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }

        return baos.toByteArray();
    }

    @Override
    public Page<CotizacionResponse> listarPaginado(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("fechaCreacion").descending()
        );

        return cotizacionRepository
                .findAllByOrderByFechaCreacionDesc(pageable)
                .map(CotizacionMapper::toResponse);
    }
}
