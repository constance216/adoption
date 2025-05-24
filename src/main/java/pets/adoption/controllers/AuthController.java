package pets.adoption.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pets.adoption.dto.auth.*;
import pets.adoption.models.User;
import pets.adoption.security.CustomUserDetails;
import pets.adoption.security.JwtUtils;
import pets.adoption.services.UserService;
import pets.adoption.services.EmailService;
import pets.adoption.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final MapperUtil mapperUtil;
    private final EmailService emailService;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        
        // Generate temporary JWT token for 2FA
        String tempToken = jwtUtils.generateTempJwtToken(authentication);
        
        // Generate and send 2FA code
        String code = generateVerificationCode();
        userService.create2FACode(user, code);
        emailService.send2FACode(user.getEmail(), code);
        
        return ResponseEntity.ok(new JwtResponse(
            tempToken,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getUser().getEmail(),
            userDetails.getRole(),
            true // requires2FA
        ));
    }
    
    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FACode(@Valid @RequestBody VerifyCodeRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getToken());
        User user = userService.getUserByUsername(username);
        
        if (userService.verify2FACode(user, request.getCode())) {
            // Create UserDetails and Authentication
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate permanent JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getUser().getEmail(),
                userDetails.getRole(),
                false // doesn't require 2FA anymore
            ));
        }
        
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification code"));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest signupRequest) {
        User user = mapperUtil.map(signupRequest, User.class);
        User createdUser = userService.createUser(user);
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        String token = UUID.randomUUID().toString();
        
        userService.createPasswordResetToken(user, token);
        emailService.sendPasswordResetToken(user.getEmail(), token);
        
        return ResponseEntity.ok(new MessageResponse("Password reset email sent successfully"));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Passwords don't match"));
        }
        
        userService.resetPassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                return ResponseEntity.ok(new MessageResponse("Token is valid for user: " + username));
            }
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
    }
    
    private String generateVerificationCode() {
        // Generate a 6-digit code
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}