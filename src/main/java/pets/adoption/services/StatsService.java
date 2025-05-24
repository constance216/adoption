package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pets.adoption.dto.StatsDTO;
import pets.adoption.repositories.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    private final PetRepository petRepository;
    private final AdoptionRepository adoptionRepository;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BreedRepository breedRepository;

    public StatsDTO getSystemStats() {
        return StatsDTO.builder()
                .totalPets(petRepository.count())
                .totalAdoptions(adoptionRepository.count())
                .totalShelters(shelterRepository.count())
                .totalUsers(userRepository.count())
                .totalCategories(categoryRepository.count())
                .totalBreeds(breedRepository.count())
                .availablePets(petRepository.findByStatus("AVAILABLE").size())
                .adoptedPets(petRepository.findByStatus("ADOPTED").size())
                .pendingAdoptions(adoptionRepository.findByStatus("PENDING").size())
                .build();
    }
} 