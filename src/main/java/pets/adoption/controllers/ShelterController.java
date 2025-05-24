package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pets.adoption.dto.pet.PetResponse;
import pets.adoption.dto.user.UserResponse;
import pets.adoption.models.Pet;
import pets.adoption.models.User;
import pets.adoption.services.ShelterService;
import pets.adoption.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import pets.adoption.dto.ShelterDTO;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {
    
    private final ShelterService shelterService;
    private final MapperUtil mapperUtil;
    
    @GetMapping
    public ResponseEntity<List<ShelterDTO>> getAllShelters() {
        return ResponseEntity.ok(shelterService.getAllShelters());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelterById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getShelterById(id));
    }
    
    @GetMapping("/{id}/pets")
    public ResponseEntity<List<PetResponse>> getPetsByShelter(@PathVariable Long id) {
        List<Pet> pets = shelterService.getPetsByShelter(id);
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterDTO shelterDTO) {
        return ResponseEntity.ok(shelterService.createShelter(shelterDTO));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterDTO shelterDTO) {
        return ResponseEntity.ok(shelterService.updateShelter(id, shelterDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) {
        shelterService.deleteShelter(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{shelterId}/pets/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<PetResponse> assignPetToShelter(@PathVariable Long petId, @PathVariable Long shelterId) {
        Pet pet = shelterService.assignPetToShelter(petId, shelterId);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
    
    @DeleteMapping("/{shelterId}/pets/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<Void> removePetFromShelter(@PathVariable Long petId, @PathVariable Long shelterId) {
        shelterService.removePetFromShelter(petId, shelterId);
        return ResponseEntity.noContent().build();
    }
}