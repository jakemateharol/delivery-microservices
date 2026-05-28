package users_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import users_service.dto.AuthResponseDTO;
import users_service.dto.LoginRequestDTO;
import users_service.dto.RegisterRequestDTO;
import users_service.service.AuthService;

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


    @GetMapping("/profile")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN') or hasRole('REPARTIDOR')") // Ahora permitimos que cualquiera vea su propio perfil
    public ResponseEntity<?> profile() {
        try {
            // 1. Sacamos el usuario que está actualmente autenticado en el sistema gracias al Token
            org.springframework.security.core.Authentication authentication = 
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            String currentPrincipalName = authentication.getName();

            // 2. Preparamos la respuesta que va a leer tu Frontend para pintar la pantalla
            Map<String, Object> response = new HashMap<>();
            response.put("username", currentPrincipalName);
            response.put("roles", authentication.getAuthorities());
            response.put("estado", "Conectado de forma segura");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo cargar el perfil: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/repartidores")
    @PreAuthorize("hasRole('ADMIN')") // <-- ¡Solo el dueño del restaurante puede ver esta lista!
    public ResponseEntity<?> listarRepartidores() {
        try {
            // 1. Traemos absolutamente todos los usuarios de la base de datos
            java.util.List<users_service.entity.User> todosLosUsuarios = authService.obtenerTodosLosUsuarios(); 
            
            // 2. Filtramos con Java para quedarnos SÓLO con los que tengan el rol de REPARTIDOR
            java.util.List<users_service.entity.User> repartidores = todosLosUsuarios.stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_REPARTIDOR")))
                .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(repartidores);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo obtener la lista: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}