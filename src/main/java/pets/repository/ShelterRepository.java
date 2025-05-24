package pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.model.Shelter;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    boolean existsByEmail(String email);
} 