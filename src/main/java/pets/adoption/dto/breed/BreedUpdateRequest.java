package pets.adoption.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedUpdateRequest {
    private String name;
    private Long categoryId;
    private String description;
}
