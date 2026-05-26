package users_service.users_service.security;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // En producción, esta clave secreta debe estar en tu Config Server o variables de entorno
    private final String JWT_SECRET = "MiClaveSecretaSuperSeguraYMuyLargaParaElDelivery2026";
    private final int JWT_EXPIRATION_MS = 86400000; // 1 día de validez

    private Key getSigningKey() {
        byte[] keyBytes = this.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generar el token cuando el usuario hace login
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }
}
