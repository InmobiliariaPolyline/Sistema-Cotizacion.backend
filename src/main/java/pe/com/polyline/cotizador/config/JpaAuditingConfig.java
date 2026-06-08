package pe.com.polyline.cotizador.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // Habilita @CreatedDate y @LastModifiedDate en entidades

    // UN POCO MAS DE CONTEXTO: Esto activa el soporte para @CreateDate y @LastModifiedDate
    // en las entidades (en fechas por ejemplo y spring los llena automaticamente)
}
