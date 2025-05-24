package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pets.adoption.dto.pet.PetResponse;
import pets.adoption.dto.user.*;
import pets.adoption.models.Pet;
import pets.adoption.models.User;
import pets.adoption.services.VeterinarianService;
import pets.adoption.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/veterinarians")
@RequiredArgsConstructor
public class VeterinarianController {
    
    private final VeterinarianService veterinarianService;
    private final MapperUtil mapperUtil;
    
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllVeterinarians() {
        List<User> veterinarians = veterinarianService.getAllVeterinarians();
        List<UserResponse> responses = veterinarians.stream()
            .map(vet -> mapperUtil.map(vet, UserResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getVeterinarianById(@PathVariable Long id) {
        User veterinarian = veterinarianService.getVeterinarianById(id);
        return ResponseEntity.ok(mapperUtil.map(veterinarian, UserResponse.class));
    }
    
    @GetMapping("/{id}/pets")
    public ResponseEntity<List<PetResponse>> getPetsByVeterinarian(@PathVariable Long id) {
        List<Pet> pets = veterinarianService.getPetsByVeterinarian(id);
        List<PetResponse> responses = pets.stream()
            .map(pet -> mapperUtil.map(pet, PetResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createVeterinarian(@Valid @RequestBody VeterinarianCreateRequest request) {
        User veterinarian = new User();
        veterinarian.setUsername(request.getUsername());
        veterinarian.setEmail(request.getEmail());
        veterinarian.setPassword(request.getPassword());
        veterinarian.setFullName(request.getFullName());
        
        User createdVeterinarian = veterinarianService.createVeterinarian(veterinarian);
        return ResponseEntity.ok(mapperUtil.map(createdVeterinarian, UserResponse.class));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('VETERINARIAN') and #id == authentication.principal.id)")
    public ResponseEntity<UserResponse> updateVeterinarian(@PathVariable Long id, @Valid @RequestBody User veterinarianDetails) {
        User updatedVeterinarian = veterinarianService.updateVeterinarian(id, veterinarianDetails);
        return ResponseEntity.ok(mapperUtil.map(updatedVeterinarian, UserResponse.class));
    }
    
    @PostMapping("/{veterinarianId}/pets/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIAN', 'SHELTER')")
    public ResponseEntity<PetResponse> assignPetToVeterinarian(@PathVariable Long petId, @PathVariable Long veterinarianId) {
        Pet pet = veterinarianService.assignPetToVeterinarian(petId, veterinarianId);
        return ResponseEntity.ok(mapperUtil.map(pet, PetResponse.class));
    }
    
    @DeleteMapping("/{veterinarianId}/pets/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIAN', 'SHELTER')")
    public ResponseEntity<Void> removePetFromVeterinarian(@PathVariable Long petId, @PathVariable Long veterinarianId) {
        veterinarianService.removePetFromVeterinarian(petId, veterinarianId);
        return ResponseEntity.noContent().build();
    }
}