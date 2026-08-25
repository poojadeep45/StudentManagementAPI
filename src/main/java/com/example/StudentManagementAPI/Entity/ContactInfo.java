package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo {
    private String emergencyContactName;
    private String emergencyContactPhone;
}
