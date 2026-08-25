package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Address;
import com.example.StudentManagementAPI.Entity.Student;
import com.example.StudentManagementAPI.error.StudentNotfoundException;
import com.example.StudentManagementAPI.repository.AddressRepository;
import com.example.StudentManagementAPI.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Address saveAddress(Long studentId, Address address) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotfoundException("Student not found with id " + studentId));

        address.setStudent(student);
        return addressRepository.save(address);
    }

    @Override
    public Address fetchAddressByStudentId(Long studentId) {
        return addressRepository.findByStudent_studentId(studentId)
                .orElseThrow(() -> new StudentNotfoundException("Address not found for student id " + studentId));
    }
}
