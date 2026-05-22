package users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private String username;

    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }
}
