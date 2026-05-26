package users_service.users_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import users_service.users_service.dto.AuthResponseDTO;
import users_service.users_service.dto.LoginRequestDTO;
import users_service.users_service.dto.RegisterRequestDTO;
import users_service.users_service.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService; // <-- Inyectamos tu servicio real

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody RegisterRequestDTO registroDTO) {
        try {
            // Llamamos a tu lógica real de registro
            String mensaje = authService.registerUser(registroDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("username", registroDTO.getUsername());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si el usuario o email ya existen, atrapará el error aquí
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequestDTO loginDTO) {
        try {
            // Llamamos a tu lógica real de login que genera el JWT
            AuthResponseDTO authResponse = authService.loginUser(loginDTO);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Si la contraseña o usuario están mal, saltará aquí
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}