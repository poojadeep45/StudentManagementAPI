package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
    List<Enrollment> findByStudent_StudentId(Long studentId);
    List<Enrollment> findByCourse_CourseId(Long courseId);
}
