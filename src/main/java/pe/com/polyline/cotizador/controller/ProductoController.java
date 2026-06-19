package pe.com.polyline.cotizador.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.com.polyline.cotizador.dto.request.ProductoRequest;
import pe.com.polyline.cotizador.dto.response.ProductoResponse;
import pe.com.polyline.cotizador.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return ResponseEntity.ok(productoService.listarActivosPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ProductoResponse>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ProductoResponse> crearConImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("unidad") String unidad,
            @RequestParam("proveedor") String proveedor,
            @RequestParam("enStock") Boolean enStock,
            @RequestParam("categoriaId") Long categoriaId
    ) {

        ProductoResponse producto = productoService.crearConImagen(
                file, nombre, descripcion, precio, unidad, proveedor, enStock, categoriaId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping(
            value = "/{id}/upload",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ProductoResponse> actualizarConImagen(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") BigDecimal precio,
            @RequestParam("unidad") String unidad,
            @RequestParam("proveedor") String proveedor,
            @RequestParam("enStock") Boolean enStock,
            @RequestParam("categoriaId") Long categoriaId
    ) {

        ProductoResponse producto = productoService.actualizarConImagen(
                id, file, nombre, descripcion, precio, unidad, proveedor, enStock, categoriaId
        );

        return ResponseEntity.ok(producto);
    }
}
