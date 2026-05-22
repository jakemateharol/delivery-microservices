package users_service.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users_service.dto.RegisterRequestDTO;
import users_service.dto.LoginRequestDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users") // <-- Aquí le ponemos el v1 para que coincida con tu SecurityConfig
public class UserController {

    // NOTA: Aquí más adelante inyectaremos tu UserService con la lógica real de MySQL y JWT.
    // Por ahora, creamos este código "espejo" para verificar que Postman y la API Gateway conecten bien.

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequestDTO registroDTO) {
        // Simulamos que guardamos el usuario con éxito
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "¡Usuario registrado con éxito en el sistema!");
        response.put("username", registroDTO.getUsername());
        response.put("rolesAsignados", registroDTO.getRoles());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequestDTO loginDTO) {
        // Simulamos que el login es correcto y devolvemos un Token JWT falso de prueba
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "¡Inicio de sesión simula exitoso!");
        response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.FakeTokenForTestingOnly...");
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}