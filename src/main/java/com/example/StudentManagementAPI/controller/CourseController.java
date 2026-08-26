package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Course;
import com.example.StudentManagementAPI.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "Manage course records")
@RequestMapping("/courses")
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Operation(summary = "Create a new course", description = "Saves a new course record and returns the persisted entity with its generated ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body")
    })
    @PostMapping("/save")
    public ResponseEntity<Course> saveCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(course));
    }

    @Operation(summary = "Fetch all courses", description = "Returns the full list of course records")
    @GetMapping("/fetch")
    public ResponseEntity<List<Course>> fetchCourseList() {
        return ResponseEntity.ok(courseService.fetchCourseList());
    }

    @Operation(summary = "Fetch a course by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "No course exists with the given ID")
    })
    @GetMapping("/fetch/{id}")
    public ResponseEntity<Course> fetchCourseById(@PathVariable("id") Long courseId) {
        return ResponseEntity.ok(courseService.fetchCourseById(courseId));
    }
}
