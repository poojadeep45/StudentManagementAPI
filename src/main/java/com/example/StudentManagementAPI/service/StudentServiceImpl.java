package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        if (studentRepository.existsByStudentEmail(student.getStudentEmail())) {
            throw new IllegalArgumentException("Email already exists " + student.getStudentEmail());
        }

        if (studentRepository.existsByStudentNIC(student.getStudentNIC())) {
            throw new IllegalArgumentException("NIC already exists " + student.getStudentNIC());
        }

        return studentRepository.save(student);
    }

    @Override
    public Page<Student> fetchStudentList(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public List<Student> fetchStudentList() {
        return studentRepository.findAll();
    }

    @Override
    public Student fetchStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotfoundException("Student not found with id " + studentId));
    }

    @Override
    public Optional<Student> fetchStudentByEmail(String studentEmail) {
        return Optional.of(studentRepository.findByStudentEmail(studentEmail)
                .orElseThrow(() -> new StudentNotfoundException("Student not found with email " + studentEmail)));
    }


    @Override
    public void deleteStudentById(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotfoundException("Cannot delete. Student not found with id " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public Student updateStudent(Long studentId, Student student) throws StudentNotfoundException {
        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotfoundException("Student Not Found with id " + studentId));

        // Check email conflict only if email is changing
        if (!existing.getStudentEmail().equals(student.getStudentEmail())
                && studentRepository.existsByStudentEmail(student.getStudentEmail())) {
            throw new IllegalArgumentException(
                    "Email already in use: " + student.getStudentEmail());
        }

        // Check NIC conflict only if NIC is changing
        if (student.getStudentNIC() != null
                && !student.getStudentNIC().equals(existing.getStudentNIC())
                && studentRepository.existsByStudentNIC(student.getStudentNIC())) {
            throw new IllegalArgumentException(
                    "NIC already in use: " + student.getStudentNIC());
        }

        existing.setStudentName(student.getStudentName());
        existing.setStudentSurname(student.getStudentSurname());
        existing.setStudentEmail(student.getStudentEmail());
        existing.setStudentPhone(student.getStudentPhone());
        existing.setStudentCourse(student.getStudentCourse());
        existing.setStudentNIC(student.getStudentNIC()); // was missing
        existing.setDepartment(student.getDepartment());
        existing.setContactInfo(student.getContactInfo());

        return studentRepository.save(existing);
    }

    @Override
    public List<Student> fetchStudentByName(String studentName) {
        List<Student> student = studentRepository.findByStudentNameIgnoreCase(studentName);
        if (student.isEmpty()) {
            throw new StudentNotfoundException("Student not found with name " + studentName);
        }
        return student;
    }

    @Override
    public Long countStudents() {
        return studentRepository.count();
    }

    @Override
    public Student getByNIC(String studentNIC) {
        Student student = studentRepository.findByStudentNIC(studentNIC)
                .orElseThrow(() -> new StudentNotfoundException("Student Not Found with NIC" + studentNIC));
        return student;
    }
}



