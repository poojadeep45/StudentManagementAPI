package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.User;
import com.example.StudentManagementAPI.Entity.VerificationToken;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.event.RegistrationEvent;
import com.example.StudentManagementAPI.repository.UserRepository;
import com.example.StudentManagementAPI.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public User registerUser(User user){
        if (userRepository.existsByuserName(user.getUserName())) {
            throw new IllegalArgumentException("Username already taken! " + user.getUserName());
        }
        if (userRepository.existsByuserEmail(user.getUserEmail())) {
            throw new IllegalArgumentException("UserEmail already taken! " + user.getUserEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        User savedUser = userRepository.save(user);

        eventPublisher.publishEvent(new RegistrationEvent(savedUser));
        return savedUser;
    }

    @Override
    public void resendVerification(String userEmail) {
        User user = userRepository.findByuserEmail(userEmail)
                .orElseThrow(() -> new StudentNotfoundException("No account found with email: " + userEmail));

        if (user.isVerified()) {
            throw new IllegalArgumentException("Account is already verified.");
        }

        // Find the existing token for this user, if any, and update it in place
        // rather than inserting a new row (VerificationToken is @OneToOne with User,
        // so only one token row can exist per user at a time).
        VerificationToken existingToken = verificationTokenRepository.findByUser(user)
                .orElse(VerificationToken.builder().user(user).build());

        existingToken.setToken(java.util.UUID.randomUUID().toString());
        existingToken.setExpiryDate(java.time.LocalDateTime.now().plusHours(24));
        verificationTokenRepository.save(existingToken);

        System.out.println("=== VERIFICATION EMAIL (RESENT) ===");
        System.out.println("To: " + user.getUserEmail());
        System.out.println("Verify your account: http://localhost:8080/auth/verify?token=" + existingToken.getToken());
        System.out.println("====================================");
    }

    @Override
    public void requestPasswordReset(String userEmail) {
        User user = userRepository.findByuserEmail(userEmail)
                .orElseThrow(() -> new StudentNotfoundException("No account found with email: " + userEmail));

        VerificationToken resetToken = verificationTokenRepository.findByUser(user)
                .orElse(VerificationToken.builder().user(user).build());

        resetToken.setToken(java.util.UUID.randomUUID().toString());
        resetToken.setExpiryDate(java.time.LocalDateTime.now().plusMinutes(30));
        verificationTokenRepository.save(resetToken);

        System.out.println("=== PASSWORD RESET EMAIL ===");
        System.out.println("To: " + user.getUserEmail());
        System.out.println("Reset your password: http://localhost:8080/auth/reset-password?token=" + resetToken.getToken());
        System.out.println("=============================");
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        VerificationToken resetToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepository.findByuserName(userName)
                .orElseThrow(() -> new StudentNotfoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}


