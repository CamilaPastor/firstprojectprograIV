package cr.una.bolsaempleo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuración de seguridad web
     * - Deshabilita login automático de Spring Security
     * - Permite todas las rutas (control de acceso manual via HttpSession)
     * - Deshabilita CSRF para desarrollo
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar login automático de Spring Security
            .formLogin(form -> form.disable())

            // Deshabilitar logout automático
            .logout(logout -> logout.disable())

            // Deshabilitar autenticación básica HTTP
            .httpBasic(basic -> basic.disable())

            // Permitir todas las rutas - control de acceso manual via HttpSession
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )

            // Deshabilitar CSRF para desarrollo (solo para desarrollo!)
            // En producción, habilitar CSRF y manejar tokens en formularios
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Bean para encriptación de contraseñas con BCrypt
     * Usado por los servicios para hashear passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}