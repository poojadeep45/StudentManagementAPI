package com.example.StudentManagementAPI.controller;

import com.example.StudentManagementAPI.Entity.Address;
import com.example.StudentManagementAPI.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Addresses", description = "Manage a student's address record")
@RequestMapping("/students")
@RestController
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Operation(summary = "Add an address for a student", description = "Creates an address record linked to the given student")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed on the request body"),
            @ApiResponse(responseCode = "404", description = "No student exists with the given ID")
    })
    @PostMapping("/{studentId}/address")
    public ResponseEntity<Address> saveAddress(@PathVariable Long studentId, @Valid @RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.saveAddress(studentId, address));
    }

    @Operation(summary = "Fetch a student's address", description = "Returns the address record linked to the given student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "No address exists for the given student ID")
    })
    @GetMapping("/{studentId}/address")
    public ResponseEntity<Address> fetchAddress(@PathVariable Long studentId) {
        return ResponseEntity.ok(addressService.fetchAddressByStudentId(studentId));
    }
}
