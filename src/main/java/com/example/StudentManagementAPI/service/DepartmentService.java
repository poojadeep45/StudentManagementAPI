package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Department;

import java.util.List;

public interface DepartmentService {
    Department saveDepartment(Department department);
    List<Department> fetchDepartmentList();
    Department fetchDepartmentById(Long deptId);
    void deleteDepartmentById(Long deptId);
    List<Department> fetchDepartmentsWithMoreThanNStudents(int minCount);
    int transferStudents(Long oldDeptId, Long newDeptId);
}
