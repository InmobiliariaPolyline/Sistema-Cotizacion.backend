package pe.com.polyline.cotizador.service;

import pe.com.polyline.cotizador.dto.request.ProductoRequest;
import pe.com.polyline.cotizador.dto.response.ProductoResponse;
import pe.com.polyline.cotizador.model.Producto;

import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listarActivos();

    List<ProductoResponse> listarActivosPorCategoria(Long categoriaId);

    List<ProductoResponse> listarTodos();

    ProductoResponse obtenerPorId(Long id);

    Producto obtenerEntidadActiva(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);

}
