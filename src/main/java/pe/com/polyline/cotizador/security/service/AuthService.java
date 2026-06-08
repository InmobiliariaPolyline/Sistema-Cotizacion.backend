package pe.com.polyline.cotizador.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.polyline.cotizador.security.dto.LoginRequest;
import pe.com.polyline.cotizador.security.dto.LoginResponse;
import pe.com.polyline.cotizador.security.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        // 1. Obtener usuario
        UserDetails userDetails =
                usuarioDetailsService.loadUserByUsername(request.getCorreo());

        // 2. Validar password manualmente
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 3. Generar token
        String token = jwtUtil.generarToken(userDetails.getUsername());

        //  4. Responder
        return new LoginResponse(token);
    }

}