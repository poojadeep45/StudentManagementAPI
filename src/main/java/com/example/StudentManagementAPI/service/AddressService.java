package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    Address saveAddress(Long studentId, Address address);
    Address fetchAddressByStudentId(Long studentId);
}
