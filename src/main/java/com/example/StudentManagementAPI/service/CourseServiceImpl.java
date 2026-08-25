package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Course;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course saveCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("Course code already exists: " + course.getCourseCode());
        }
        return courseRepository.save(course);
    }

    @Override
    public List<Course> fetchCourseList() {
        return courseRepository.findAll();
    }

    @Override
    public Course fetchCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new StudentNotfoundException("Course not found with id " + courseId));
    }
}
