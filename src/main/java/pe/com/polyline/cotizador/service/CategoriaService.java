package pe.com.polyline.cotizador.service;

import pe.com.polyline.cotizador.dto.request.CategoriaRequest;
import pe.com.polyline.cotizador.dto.response.CategoriaResponse;
import pe.com.polyline.cotizador.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> listarActivas();

    List<CategoriaResponse> listarTodas();

    CategoriaResponse obtenerPorId(Long id);

    Categoria obtenerEntidad(Long id);

    CategoriaResponse crear(CategoriaRequest request);

    CategoriaResponse actualizar(Long id, CategoriaRequest request);

    void desactivar(Long id);

}
