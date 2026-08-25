package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Enrollment;
import com.example.StudentManagementAPI.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/students/{studentId}/enroll/{courseId}")
    public ResponseEntity<Enrollment> enrollStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @RequestBody Enrollment enrollment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enrollStudent(studentId, courseId, enrollment));
    }

    @GetMapping("/students/{studentId}/enrollments")
    public ResponseEntity<List<Enrollment>> fetchEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.fetchEnrollmentsByStudentId(studentId));
    }
}
