package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pets.adoption.models.User;
import pets.adoption.models.Pet;
import pets.adoption.repositories.UserRepository;
import pets.adoption.repositories.PetRepository;
import pets.adoption.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VeterinarianService {
    
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final UserService userService;
    
    public List<User> getAllVeterinarians() {
        return userRepository.findAll().stream()
            .filter(user -> "VETERINARIAN".equals(user.getRole()))
            .collect(Collectors.toList());
    }
    
    public User getVeterinarianById(Long id) {
        User veterinarian = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with id: " + id));
        
        if (!"VETERINARIAN".equals(veterinarian.getRole())) {
            throw new IllegalArgumentException("User is not a veterinarian");
        }
        
        return veterinarian;
    }
    
    public List<Pet> getPetsByVeterinarian(Long veterinarianId) {
        getVeterinarianById(veterinarianId); // Validate veterinarian exists
        return petRepository.findByVeterinarian_Id(veterinarianId);
    }
    
    public User createVeterinarian(User veterinarianData) {
        veterinarianData.setRole("VETERINARIAN");
        return userService.createUser(veterinarianData);
    }
    
    public User updateVeterinarian(Long id, User veterinarianDetails) {
        User veterinarian = getVeterinarianById(id);
        
        veterinarian.setFullName(veterinarianDetails.getFullName());
        veterinarian.setEmail(veterinarianDetails.getEmail());
        
        if (veterinarianDetails.getPassword() != null && !veterinarianDetails.getPassword().isEmpty()) {
            return userService.updateUser(id, veterinarianDetails);
        }
        
        return userRepository.save(veterinarian);
    }
    
    public Pet assignPetToVeterinarian(Long petId, Long veterinarianId) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        
        User veterinarian = getVeterinarianById(veterinarianId);
        
        pet.setVeterinarian(veterinarian);
        return petRepository.save(pet);
    }
    
    public void removePetFromVeterinarian(Long petId, Long veterinarianId) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        
        if (pet.getVeterinarian() != null && pet.getVeterinarian().getId().equals(veterinarianId)) {
            pet.setVeterinarian(null);
            petRepository.save(pet);
        }
    }
}