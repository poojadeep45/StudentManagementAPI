package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Instructor;

import java.util.List;

public interface InstructorService {
    Instructor saveInstructor(Instructor instructor);
    List<Instructor> fetchInstructorList();
    Instructor fetchInstructorById(Long instructorId);
}
