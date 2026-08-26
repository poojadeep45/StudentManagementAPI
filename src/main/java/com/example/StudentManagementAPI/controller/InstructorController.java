package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Instructor;
import com.example.StudentManagementAPI.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Instructors", description = "Manage instructor records")
@RequestMapping("/instructors")
@RestController
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @Operation(summary = "Create a new instructor", description = "Saves a new instructor record and returns the persisted entity with its generated ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Instructor created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body")
    })
    @PostMapping("/save")
    public ResponseEntity<Instructor> saveInstructor(@Valid @RequestBody Instructor instructor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(instructorService.saveInstructor(instructor));
    }

    @Operation(summary = "Fetch all instructors", description = "Returns the full list of instructor records")
    @GetMapping("/fetch")
    public ResponseEntity<List<Instructor>> fetchInstructorList() {
        return ResponseEntity.ok(instructorService.fetchInstructorList());
    }

    @Operation(summary = "Fetch an instructor by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instructor found"),
            @ApiResponse(responseCode = "404", description = "No instructor exists with the given ID")
    })
    @GetMapping("/fetch/{id}")
    public ResponseEntity<Instructor> fetchInstructorById(
            @Parameter(description = "ID of the instructor to retrieve") @PathVariable("id") Long instructorId) {
        return ResponseEntity.ok(instructorService.fetchInstructorById(instructorId));
    }
}