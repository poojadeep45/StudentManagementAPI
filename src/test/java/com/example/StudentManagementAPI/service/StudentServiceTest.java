package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .studentName("Pooja")
                .studentSurname("Deep")
                .studentCourse("BSCS")
                .studentEmail("poojadeep@gmail.com")
                .studentNIC("44303-3423352-1")
                .studentPhone("0333-4167678")
                .studentId(1L)
                .build();
    }

    @Test
    public void whenValidStudent_thenStudentIsFound() {
        when(studentRepository.findByStudentNameIgnoreCase("Pooja"))
                .thenReturn(List.of(student));

        List<Student> found = studentService.fetchStudentByName("Pooja");

        assertFalse(found.isEmpty());
        assertEquals("Pooja", found.get(0).getStudentName());
    }

    @Test
    public void whenNameNotFound_thenThrowException() {
        when(studentRepository.findByStudentNameIgnoreCase("Pooja"))
                .thenReturn(List.of());

        assertThatThrownBy(() -> studentService.fetchStudentByName("Pooja"))
                .isInstanceOf(StudentNotfoundException.class);
    }

    @Test
    void whenSavingDuplicateEmail_thenThrowsIllegalArgumentException() {
        when(studentRepository.existsByStudentEmail("poojadeep@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.saveStudent(student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void whenDeletingNonexistentId_thenThrowsException() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudentById(99L))
                .isInstanceOf(StudentNotfoundException.class);
    }
}