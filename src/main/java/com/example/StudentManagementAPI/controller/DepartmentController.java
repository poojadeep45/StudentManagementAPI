package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Department;
import com.example.StudentManagementAPI.service.DepartmentService;
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

@Tag(name = "Departments", description = "Manage department records and department-level student operations")
@RequestMapping("/departments")
@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Operation(summary = "Create a new department", description = "Saves a new department record and returns the persisted entity with its generated ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Department created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body")
    })
    @PostMapping("/save")
    public ResponseEntity<Department> saveDepartment(@Valid @RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.saveDepartment(department));
    }

    @Operation(summary = "Fetch all departments", description = "Returns the full list of department records")
    @GetMapping("/fetch")
    public ResponseEntity<List<Department>> fetchDepartmentList() {
        return ResponseEntity.ok(departmentService.fetchDepartmentList());
    }

    @Operation(summary = "Fetch a department by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department found"),
            @ApiResponse(responseCode = "404", description = "No department exists with the given ID")
    })
    @GetMapping("/fetch/{id}")
    public ResponseEntity<Department> fetchDepartmentById(
            @Parameter(description = "ID of the department to retrieve") @PathVariable("id") Long deptId) {
        return ResponseEntity.ok(departmentService.fetchDepartmentById(deptId));
    }

    @Operation(summary = "Delete a department by ID", description = "Permanently removes a department record. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Caller lacks ADMIN role"),
            @ApiResponse(responseCode = "404", description = "No department exists with the given ID")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDepartmentById(
            @Parameter(description = "ID of the department to delete") @PathVariable("id") Long deptId) {
        departmentService.deleteDepartmentById(deptId);
        return ResponseEntity.ok("Deleted department with id " + deptId);
    }

    @Operation(summary = "Fetch departments above a student-count threshold", description = "Returns departments whose enrolled student count exceeds the given minimum")
    @GetMapping("/large")
    public ResponseEntity<List<Department>> fetchLargeDepartments(
            @Parameter(description = "Minimum student count a department must exceed to be included") @RequestParam int minCount) {
        return ResponseEntity.ok(departmentService.fetchDepartmentsWithMoreThanNStudents(minCount));
    }

    @Operation(summary = "Transfer all students between departments", description = "Moves every student from the old department to the new department")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Students transferred successfully"),
            @ApiResponse(responseCode = "404", description = "Old or new department ID does not exist")
    })
    @PutMapping("/transfer")
    public ResponseEntity<String> transferStudents(
            @Parameter(description = "ID of the department to transfer students from") @RequestParam Long oldDeptId,
            @Parameter(description = "ID of the department to transfer students to") @RequestParam Long newDeptId) {
        int updated = departmentService.transferStudents(oldDeptId, newDeptId);
        return ResponseEntity.ok(updated + " student(s) transferred.");
    }
}