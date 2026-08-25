package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByStudentEmail(String studentEmail);

    Optional<Student> findByStudentNIC(String studentNIC);

    List<Student> findByStudentNameIgnoreCase(String studentName);

    boolean existsByStudentEmail(String studentEmail);

    boolean existsByStudentNIC(String studentNIC);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.department.deptId = :newDeptId WHERE s.department.deptId = :oldDeptId")
    int transferStudentsToDepartment(@Param("oldDeptId") Long oldDeptId, @Param("newDeptId") Long newDeptId);

}
