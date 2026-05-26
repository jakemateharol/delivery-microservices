package users_service.users_service.service;



import users_service.users_service.dto.AuthResponseDTO;
import users_service.users_service.dto.LoginRequestDTO;
import users_service.users_service.dto.RegisterRequestDTO;

public interface AuthService {
    String registerUser(RegisterRequestDTO registerRequest);
    AuthResponseDTO loginUser(LoginRequestDTO loginRequest);
}
