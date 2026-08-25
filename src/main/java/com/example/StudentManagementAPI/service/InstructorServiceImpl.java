package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Instructor;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public Instructor saveInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> fetchInstructorList() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor fetchInstructorById(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new StudentNotfoundException("Instructor not found with id " + instructorId));
    }
}
