package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy // Habilita los aspectos
@ComponentScan(basePackages = {"models", "services", "aspectos"})
public class ProjectConfiguration {
    // No es necesario añadir configuración adicional
}