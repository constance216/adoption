package pets.adoption.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private Long id;
    private String name;
    private BreedSummary breed;
    private CategorySummary category;
    private Integer age;
    private String description;
    private String image;
    private String gender;
    private String status;
    private UserSummary owner;
    private ShelterSummary shelter;
    private UserSummary veterinarian;
    private UserSummary adoptedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
