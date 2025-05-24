package pets.adoption.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    public void send2FACode(String to, String code) {
        String subject = "Your 2FA Verification Code";
        String body = String.format("""
            Hello,
            
            Your verification code is: %s
            
            This code will expire in 5 minutes.
            
            If you didn't request this code, please ignore this email.
            
            Best regards,
            Pet Adoption Team
            """, code);

        sendEmail(to, subject, body);
    }

    public void sendPasswordResetToken(String to, String token) {
        String subject = "Password Reset Request";
        String body = String.format("""
            Hello,
            
            You have requested to reset your password. Click the link below to proceed:
            
            %s/reset-password?token=%s
            
            This link will expire in 1 hour.
            
            If you didn't request this password reset, please ignore this email.
            
            Best regards,
            Pet Adoption Team
            """, frontendUrl, token);

        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
} 