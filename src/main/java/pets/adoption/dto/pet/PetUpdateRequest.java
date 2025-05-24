package pets.adoption.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetUpdateRequest {
    private String name;
    private Long breedId;
    private Long categoryId;
    private Integer age;
    private String description;
    private String image;
    private String gender;
    private String status;
}
