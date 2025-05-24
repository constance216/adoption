package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pets.adoption.dto.adoption.*;
import pets.adoption.models.Adoption;
import pets.adoption.models.Pet;
import pets.adoption.models.User;
import pets.adoption.services.AdoptionService;
import pets.adoption.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adoptions")
@RequiredArgsConstructor
public class AdoptionController {
    
    private final AdoptionService adoptionService;
    private final MapperUtil mapperUtil;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AdoptionResponse> createAdoption(@Valid @RequestBody AdoptionRequest adoptionRequest) {
        Adoption adoption = new Adoption();
        adoption.setPet(new Pet());
        adoption.getPet().setId(adoptionRequest.getPetId());
        adoption.setAdopter(new User());
        adoption.getAdopter().setId(adoptionRequest.getAdopterId());
        adoption.setNotes(adoptionRequest.getNotes());
        
        Adoption createdAdoption = adoptionService.createAdoption(adoption);
        return ResponseEntity.ok(mapperUtil.map(createdAdoption, AdoptionResponse.class));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<List<AdoptionResponse>> getAllAdoptions() {
        List<Adoption> adoptions = adoptionService.getAllAdoptions();
        List<AdoptionResponse> responses = adoptions.stream()
            .map(adoption -> mapperUtil.map(adoption, AdoptionResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER') or @adoptionService.getAdoptionById(#id).adopter.id == authentication.principal.id")
    public ResponseEntity<AdoptionResponse> getAdoptionById(@PathVariable Long id) {
        Adoption adoption = adoptionService.getAdoptionById(id);
        return ResponseEntity.ok(mapperUtil.map(adoption, AdoptionResponse.class));
    }
    
    @GetMapping("/adopter/{adopterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER') or #adopterId == authentication.principal.id")
    public ResponseEntity<List<AdoptionResponse>> getAdoptionsByAdopter(@PathVariable Long adopterId) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByAdopter(adopterId);
        List<AdoptionResponse> responses = adoptions.stream()
            .map(adoption -> mapperUtil.map(adoption, AdoptionResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<List<AdoptionResponse>> getAdoptionsByPet(@PathVariable Long petId) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByPet(petId);
        List<AdoptionResponse> responses = adoptions.stream()
            .map(adoption -> mapperUtil.map(adoption, AdoptionResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<List<AdoptionResponse>> getAdoptionsByStatus(@PathVariable String status) {
        List<Adoption> adoptions = adoptionService.getAdoptionsByStatus(status);
        List<AdoptionResponse> responses = adoptions.stream()
            .map(adoption -> mapperUtil.map(adoption, AdoptionResponse.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<AdoptionResponse> updateAdoptionStatus(@PathVariable Long id, @Valid @RequestBody AdoptionStatusUpdateRequest statusUpdate) {
        Adoption updatedAdoption = adoptionService.updateAdoptionStatus(id, statusUpdate.getStatus());
        return ResponseEntity.ok(mapperUtil.map(updatedAdoption, AdoptionResponse.class));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    public ResponseEntity<AdoptionResponse> updateAdoption(@PathVariable Long id, @Valid @RequestBody AdoptionUpdateRequest adoptionUpdate) {
        Adoption adoptionDetails = mapperUtil.map(adoptionUpdate, Adoption.class);
        Adoption updatedAdoption = adoptionService.updateAdoption(id, adoptionDetails);
        return ResponseEntity.ok(mapperUtil.map(updatedAdoption, AdoptionResponse.class));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}