package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    Student saveStudent(Student student);

    Page<Student> fetchStudentList(Pageable pageable);

    List<Student> fetchStudentList();

    Student fetchStudentById(Long studentId);

    Optional<Student> fetchStudentByEmail(String studentEmail);

    void deleteStudentById(Long studentId);

    Student updateStudent(Long studentId, Student student);

    List<Student> fetchStudentByName(String studentName);

    Long countStudents();

    Student getByNIC(String studentNIC);

}
