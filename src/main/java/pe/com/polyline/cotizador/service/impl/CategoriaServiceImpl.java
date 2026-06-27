package pe.com.polyline.cotizador.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.polyline.cotizador.dto.request.CategoriaRequest;
import pe.com.polyline.cotizador.dto.response.CategoriaResponse;
import pe.com.polyline.cotizador.exception.RecursoNoEncontradoException;
import pe.com.polyline.cotizador.mapper.CategoriaMapper;
import pe.com.polyline.cotizador.model.Categoria;
import pe.com.polyline.cotizador.repository.CategoriaRepository;
import pe.com.polyline.cotizador.service.CategoriaService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaResponse> listarActivas() {
        return categoriaRepository.findAll().stream()
                .filter(c -> Boolean.TRUE.equals(c.getActivo()))
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Override
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Override
    public CategoriaResponse obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    @Override
    public Categoria obtenerEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    @Override
    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre().trim())
                .icono(request.getIcono())
                .color(request.getColor())
                .activo(true)
                .build();

        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));

        categoria.setNombre(request.getNombre().trim());
        categoria.setIcono(request.getIcono());
        categoria.setColor(request.getColor());

        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, boolean activo) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));

        categoria.setActivo(activo);
        categoriaRepository.save(categoria);
    }


    @Override
    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));

        categoriaRepository.delete(categoria);
    }


}
