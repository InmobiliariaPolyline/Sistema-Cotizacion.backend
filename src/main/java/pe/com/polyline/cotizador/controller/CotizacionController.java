package pe.com.polyline.cotizador.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.polyline.cotizador.dto.request.CotizacionRequest;
import pe.com.polyline.cotizador.dto.response.CotizacionResponse;
import pe.com.polyline.cotizador.model.enums.EstadoCotizacion;
import pe.com.polyline.cotizador.service.CotizacionService;

import java.util.List;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;

    @GetMapping
    public ResponseEntity<List<CotizacionResponse>> listar(
            @RequestParam(required = false) EstadoCotizacion estado) {
        if (estado != null) {
            return ResponseEntity.ok(cotizacionService.listarPorEstado(estado));
        }
        return ResponseEntity.ok(cotizacionService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cotizacionService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CotizacionResponse> crear(@Valid @RequestBody CotizacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cotizacionService.crear(request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CotizacionResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCotizacion estado) {
        return ResponseEntity.ok(cotizacionService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cotizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {

        byte[] pdf = cotizacionService.generarPdf(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=cotizacion.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/paginado")
    public Page<CotizacionResponse> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return cotizacionService.listarPaginado(page, size);
    }

}
