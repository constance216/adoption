package pets.adoption.dto.adoption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private String status; // PENDING, APPROVED, COMPLETED, CANCELLED
}
