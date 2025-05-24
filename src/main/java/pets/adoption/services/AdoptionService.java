package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pets.adoption.models.Adoption;
import pets.adoption.models.Pet;
import pets.adoption.models.User;
import pets.adoption.repositories.AdoptionRepository;
import pets.adoption.repositories.PetRepository;
import pets.adoption.repositories.UserRepository;
import pets.adoption.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptionService {
    
    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    
    public Adoption createAdoption(Adoption adoption) {
        // Validate pet
        Pet pet = petRepository.findById(adoption.getPet().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        
        // Validate adopter
        User adopter = userRepository.findById(adoption.getAdopter().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Adopter not found"));
        
        // Check if pet is already in an active adoption process
        List<String> activeStatuses = Arrays.asList("PENDING", "APPROVED");
        if (adoptionRepository.existsByPet_IdAndStatusIn(pet.getId(), activeStatuses)) {
            throw new IllegalStateException("Pet is already in an active adoption process");
        }
        
        // Check if pet is available for adoption
        if ("ADOPTED".equals(pet.getStatus())) {
            throw new IllegalStateException("Pet is already adopted");
        }
        
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        adoption.setStatus("PENDING");
        adoption.setAdoptionDate(LocalDateTime.now());
        
        return adoptionRepository.save(adoption);
    }
    
    public Adoption getAdoptionById(Long id) {
        return adoptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Adoption not found with id: " + id));
    }
    
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }
    
    public List<Adoption> getAdoptionsByAdopter(Long adopterId) {
        return adoptionRepository.findByAdopter_Id(adopterId);
    }
    
    public List<Adoption> getAdoptionsByPet(Long petId) {
        return adoptionRepository.findByPet_Id(petId);
    }
    
    public List<Adoption> getAdoptionsByStatus(String status) {
        return adoptionRepository.findByStatus(status);
    }
    
    public Adoption updateAdoptionStatus(Long id, String status) {
        Adoption adoption = getAdoptionById(id);
        adoption.setStatus(status);
        
        // If adoption is completed, update pet status
        if ("COMPLETED".equals(status)) {
            Pet pet = adoption.getPet();
            pet.setStatus("ADOPTED");
            pet.setAdoptedBy(adoption.getAdopter());
            petRepository.save(pet);
        }
        
        // If adoption is cancelled, revert pet status
        if ("CANCELLED".equals(status)) {
            Pet pet = adoption.getPet();
            if ("ADOPTED".equals(pet.getStatus()) && pet.getAdoptedBy() != null && 
                pet.getAdoptedBy().getId().equals(adoption.getAdopter().getId())) {
                pet.setStatus("ACTIVE");
                pet.setAdoptedBy(null);
                petRepository.save(pet);
            }
        }
        
        return adoptionRepository.save(adoption);
    }
    
    public Adoption updateAdoption(Long id, Adoption adoptionDetails) {
        Adoption adoption = getAdoptionById(id);
        
        adoption.setNotes(adoptionDetails.getNotes());
        adoption.setAdoptionDate(adoptionDetails.getAdoptionDate());
        
        return adoptionRepository.save(adoption);
    }
    
    public void deleteAdoption(Long id) {
        Adoption adoption = getAdoptionById(id);
        
        // If adoption was completed, revert pet status
        if ("COMPLETED".equals(adoption.getStatus())) {
            Pet pet = adoption.getPet();
            if ("ADOPTED".equals(pet.getStatus()) && pet.getAdoptedBy() != null && 
                pet.getAdoptedBy().getId().equals(adoption.getAdopter().getId())) {
                pet.setStatus("ACTIVE");
                pet.setAdoptedBy(null);
                petRepository.save(pet);
            }
        }
        
        adoptionRepository.delete(adoption);
    }
}