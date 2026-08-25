package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Department;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.DepartmentRepository;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Department saveDepartment(Department department) {
        if (departmentRepository.findByDeptName(department.getDeptName()).isPresent()) {
            throw new IllegalArgumentException("Department already exists: " + department.getDeptName());
        }
        return departmentRepository.save(department);
    }

    @Override
    public List<Department> fetchDepartmentList() {
        return departmentRepository.findAll();
    }

    @Override
    public Department fetchDepartmentById(Long deptId) {
        return departmentRepository.findById(deptId)
                .orElseThrow(() -> new StudentNotfoundException("Department not found with id " + deptId));
    }

    @Override
    public void deleteDepartmentById(Long deptId) {
        if (!departmentRepository.existsById(deptId)) {
            throw new StudentNotfoundException("Cannot delete. Department not found with id " + deptId);
        }
        departmentRepository.deleteById(deptId);
    }

    @Override
    public List<Department> fetchDepartmentsWithMoreThanNStudents(int minCount) {
        return departmentRepository.findDepartmentsWithMoreThanNStudents(minCount);
    }

    @Override
    public int transferStudents(Long oldDeptId, Long newDeptId) {
        return studentRepository.transferStudentsToDepartment(oldDeptId, newDeptId);
    }
}
