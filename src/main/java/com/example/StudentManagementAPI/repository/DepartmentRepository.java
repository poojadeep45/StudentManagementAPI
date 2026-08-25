package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByDeptName(String deptName);

    @Query(value = "SELECT d.* FROM departments d " +
            "JOIN students s ON s.dept_id = d.dept_id " +
            "GROUP BY d.dept_id " +
            "HAVING COUNT(s.student_id) > :minCount",
            nativeQuery = true)
    List<Department> findDepartmentsWithMoreThanNStudents(@Param("minCount") int minCount);
}
