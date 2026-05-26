package com.SecurityConfig;





import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // 🔥 ESTO QUITA EL 403
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/users/**").permitAll()
                        .anyExchange().permitAll()
                )
                .build();
    }
}
