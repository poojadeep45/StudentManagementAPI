package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.security.JwtUtil;
import com.example.StudentManagementAPI.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void whenValidId_thenReturns200() throws Exception {
        Student student = Student.builder()
                .studentId(1L)
                .studentName("Pooja")
                .studentEmail("poojadeep@gmail.com")
                .studentNIC("44303-3423352-1")
                .build();

        when(studentService.fetchStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/fetch/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("Pooja"));
    }

    @Test
    void whenIdNotFound_thenReturns404() throws Exception {
        when(studentService.fetchStudentById(99L))
                .thenThrow(new StudentNotfoundException("Student not found with id 99"));

        mockMvc.perform(get("/students/fetch/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void whenInvalidBody_thenReturns400() throws Exception {
        Student invalid = Student.builder().studentName("").build(); // blank name fails @NotBlank

        mockMvc.perform(post("/students/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.studentName").exists());
    }
}