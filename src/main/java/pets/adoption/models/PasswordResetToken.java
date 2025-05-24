package pets.adoption.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    private boolean used;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            // Set default expiry to 1 hour from creation
            expiryDate = LocalDateTime.now().plusHours(1);
        }
    }

    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expiryDate);
    }
} 