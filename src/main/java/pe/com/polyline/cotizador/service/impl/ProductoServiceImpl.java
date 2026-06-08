package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.request.ProductoRequest;
import pe.com.polyline.cotizador.dto.response.ProductoResponse;
import pe.com.polyline.cotizador.exception.RecursoNoEncontradoException;
import pe.com.polyline.cotizador.mapper.ProductoMapper;
import pe.com.polyline.cotizador.model.Categoria;
import pe.com.polyline.cotizador.model.Producto;
import pe.com.polyline.cotizador.repository.ProductoRepository;
import pe.com.polyline.cotizador.service.CategoriaService;
import pe.com.polyline.cotizador.service.ProductoService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    @Override
    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> listarActivosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId).stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(ProductoMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }

    @Override
    public Producto obtenerEntidadActiva(Long id) {
        return productoRepository.findById(id)
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto activo", id));
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaService.obtenerEntidad(request.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(request.getNombre().trim())
                .descipcion(request.getDescripcion())
                .precio(request.getPrecio())
                .imagenUrl(request.getImagenUrl())
                .unidad(request.getUnidad().trim())
                .proveedor(request.getProveedor().trim())
                .enStock(request.getEnStock() != null ? request.getEnStock() : true)
                .activo(true)
                .categoria(categoria)
                .build();

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));

        Categoria categoria = categoriaService.obtenerEntidad(request.getCategoriaId());

        producto.setNombre(request.getNombre().trim());
        producto.setDescipcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setUnidad(request.getUnidad().trim());
        producto.setProveedor(request.getProveedor().trim());
        producto.setEnStock(request.getEnStock() != null ? request.getEnStock() : producto.getEnStock());
        producto.setCategoria(categoria);

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));

        producto.setActivo(false);
        productoRepository.save(producto);
    }

}
