package pets.adoption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.adoption.models.Shelter;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
} 