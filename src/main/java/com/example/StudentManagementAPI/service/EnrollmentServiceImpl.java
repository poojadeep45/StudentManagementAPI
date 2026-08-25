package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Course;
import com.example.StudentManagementAPI.Entity.Enrollment;
import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.CourseRepository;
import com.example.StudentManagementAPI.repository.EnrollmentRepository;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Enrollment enrollStudent(Long studentId, Long courseId, Enrollment enrollment) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotfoundException("Student not found with id " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new StudentNotfoundException("Course not found with id " + courseId));

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> fetchEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId);
    }
}
