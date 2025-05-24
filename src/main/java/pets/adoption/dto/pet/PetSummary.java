package pets.adoption.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetSummary {
    private Long id;
    private String name;
    private String breed;
    private String category;
    private Integer age;
    private String gender;
    private String status;
    private String image;
}
