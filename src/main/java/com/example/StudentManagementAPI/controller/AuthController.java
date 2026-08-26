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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Authentication", description = "Registration, login, verification, and password management")
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

    @Operation(summary = "Register a new user account", description = "Creates a new account with a hashed password; sends a verification email")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body")
    })
    // Creates a new user account. Password is hashed before storage (see AuthServiceImpl).
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User saved = authService.registerUser(user);
        saved.setPassword(null); // never echo the password hash back to the client
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Log in", description = "Verifies credentials and returns a signed JWT for use in the Authorization: Bearer header")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT returned"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
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

    @Operation(summary = "Verify a new account", description = "Confirms account ownership via the token sent by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account verified successfully"),
            @ApiResponse(responseCode = "400", description = "Token is invalid or expired")
    })
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

    @Operation(summary = "Resend the verification email", description = "Issues a new verification token if the original expired or was lost")
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam String userEmail) {
        authService.resendVerification(userEmail);
        return ResponseEntity.ok("Verification Token Resend Successfully");
    }

    @Operation(summary = "Request a password reset", description = "Sends a password reset link to the given email")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String userEmail) {
        authService.requestPasswordReset(userEmail);
        return ResponseEntity.ok("Password reset link sent.");
    }

    @Operation(summary = "Reset password via token", description = "Sets a new password using the token from the reset-link email")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequest request) {
        authService.resetPassword(token, request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }

    @Operation(summary = "Change password", description = "Changes the password for an authenticated user given their old password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Old password does not match")
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String userName,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        authService.changePassword(userName, oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }
}