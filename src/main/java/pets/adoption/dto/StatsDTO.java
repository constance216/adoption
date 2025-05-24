package pets.adoption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private long totalPets;
    private long totalAdoptions;
    private long totalShelters;
    private long totalUsers;
    private long totalCategories;
    private long totalBreeds;
    private long availablePets;
    private long adoptedPets;
    private long pendingAdoptions;
} 