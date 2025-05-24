package pets.adoption.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import pets.adoption.models.User;
import pets.adoption.models.TwoFactorCode;
import pets.adoption.models.PasswordResetToken;
import pets.adoption.repositories.UserRepository;
import pets.adoption.repositories.TwoFactorCodeRepository;
import pets.adoption.repositories.PasswordResetTokenRepository;
import pets.adoption.exceptions.ResourceNotFoundException;
import pets.adoption.exceptions.DuplicateResourceException;
import pets.adoption.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    public User updateUserRole(Long id, String role) {
        User user = getUserById(id);
        user.setRole(role);
        return userRepository.save(user);
    }
    
    public void create2FACode(User user, String code) {
        // Delete any existing codes for this user
        twoFactorCodeRepository.deleteByUser(user);
        
        TwoFactorCode twoFactorCode = TwoFactorCode.builder()
            .user(user)
            .code(code)
            .used(false)
            .build();
        
        twoFactorCodeRepository.save(twoFactorCode);
    }
    
    public boolean verify2FACode(User user, String code) {
        return twoFactorCodeRepository.findByUserAndCodeAndUsedFalse(user, code)
            .map(twoFactorCode -> {
                if (twoFactorCode.isValid()) {
                    twoFactorCode.setUsed(true);
                    twoFactorCodeRepository.save(twoFactorCode);
                    return true;
                }
                return false;
            })
            .orElse(false);
    }
    
    @Transactional
    public void createPasswordResetToken(User user, String token) {
        try {
            // Delete any existing tokens for this user
            passwordResetTokenRepository.deleteByUser(user);
            passwordResetTokenRepository.flush(); // Force the delete to be executed
            
            PasswordResetToken resetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .used(false)
                .build();
            
            passwordResetTokenRepository.save(resetToken);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create password reset token", e);
        }
    }
    
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenAndUsedFalse(token)
            .orElseThrow(() -> new InvalidTokenException("Invalid or expired password reset token"));
        
        if (!resetToken.isValid()) {
            throw new InvalidTokenException("Password reset token has expired");
        }
        
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
