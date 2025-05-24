package pets.adoption.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pets.adoption.models.User;
import pets.adoption.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Check if admin exists
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("uconsta21@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Change this in production
                admin.setFullName("System Administrator");
                admin.setRole("ADMIN");
                
                userRepository.save(admin);
                log.info("Admin user created with username: admin and password: admin123");
            } else {
                log.info("Admin user already exists");
            }
        };
    }
}
