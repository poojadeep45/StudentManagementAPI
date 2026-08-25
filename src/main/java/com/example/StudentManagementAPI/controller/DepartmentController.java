package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Department;
import com.example.StudentManagementAPI.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/departments/save")
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.saveDepartment(department));
    }

    @GetMapping("/departments/fetch")
    public ResponseEntity<List<Department>> fetchDepartmentList() {
        return ResponseEntity.ok(departmentService.fetchDepartmentList());
    }

    @GetMapping("/departments/fetch/{id}")
    public ResponseEntity<Department> fetchDepartmentById(@PathVariable("id") Long deptId) {
        return ResponseEntity.ok(departmentService.fetchDepartmentById(deptId));
    }

    @DeleteMapping("/departments/delete/{id}")
    public ResponseEntity<String> deleteDepartmentById(@PathVariable("id") Long deptId) {
        departmentService.deleteDepartmentById(deptId);
        return ResponseEntity.ok("Deleted department with id " + deptId);
    }

    @GetMapping("/departments/large")
    public ResponseEntity<List<Department>> fetchLargeDepartments(@RequestParam int minCount) {
        return ResponseEntity.ok(departmentService.fetchDepartmentsWithMoreThanNStudents(minCount));
    }

    @PutMapping("/departments/transfer")
    public ResponseEntity<String> transferStudents(
            @RequestParam Long oldDeptId,
            @RequestParam Long newDeptId) {
        int updated = departmentService.transferStudents(oldDeptId, newDeptId);
        return ResponseEntity.ok(updated + " student(s) transferred.");
    }
}
