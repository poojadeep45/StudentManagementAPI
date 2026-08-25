package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @NotBlank(message = "PLease Enter Course Name. ")
    private String courseName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Please Enter Course Code. ")
    private String courseCode;

    @Min(value = 1, message = "Credit Hours must be atleast 1. ")
    private int creditHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructorId")
    @com.fasterxml.jackson.annotation.JsonBackReference("instructor-course")
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonManagedReference("course-enrollment")
    private List<Enrollment> enrollments = new ArrayList<>();
}
