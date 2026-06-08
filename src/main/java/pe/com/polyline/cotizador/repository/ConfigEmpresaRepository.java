package pe.com.polyline.cotizador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.polyline.cotizador.model.ConfigEmpresa;

public interface ConfigEmpresaRepository extends JpaRepository<ConfigEmpresa, Long> {
    // Singleton por convención: siempre id = 1L
}
