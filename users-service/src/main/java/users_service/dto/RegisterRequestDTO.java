package users_service.users_service.dto;



import lombok.Data;
import java.util.Set;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private Set<String> roles; // El cliente enviará "CLIENTE" o "ADMIN"
}