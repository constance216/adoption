package pets.adoption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.adoption.models.Pet;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    List<Pet> findByStatus(String status);
    
    List<Pet> findByCategory_Id(Long categoryId);
    
    List<Pet> findByBreed_Id(Long breedId);
    
    List<Pet> findByOwner_Id(Long ownerId);
    
    List<Pet> findByShelter_Id(Long shelterId);
    
    List<Pet> findByVeterinarian_Id(Long veterinarianId);
    
    List<Pet> findByAdoptedBy_Id(Long adoptedById);
    
    List<Pet> findByBreed_NameIgnoreCase(String breedName);
    
    List<Pet> findByGenderAndStatus(String gender, String status);
    
    List<Pet> findByCategory_IdAndBreed_Id(Long categoryId, Long breedId);
}