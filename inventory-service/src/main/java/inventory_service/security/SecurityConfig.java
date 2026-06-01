package inventory_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar @PreAuthorize en los controladores si quieres
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
        // 🛡️ Esto saca la ruta del radar de Spring Security por completo
           .requestMatchers(HttpMethod.PUT, "/api/dishes/descontar-stock");
    }

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .authorizeHttpRequests(auth -> auth
            // 1. Permitir que TODO EL MUNDO vea la carta del restaurante
            .requestMatchers(HttpMethod.GET, "/api/dishes").permitAll()
            
            // 🎯 NUEVA REGLA: Permitir que el microservicio de órdenes descuente stock sin token
            .requestMatchers(HttpMethod.PUT, "/api/dishes/descontar-stock").permitAll() 
            
            // 2. Proteger la creación, edición y TODO LO DEMÁS de platos SOLO para el ADMIN
            .requestMatchers("/api/dishes/**").hasRole("ADMIN")
            
            // Cualquier otra ruta requiere estar autenticado
            .anyRequest().authenticated()
        );

    // Añadir nuestro filtro JWT antes del filtro por defecto de Spring
         http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

         return http.build();
    }
}