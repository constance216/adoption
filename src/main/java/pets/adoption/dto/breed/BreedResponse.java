package pets.adoption.dto.breed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pets.adoption.category.CategoryResponse;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedResponse {
    private Long id;
    private String name;
    private CategoryResponse category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
