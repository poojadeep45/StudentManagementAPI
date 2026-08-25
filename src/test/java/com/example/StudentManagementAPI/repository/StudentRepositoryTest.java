package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenFindByStudentEmail_thenReturnStudent() {
        Student student = Student.builder()
                .studentName("Pooja")
                .studentSurname("Deep")
                .studentCourse("BSCS")
                .studentEmail("poojadeep@gmail.com")
                .studentNIC("44303-3423352-1")
                .studentPhone("0333-4167678")
                .build();
        entityManager.persistAndFlush(student);

        Optional<Student> found = studentRepository.findByStudentEmail("poojadeep@gmail.com");

        assertThat(found).isPresent();
        assertThat(found.get().getStudentName()).isEqualTo("Pooja");
    }

    @Test
    void whenFindByStudentNameIgnoreCase_thenReturnList() {
        Student student = Student.builder()
                .studentName("Pooja")
                .studentEmail("pooja2@gmail.com")
                .studentNIC("44303-1111111-1")
                .build();
        entityManager.persistAndFlush(student);

        List<Student> found = studentRepository.findByStudentNameIgnoreCase("pooja"); // lowercase on purpose

        assertThat(found).hasSize(1);
    }

    @Test
    void whenExistsByStudentEmail_andEmailNotPresent_thenReturnFalse() {
        assertThat(studentRepository.existsByStudentEmail("nobody@gmail.com")).isFalse();
    }
}