package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository <Instructor, Long> {
}
