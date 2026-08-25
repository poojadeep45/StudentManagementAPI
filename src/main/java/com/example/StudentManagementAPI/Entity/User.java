package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    @NotBlank (message = "Please Enter UserName: ")
    private String userName;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid format. ")
    @NotBlank(message = "Please Enter an Email. ")
    private String userEmail;

    @NotBlank(message = "Please Enter Password: ")
    private String password;

    @Builder.Default
    private String userRole =  "USER";

    @Builder.Default
    private boolean isVerified = false; // for the email verification flow later
}
