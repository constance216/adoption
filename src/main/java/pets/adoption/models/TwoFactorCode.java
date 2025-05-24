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
public class TwoFactorCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    private boolean used;

    @PrePersist
    protected void onCreate() {
        if (expiryDate == null) {
            // Set default expiry to 5 minutes from creation
            expiryDate = LocalDateTime.now().plusMinutes(5);
        }
    }

    public boolean isValid() {
        return !used && LocalDateTime.now().isBefore(expiryDate);
    }
} 