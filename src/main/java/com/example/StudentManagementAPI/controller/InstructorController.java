package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Instructor;
import com.example.StudentManagementAPI.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping("/instructors/save")
    public ResponseEntity<Instructor> saveInstructor(@Valid @RequestBody Instructor instructor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instructorService.saveInstructor(instructor));
    }

    @GetMapping("/instructors/fetch")
    public ResponseEntity<List<Instructor>> fetchInstructorList() {
        return ResponseEntity.ok(instructorService.fetchInstructorList());
    }

    @GetMapping("/instructors/fetch/{id}")
    public ResponseEntity<Instructor> fetchInstructorById(@PathVariable("id") Long instructorId) {
        return ResponseEntity.ok(instructorService.fetchInstructorById(instructorId));
    }
}
