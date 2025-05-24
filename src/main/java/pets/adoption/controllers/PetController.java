package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pets.adoption.dto.pet.*;
import pets.adoption.models.Pet;
import pets.adoption.models.Shelter;
import pets.adoption.services.PetService;
import pets.adoption.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    
    private final PetService petService;
    private final MapperUtil mapperUtil;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetCreateRequest petRequest) {
        Pet pet = mapperUtil.map(petRequest, Pet.class);
        
        // Set relationships
        if (petRequest.getCategoryId() != null) {
            pet.setCategory(new pets.adoption.models.Category());
            pet.getCategory().setId(petRequest.getCategoryId());
        }
        
        if (petRequest.getBreedId() != null) {
            pet.setBreed(new pets.adoption.models.Breed());
            pet.getBreed().setId(petRequest.getBreedId());
        }
        
        if (petRequest.getOwnerId() != null) {
            pet.setOwner(new pets.adoption.models.User());
            pet.getOwner().setId(petRequest.getOwnerId());
        }
        
        if (petRequest.getShelterId() != null) {
            pet.setShelter(new Shelter());
            pet.getShelter().setId(petRequest.getShelterId());
        }
        
        if (petRequest.getVeterinarianId() != null) {
            pet.setVeterinarian(new pets.adoption.models.User());
            pet.getVeterinarian().setId(petRequest.getVeterinarianId());
        }
        
        Pet createdPet = petService.createPet(pet);
        return ResponseEntity.ok(mapperUtil.map(createdPet, PetResponse.class));
    }
    
    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PetResponse>> getPetsByStatus(@PathVariable String status) {
        List<Pet> pets = petService.getPetsByStatus(status);
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PetResponse>> getPetsByCategory(@PathVariable Long categoryId) {
        List<Pet> pets = petService.getPetsByCategory(categoryId);
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/breed/{breedId}")
    public ResponseEntity<List<PetResponse>> getPetsByBreed(@PathVariable Long breedId) {
        List<Pet> pets = petService.getPetsByBreed(breedId);
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @Valid @RequestBody PetUpdateRequest petRequest) {
        Pet petDetails = mapperUtil.map(petRequest, Pet.class);
        
        // Set relationships
        if (petRequest.getCategoryId() != null) {
            petDetails.setCategory(new pets.adoption.models.Category());
            petDetails.getCategory().setId(petRequest.getCategoryId());
        }
        
        if (petRequest.getBreedId() != null) {
            petDetails.setBreed(new pets.adoption.models.Breed());
            petDetails.getBreed().setId(petRequest.getBreedId());
        }
        
        Pet updatedPet = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(mapperUtil.map(updatedPet, PetResponse.class));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{petId}/shelter/{shelterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<PetResponse> assignShelter(@PathVariable Long petId, @PathVariable Long shelterId) {
        Pet pet = petService.assignShelter(petId, shelterId);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
    
    @PostMapping("/{petId}/veterinarian/{veterinarianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIAN')")
    public ResponseEntity<PetResponse> assignVeterinarian(@PathVariable Long petId, @PathVariable Long veterinarianId) {
        Pet pet = petService.assignVeterinarian(petId, veterinarianId);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
    
    @PostMapping("/{petId}/adopt/{adopterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<PetResponse> adoptPet(@PathVariable Long petId, @PathVariable Long adopterId) {
        Pet pet = petService.adoptPet(petId, adopterId);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
}