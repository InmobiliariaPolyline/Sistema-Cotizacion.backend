package pe.com.polyline.cotizador.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.polyline.cotizador.dto.request.CategoriaRequest;
import pe.com.polyline.cotizador.dto.response.CategoriaResponse;
import pe.com.polyline.cotizador.service.CategoriaService;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listarActivas());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    // ACTIVAR  DESACTIVAR
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        categoriaService.cambiarEstado(id, activo);
        return ResponseEntity.ok().build();
    }

    // ELIMINAR CATEGORIA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
