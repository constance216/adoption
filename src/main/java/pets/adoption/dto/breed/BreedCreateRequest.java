package pets.adoption.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedCreateRequest {
    @NotBlank(message = "Breed name is required")
    @JsonProperty("name")
    private String name;
    
    @NotNull(message = "Category ID is required")
    @JsonProperty("categoryId")
    private Long categoryId;
    
    @JsonProperty("description")
    private String description;
}
