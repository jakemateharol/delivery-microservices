package users_service.service;



import java.util.List;



import users_service.dto.AuthResponseDTO;
import users_service.dto.LoginRequestDTO;
import users_service.dto.RegisterRequestDTO;
import users_service.entity.User; // <-- PEGA ESTA LÍNEA

public interface AuthService {
    String registerUser(RegisterRequestDTO registerRequest);
    AuthResponseDTO loginUser(LoginRequestDTO loginRequest);
    List<User> obtenerTodosLosUsuarios();
}
