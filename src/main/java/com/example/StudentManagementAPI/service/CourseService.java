package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Course;

import java.util.List;

public interface CourseService {
    Course saveCourse(Course course);
    List<Course> fetchCourseList();
    Course fetchCourseById(Long courseId);
}
