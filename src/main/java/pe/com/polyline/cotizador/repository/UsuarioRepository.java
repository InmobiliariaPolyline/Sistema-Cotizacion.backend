package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.polyline.cotizador.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
