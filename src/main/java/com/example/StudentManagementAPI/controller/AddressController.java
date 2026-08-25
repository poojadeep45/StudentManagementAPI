package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Address;
import com.example.StudentManagementAPI.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("/students/{studentId}/address")
    public ResponseEntity<Address> saveAddress(@PathVariable Long studentId, @Valid @RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(studentId, address));
    }

    @GetMapping("/students/{studentId}/address")
    public ResponseEntity<Address> fetchAddress(@PathVariable Long studentId) {
        return ResponseEntity.ok(addressService.fetchAddressByStudentId(studentId));
    }
}
