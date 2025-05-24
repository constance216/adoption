package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pets.adoption.services.BreedService;
import pets.adoption.dto.breed.BreedDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/breeds")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
@Slf4j
public class BreedController {
    
    private final BreedService breedService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BreedDTO> createBreed(@Valid @RequestBody BreedDTO request) {
        log.info("Starting breed creation process with request: {}", request);
        return ResponseEntity.ok(breedService.createBreed(request));
    }
    
    @GetMapping
    public ResponseEntity<List<BreedDTO>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BreedDTO> getBreedById(@PathVariable Long id) {
        return ResponseEntity.ok(breedService.getBreedById(id));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<BreedDTO> getBreedByName(@PathVariable String name) {
        return ResponseEntity.ok(breedService.getBreedByName(name));
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BreedDTO>> getBreedsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(breedService.getBreedsByCategory(categoryId));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BreedDTO> updateBreed(@PathVariable Long id, @Valid @RequestBody BreedDTO request) {
        return ResponseEntity.ok(breedService.updateBreed(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBreed(@PathVariable Long id) {
        breedService.deleteBreed(id);
        return ResponseEntity.ok().build();
    }
}