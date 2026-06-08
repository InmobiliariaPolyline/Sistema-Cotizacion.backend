package pe.com.polyline.cotizador.service;

import pe.com.polyline.cotizador.dto.request.ConfigEmpresaRequest;
import pe.com.polyline.cotizador.dto.response.ConfigEmpresaResponse;

public interface ConfigEmpresaService {

    ConfigEmpresaResponse obtener();

    ConfigEmpresaResponse actualizar(ConfigEmpresaRequest request);

}
