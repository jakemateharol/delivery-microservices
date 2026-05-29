package users_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final String JWT_SECRET =
            "MiClaveSecretaSuperSeguraYMuyLargaParaElDelivery2026";

    private final int JWT_EXPIRATION_MS = 86400000;

    private Key getSigningKey() {
        byte[] keyBytes = JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 🔥 MODIFICADO: Ahora recibe el username Y el role
    public String generateJwtToken(String username, String role) {

        return Jwts.builder()
                .subject(username)
                .claim("roles", role) // 🎯 ¡La línea mágica que faltaba para el inventario!
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    // VALIDAR TOKEN
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // OBTENER USERNAME
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}