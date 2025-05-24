package pets.adoption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.adoption.models.Adoption;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
    
    List<Adoption> findByAdopter_Id(Long adopterId);
    
    List<Adoption> findByPet_Id(Long petId);
    
    List<Adoption> findByStatus(String status);
    
    Optional<Adoption> findByPet_IdAndStatus(Long petId, String status);
    
    boolean existsByPet_IdAndStatusIn(Long petId, List<String> statuses);
}