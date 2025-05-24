package pets.adoption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.adoption.models.Breed;
import java.util.List;
import java.util.Optional;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByName(String name);
    List<Breed> findByCategory_Id(Long categoryId);
    boolean existsByNameAndCategory_Id(String name, Long categoryId);
}