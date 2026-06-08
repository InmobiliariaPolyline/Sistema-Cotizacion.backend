package pe.com.polyline.cotizador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CotizadorBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(CotizadorBackendApplication.class, args);
	}

}
