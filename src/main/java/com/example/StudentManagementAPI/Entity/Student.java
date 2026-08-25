package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false)
    @NotBlank(message = "Please Enter the Student name.")
    private String studentName;

    private String studentSurname;

    @Column(unique = true ,  nullable = false)
    @NotBlank(message = "Please Enter the Student Email.")
    @Email(message = "Invalid Email Format. Please Enter Valid EmailID.")
    private String studentEmail;

    @Pattern( regexp = "^03[0-9]{2}-[0-9]{7}$", message = "Phone must be in format 03XX-XXXXXXX. Please Enter in correct format.")
    private String studentPhone;
    private String studentCourse;

    @Pattern(regexp = "^[0-9]{5}-[0-9]{7}-[0-9]{1}$", message = "NIC must be in this format 12345-1234567-1. Enter in correct format.")
    @Column(unique = true)
    private String studentNIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deptId")
    @com.fasterxml.jackson.annotation.JsonBackReference("department-student")
    private Department department;

    @Embedded
    private ContactInfo contactInfo;

    // Student is the "inverse" side — mappedBy points to the field name on Address
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference("student-address")
    private Address address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonManagedReference("student-enrollment")
    private List<Enrollment> enrollments = new ArrayList<>();

}

