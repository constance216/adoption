package pets.adoption.dto.adoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionSummary {
    private Long id;
    private String petName;
    private String adopterName;
    private String status;
    private LocalDateTime adoptionDate;
}
