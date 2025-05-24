package pets.adoption.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pets.adoption.dto.ShelterDTO;
import pets.adoption.models.Shelter;
import pets.adoption.models.Pet;
import pets.adoption.repositories.ShelterRepository;
import pets.adoption.repositories.PetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;

    public List<ShelterDTO> getAllShelters() {
        return shelterRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShelterDTO getShelterById(Long id) {
        return shelterRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found with id: " + id));
    }

    public ShelterDTO createShelter(ShelterDTO shelterDTO) {
        Shelter shelter = convertToEntity(shelterDTO);
        Shelter savedShelter = shelterRepository.save(shelter);
        return convertToDTO(savedShelter);
    }

    public ShelterDTO updateShelter(Long id, ShelterDTO shelterDTO) {
        if (!shelterRepository.existsById(id)) {
            throw new EntityNotFoundException("Shelter not found with id: " + id);
        }
        Shelter shelter = convertToEntity(shelterDTO);
        shelter.setId(id);
        Shelter updatedShelter = shelterRepository.save(shelter);
        return convertToDTO(updatedShelter);
    }

    public void deleteShelter(Long id) {
        if (!shelterRepository.existsById(id)) {
            throw new EntityNotFoundException("Shelter not found with id: " + id);
        }
        shelterRepository.deleteById(id);
    }

    public List<Pet> getPetsByShelter(Long shelterId) {
        if (!shelterRepository.existsById(shelterId)) {
            throw new EntityNotFoundException("Shelter not found with id: " + shelterId);
        }
        return petRepository.findByShelter_Id(shelterId);
    }

    public Pet assignPetToShelter(Long petId, Long shelterId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        Shelter shelter = shelterRepository.findById(shelterId)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found with id: " + shelterId));

        pet.setShelter(shelter);
        return petRepository.save(pet);
    }

    public void removePetFromShelter(Long petId, Long shelterId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        if (pet.getShelter() == null || !pet.getShelter().getId().equals(shelterId)) {
            throw new IllegalStateException("Pet is not associated with the specified shelter");
        }

        pet.setShelter(null);
        petRepository.save(pet);
    }

    private ShelterDTO convertToDTO(Shelter shelter) {
        return new ShelterDTO(
                shelter.getId(),
                shelter.getName(),
                shelter.getAddress(),
                shelter.getEmail(),
                shelter.getPhone()
        );
    }

    private Shelter convertToEntity(ShelterDTO shelterDTO) {
        return Shelter.builder()
                .id(shelterDTO.getId())
                .name(shelterDTO.getName())
                .address(shelterDTO.getAddress())
                .email(shelterDTO.getEmail())
                .phone(shelterDTO.getPhone())
                .build();
    }
}