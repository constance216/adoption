package pets.adoption.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCodeRequest {
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Token is required")
    private String token;
} 