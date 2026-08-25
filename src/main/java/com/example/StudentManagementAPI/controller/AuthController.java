package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.User;
import com.example.StudentManagementAPI.Entity.VerificationToken;
import com.example.StudentManagementAPI.dto.LoginRequest;
import com.example.StudentManagementAPI.dto.PasswordResetRequest;
import com.example.StudentManagementAPI.error.InvalidCredentialsException;
import com.example.StudentManagementAPI.repository.UserRepository;
import com.example.StudentManagementAPI.repository.VerificationTokenRepository;
import com.example.StudentManagementAPI.security.JwtUtil;
import com.example.StudentManagementAPI.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Public-facing authentication endpoints: register a new account, and log in
// to receive a JWT for use on protected endpoints.
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    // Creates a new user account. Password is hashed before storage (see AuthServiceImpl).
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User saved = authService.registerUser(user);
        saved.setPassword(null); // never echo the password hash back to the client
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Verifies credentials and, if valid, issues a signed JWT the client can
    // use on subsequent requests via the Authorization: Bearer <token> header.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByuserName(request.getUserName())
                .orElseThrow(() -> new InvalidCredentialsException("Username is invalid for " + request.getUserName()));

        // BCrypt hashes are salted/non-deterministic, so we can never compare
        // raw strings directly — matches() handles the correct comparison.
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Password mismatched for " + request.getUserName());
        }

        String token = jwtUtil.generateToken(user.getUserName(), user.getUserRole());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Token Validation for " + token));

        if (verificationToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new InvalidCredentialsException("Verification Token has expired " );
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("Account Verified Successfully");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String userEmail) {
        authService.resendVerification(userEmail);
        return ResponseEntity.ok("Verification Token Resend Successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String userEmail) {
        authService.requestPasswordReset(userEmail);
        return ResponseEntity.ok("Password reset link sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequest request) {
        authService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String userName,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        authService.changePassword(userName, oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }
}