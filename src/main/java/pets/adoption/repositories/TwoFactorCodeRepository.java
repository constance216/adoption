package pets.adoption.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pets.adoption.models.TwoFactorCode;
import pets.adoption.models.User;

import java.util.Optional;

@Repository
public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findByUserAndCodeAndUsedFalse(User user, String code);
    void deleteByUser(User user);
} 