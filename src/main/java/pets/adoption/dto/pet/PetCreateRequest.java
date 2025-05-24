package pets.adoption.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetCreateRequest {
    @NotBlank(message = "Pet name is required")
    private String name;
    
    private Long breedId;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    private Integer age;
    
    private String description;
    
    private String image;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    private Long ownerId;
    
    private Long shelterId;
    
    private Long veterinarianId;
}
