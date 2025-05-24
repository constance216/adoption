package pets.adoption.dto.adoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequest {
    @NotNull(message = "Pet ID is required")
    private Long petId;
    
    @NotNull(message = "Adopter ID is required")
    private Long adopterId;
    
    private String notes;
}
