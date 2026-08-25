package com.example.StudentManagementAPI.event;

import com.example.StudentManagementAPI.Entity.User;
import com.example.StudentManagementAPI.Entity.VerificationToken;
import com.example.StudentManagementAPI.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RegistrationListener {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @EventListener
    public void handleRegistrationEvent(RegistrationEvent event) {
        User user = event.getUser();

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        tokenRepository.save(verificationToken);

        // Simulated email — in a real app, this would call an email service.
        // For now, we just log the link so you can copy it and test manually
        System.out.println("=== VERIFICATION EMAIL ===");
        System.out.println("To: " + user.getUserEmail());
        System.out.println("Verify your account: http://localhost:8080/auth/verify?token=" + token);
        System.out.println("==========================");
    }
}
