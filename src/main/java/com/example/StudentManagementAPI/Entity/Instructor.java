package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorId;

    @NotBlank(message = "Please Enter Instructor Name. ")
    private String instructorName;

    @Email(message = "Invalid Email Format. ")
    @Column(unique = true)
    private String instructorEmail;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonManagedReference("instructor-course")
    private List<Course> courses = new ArrayList<>();
}
