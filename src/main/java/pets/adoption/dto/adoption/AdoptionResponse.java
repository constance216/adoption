package pets.adoption.dto.adoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import pets.adoption.dto.pet.PetSummary;
import pets.adoption.dto.pet.UserSummary;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionResponse {
    private Long id;
    private PetSummary pet;
    private UserSummary adopter;
    private LocalDateTime adoptionDate;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
