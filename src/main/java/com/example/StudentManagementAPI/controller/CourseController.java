package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Course;
import com.example.StudentManagementAPI.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/courses/save")
    public ResponseEntity<Course> saveCourse(@Valid @RequestBody Course course) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(course));
    }

    @GetMapping("/courses/fetch")
    public ResponseEntity<List<Course>> fetchCourseList() {
        return ResponseEntity.ok(courseService.fetchCourseList());
    }

    @GetMapping("/courses/fetch/{id}")
    public ResponseEntity<Course> fetchCourseById(@PathVariable("id") Long courseId) {
        return ResponseEntity.ok(courseService.fetchCourseById(courseId));
    }
}
