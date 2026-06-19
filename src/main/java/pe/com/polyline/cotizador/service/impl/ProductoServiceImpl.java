package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.com.polyline.cotizador.dto.request.ProductoRequest;
import pe.com.polyline.cotizador.dto.response.ProductoResponse;
import pe.com.polyline.cotizador.exception.RecursoNoEncontradoException;
import pe.com.polyline.cotizador.mapper.ProductoMapper;
import pe.com.polyline.cotizador.model.Categoria;
import pe.com.polyline.cotizador.model.Producto;
import pe.com.polyline.cotizador.repository.ProductoRepository;
import pe.com.polyline.cotizador.service.CategoriaService;
import pe.com.polyline.cotizador.service.CloudinaryService;
import pe.com.polyline.cotizador.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final CloudinaryService cloudinaryService;

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
                .descripcion(request.getDescripcion())
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
        producto.setDescripcion(request.getDescripcion());
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

    @Override
    @Transactional
    public ProductoResponse crearConImagen(
            MultipartFile file,
            String nombre,
            String descripcion,
            BigDecimal precio,
            String unidad,
            String proveedor,
            Boolean enStock,
            Long categoriaId
    ) {

        // 1. subir imagen
        String imageUrl = cloudinaryService.uploadFile(file);

        // 2. obtener categoría
        Categoria categoria = categoriaService.obtenerEntidad(categoriaId);

        // 3. crear producto
        Producto producto = Producto.builder()
                .nombre(nombre.trim())
                .descripcion(descripcion)
                .precio(precio)
                .imagenUrl(imageUrl)
                .unidad(unidad)
                .proveedor(proveedor)
                .enStock(enStock)
                .categoria(categoria)
                .activo(true)
                .build();

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponse actualizarConImagen(
            Long id,
            MultipartFile file,
            String nombre,
            String descripcion,
            BigDecimal precio,
            String unidad,
            String proveedor,
            Boolean enStock,
            Long categoriaId
    ) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", id));

        Categoria categoria = categoriaService.obtenerEntidad(categoriaId);

        producto.setNombre(nombre.trim());
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setUnidad(unidad.trim());
        producto.setProveedor(proveedor.trim());
        producto.setEnStock(enStock != null ? enStock : producto.getEnStock());
        producto.setCategoria(categoria);

        // Aca para actualizar la imagen, diferecia co el otro actualizar, si el file no es nulo y no esta vacio, se sube la nueva imagen y se actualiza el url
        if (file != null && !file.isEmpty()) {
            String url = cloudinaryService.uploadFile(file);
            producto.setImagenUrl(url);
        }

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }
}
