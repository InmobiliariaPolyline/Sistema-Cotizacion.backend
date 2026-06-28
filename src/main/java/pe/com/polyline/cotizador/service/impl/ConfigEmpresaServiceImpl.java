package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.request.ConfigEmpresaRequest;
import pe.com.polyline.cotizador.dto.response.ConfigEmpresaResponse;
import pe.com.polyline.cotizador.model.ConfigEmpresa;
import pe.com.polyline.cotizador.repository.ConfigEmpresaRepository;
import pe.com.polyline.cotizador.service.ConfigEmpresaService;
import pe.com.polyline.cotizador.repository.ConfigEmpresaRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ConfigEmpresaServiceImpl implements ConfigEmpresaService {

    private static final Long ID_SINGLETON = 1L;

    private final ConfigEmpresaRepository configEmpresaRepository;

    @Override
    public ConfigEmpresaResponse obtener() {
        ConfigEmpresa config = obtenerOCrearDefault();
        return toResponse(config);
    }

    @Override
    @Transactional
    public ConfigEmpresaResponse actualizar(ConfigEmpresaRequest request) {
        ConfigEmpresa config = obtenerOCrearDefault();

        if (request.getNombreEmpresa() != null)
            config.setNombreEmpresa(request.getNombreEmpresa().trim());

        if (request.getNombreRepresentante() != null)
            config.setNombreRepresentante(request.getNombreRepresentante().trim());

        if (request.getTelefono() != null)
            config.setTelefono(request.getTelefono());

        if (request.getCorreo() != null)
            config.setCorreo(request.getCorreo());

        if (request.getDireccion() != null)
            config.setDireccion(request.getDireccion());

        if (request.getCondicionesCotizacion() != null)
            config.setCondicionesCotizacion(request.getCondicionesCotizacion());

        if (request.getLogoUrl() != null)
            config.setLogoUrl(request.getLogoUrl());

        return toResponse(configEmpresaRepository.save(config));
    }

    // lógica de singleton: si no existe configuración, crea una con valores por defecto, esto garantiza que siempre hay una configuración con id = 1L.
    private ConfigEmpresa obtenerOCrearDefault() {
        return configEmpresaRepository.findById(ID_SINGLETON)
                .orElseGet(() -> {
                    ConfigEmpresa defecto = ConfigEmpresa.builder()
                            .id(ID_SINGLETON)
                            .build();
                    return configEmpresaRepository.save(defecto);
                });
    }

    private ConfigEmpresaResponse toResponse(ConfigEmpresa c) {
        return ConfigEmpresaResponse.builder()
                .id(c.getId())
                .nombreEmpresa(c.getNombreEmpresa())
                .nombreRepresentante(c.getNombreRepresentante())
                .telefono(c.getTelefono())
                .correo(c.getCorreo())
                .direccion(c.getDireccion())
                .condicionesCotizacion(c.getCondicionesCotizacion())
                .logoUrl(c.getLogoUrl())
                .fechaActualizacion(c.getFechaActualizacion())
                .build();
    }

    @Override
    public ConfigEmpresa obtenerEntidad() {

        return configEmpresaRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay configuración de empresa"));
    }

}
