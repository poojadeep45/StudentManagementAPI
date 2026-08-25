package com.example.StudentManagementAPI.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Enrollments" , uniqueConstraints = {
        @UniqueConstraint(columnNames = {"studentId" , "courseId"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentId")
    @com.fasterxml.jackson.annotation.JsonBackReference("student-enrollment")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId")
    @com.fasterxml.jackson.annotation.JsonBackReference("course-enrollment")
    private Course course;

    private String semester;

    private Double grade;

    private LocalDate enrollmentDate;
}
