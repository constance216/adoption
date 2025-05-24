package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pets.adoption.models.Breed;
import pets.adoption.models.Category;
import pets.adoption.repositories.BreedRepository;
import pets.adoption.repositories.CategoryRepository;
import pets.adoption.exceptions.ResourceNotFoundException;
import pets.adoption.exceptions.DuplicateResourceException;
import pets.adoption.dto.breed.BreedDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BreedService {
    
    private final BreedRepository breedRepository;
    private final CategoryRepository categoryRepository;

    private BreedDTO convertToDTO(Breed breed) {
        if (breed == null) {
            return null;
        }

        return new BreedDTO(
            breed.getId(),
            breed.getName(),
            breed.getDescription(),
            breed.getCategoryId(),
            breed.getCategoryName(),
            breed.getCreatedAt(),
            breed.getUpdatedAt()
        );
    }

    private Breed convertToEntity(BreedDTO dto) {
        if (dto == null) {
            return null;
        }

        Breed breed = new Breed();
        breed.setId(dto.getId());
        breed.setName(dto.getName());
        breed.setDescription(dto.getDescription());
        
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            breed.setCategory(category);
        }

        return breed;
    }

    public List<BreedDTO> getAllBreeds() {
        try {
            List<Breed> breeds = breedRepository.findAll();
            return breeds.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all breeds: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching breeds", e);
        }
    }

    @Transactional
    public BreedDTO createBreed(BreedDTO breedDTO) {
        if (breedDTO == null) {
            throw new IllegalArgumentException("Breed cannot be null");
        }
        if (breedDTO.getName() == null || breedDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Breed name is required");
        }
        if (breedDTO.getCategoryId() == null) {
            throw new IllegalArgumentException("Category is required");
        }

        Category category = categoryRepository.findById(breedDTO.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + breedDTO.getCategoryId()));

        if (breedRepository.existsByNameAndCategory_Id(breedDTO.getName(), category.getId())) {
            throw new DuplicateResourceException("Breed already exists with name: " + breedDTO.getName() + " in category: " + category.getName());
        }

        Breed breed = convertToEntity(breedDTO);
        Breed savedBreed = breedRepository.save(breed);
        return convertToDTO(savedBreed);
    }

    public BreedDTO getBreedById(Long id) {
        Breed breed = breedRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Breed not found with id: " + id));
        return convertToDTO(breed);
    }

    public BreedDTO getBreedByName(String name) {
        Breed breed = breedRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Breed not found with name: " + name));
        return convertToDTO(breed);
    }

    public List<BreedDTO> getBreedsByCategory(Long categoryId) {
        return breedRepository.findByCategory_Id(categoryId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public BreedDTO updateBreed(Long id, BreedDTO breedDTO) {
        Breed breed = breedRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Breed not found with id: " + id));

        if (breedDTO.getName() != null) {
            breed.setName(breedDTO.getName());
        }
        if (breedDTO.getDescription() != null) {
            breed.setDescription(breedDTO.getDescription());
        }

        if (breedDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(breedDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + breedDTO.getCategoryId()));

            if (!breed.getCategory().getId().equals(category.getId()) && 
                breedRepository.existsByNameAndCategory_Id(breed.getName(), category.getId())) {
                throw new DuplicateResourceException("Breed already exists with name: " + breed.getName() + " in category: " + category.getName());
            }

            breed.setCategory(category);
        }

        Breed savedBreed = breedRepository.save(breed);
        return convertToDTO(savedBreed);
    }

    @Transactional
    public void deleteBreed(Long id) {
        Breed breed = breedRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Breed not found with id: " + id));

        if (breed.getPets() != null && !breed.getPets().isEmpty()) {
            throw new IllegalStateException("Cannot delete breed with existing pets");
        }

        breedRepository.delete(breed);
    }
}