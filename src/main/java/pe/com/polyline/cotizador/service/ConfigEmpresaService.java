package pe.com.polyline.cotizador.service;

import pe.com.polyline.cotizador.dto.request.ConfigEmpresaRequest;
import pe.com.polyline.cotizador.dto.response.ConfigEmpresaResponse;
import pe.com.polyline.cotizador.model.ConfigEmpresa;

public interface ConfigEmpresaService {

    ConfigEmpresaResponse obtener();

    ConfigEmpresaResponse actualizar(ConfigEmpresaRequest request);

    ConfigEmpresa obtenerEntidad();

}
