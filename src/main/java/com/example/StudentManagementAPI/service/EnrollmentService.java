package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Enrollment;

import java.util.List;

public interface EnrollmentService {
    Enrollment enrollStudent(Long studentId, Long courseId, Enrollment enrollment);
    List<Enrollment> fetchEnrollmentsByStudentId(Long studentId);
}
