package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addrId;

    @NotBlank(message = "Please Enter Street. ")
    private String street;

    @NotBlank(message = "Please Enter City.")
    private String city;
    private String postalCode;

    // Address owns the FK column (student_id) — this is the "owning" side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId" , unique = true)
    @com.fasterxml.jackson.annotation.JsonBackReference("student-address")
    private Student student;
}
