package pe.com.polyline.cotizador.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.com.polyline.cotizador.model.Usuario;
import pe.com.polyline.cotizador.repository.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        // Buscar usuario
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado: " + correo)
                );

        // Validar activo (muy importante en producción)
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        // Crear UserDetails (Spring Security)
        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                )
        );
    }
}
