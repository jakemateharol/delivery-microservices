package inventory_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenProvider {

    // Pon exactamente la misma clave secreta de tu users-service
    private final String jwtSecret = "TuClaveSecretaSuperLargaYSeguraQueTieneQueTenerMasDeTreintaYDosCaracteres123456";

    private SecretKey getSigningKey() {
    byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes); // <-- Este es el método correcto
}
    
    // Si en usuarios usaste hmacShaKeyFor, cámbialo por:
    // return Keys.hmacShaKeyFor(keyBytes);

    // 1. Validar que el token sea correcto y no haya expirado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // 2. Extraer el nombre de usuario del token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 3. Extraer el rol del token (recuerda que lo guardamos como un String o Lista)
    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        // Jalamos el campo "roles" que inyectamos en el JWT de usuarios
        return claims.get("roles", String.class); 
    }
}
