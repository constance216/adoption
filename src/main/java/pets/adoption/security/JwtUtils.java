package pets.adoption.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    
    @Value("${jwt.temp-expiration:300000}") // 5 minutes in milliseconds
    private int tempJwtExpirationMs;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key signingKey;
    
    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.getBytes());
            signingKey = Keys.hmacShaKeyFor(keyBytes);
            logger.info("JWT signing key initialized successfully");
        } catch (Exception e) {
            logger.warn("Error initializing JWT key from property, generating a new one");
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            String encodedKey = Base64.getEncoder().encodeToString(signingKey.getEncoded());
            logger.info("Generated new JWT key. Add this to application.properties as jwt.secret to make it persistent: {}", encodedKey);
        }
    }
    
    private Key getSigningKey() {
        return signingKey;
    }
    
    public String generateJwtToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String generateTempJwtToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tempJwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}