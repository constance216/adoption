package pets.adoption.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Email(message = "Email should be valid")
    private String email;
    
    private String fullName;
    
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;
}
