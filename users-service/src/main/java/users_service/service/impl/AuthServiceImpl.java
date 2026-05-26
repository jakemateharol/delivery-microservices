package users_service.users_service.service.impl;



import users_service.users_service.dto.AuthResponseDTO;
import users_service.users_service.dto.LoginRequestDTO;
import users_service.users_service.dto.RegisterRequestDTO;
import users_service.users_service.entity.Role;
import users_service.users_service.entity.User;
import users_service.users_service.repository.RoleRepository;
import users_service.users_service.repository.UserRepository;
import users_service.users_service.security.JwtUtils;
import users_service.users_service.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String registerUser(RegisterRequestDTO registerRequest) {
        // 1. Validar que el username o email no existan ya
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Error: ¡El nombre de usuario ya está en uso!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: ¡El correo electrónico ya está registrado!");
        }

        // 2. Crear el nuevo objeto Usuario
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        
        // Encriptar la contraseña usando BCrypt antes de guardarla
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 3. Mapear y asignar los roles que vienen en el DTO
        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Rol por defecto si no mandan ninguno: CLIENTE
            Role defaultRole = roleRepository.findByName("ROLE_CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Error: El Rol ROLE_CLIENTE no fue encontrado."));
            roles.add(defaultRole);
        } else {
            strRoles.forEach(role -> {
                String roleName = "ROLE_" + role.toUpperCase();
                Role foundRole = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Error: El Rol " + roleName + " no existe."));
                roles.add(foundRole);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return "Usuario registrado exitosamente";
    }

    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO loginRequest) {
        // 1. Buscar si el usuario existe en la base de datos
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario o contraseña incorrectos."));

        // 2. Verificar si la contraseña ingresada coincide con la encriptada en la BD
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Error: Usuario o contraseña incorrectos.");
        }

        // 3. Generar el Token JWT si todo está bien
        String token = jwtUtils.generateJwtToken(user.getUsername());

        return new AuthResponseDTO(token, user.getUsername());
    }
}
