package pets.adoption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelterDTO {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
} 