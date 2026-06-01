package com.delivery.api_gateway.SecurityConfig;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    // 🔑 Clonamos exactamente tu clave secreta del users-service
    private final String JWT_SECRET = "MiClaveSecretaSuperSeguraYMuyLargaParaElDelivery2026";

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // Requerido por la factoría de filtros de Spring Cloud Gateway
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. Si no tiene la cabecera Authorization, lo rebotamos
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Falta la cabecera Authorization", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Estructura de Token inválida", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            try {
                // 2. Validar el token usando tu firma secreta
                byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
                SecretKey key = Keys.hmacShaKeyFor(keyBytes);

                Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token);

            } catch (Exception e) {
                // Si el token expiró o es falso, rebota aquí
                return onError(exchange, "Token inválido o expirado", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}