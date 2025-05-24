package pets.adoption.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelterSummary {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
} 