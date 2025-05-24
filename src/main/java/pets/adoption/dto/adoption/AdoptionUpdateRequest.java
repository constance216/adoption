package pets.adoption.dto.adoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionUpdateRequest {
    private String notes;
    private LocalDateTime adoptionDate;
}
