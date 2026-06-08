package pe.com.polyline.cotizador.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.polyline.cotizador.dto.request.ConfigEmpresaRequest;
import pe.com.polyline.cotizador.dto.response.ConfigEmpresaResponse;
import pe.com.polyline.cotizador.service.ConfigEmpresaService;

@RestController
@RequestMapping("/api/config-empresa")
@RequiredArgsConstructor
public class ConfigEmpresaController {

    private final ConfigEmpresaService configEmpresaService;

    @GetMapping
    public ResponseEntity<ConfigEmpresaResponse> obtener() {
        return ResponseEntity.ok(configEmpresaService.obtener());
    }

    @PutMapping
    public ResponseEntity<ConfigEmpresaResponse> actualizar(
            @Valid @RequestBody ConfigEmpresaRequest request) {
        return ResponseEntity.ok(configEmpresaService.actualizar(request));
    }
}
