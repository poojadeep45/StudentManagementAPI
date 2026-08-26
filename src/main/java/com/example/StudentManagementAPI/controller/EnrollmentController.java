package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Enrollment;
import com.example.StudentManagementAPI.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollments", description = "Manage student enrollment in courses")
@RequestMapping("/students")
@RestController
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @Operation(summary = "Enroll a student in a course", description = "Creates an enrollment record linking the given student and course")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "404", description = "Student or course does not exist"),
            @ApiResponse(responseCode = "409", description = "Student is already enrolled in this course")
    })
    @PostMapping("/{studentId}/enroll/{courseId}")
    public ResponseEntity<Enrollment> enrollStudent(
            @Parameter(description = "ID of the student to enroll") @PathVariable Long studentId,
            @Parameter(description = "ID of the course to enroll in") @PathVariable Long courseId,
            @RequestBody Enrollment enrollment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enrollStudent(studentId, courseId, enrollment));
    }

    @Operation(summary = "Fetch a student's enrollments", description = "Returns all courses the given student is enrolled in")
    @GetMapping("/{studentId}/enrollments")
    public ResponseEntity<List<Enrollment>> fetchEnrollments(
            @Parameter(description = "ID of the student whose enrollments to retrieve") @PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.fetchEnrollmentsByStudentId(studentId));
    }
}