package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByStudent_studentId(Long studentId);
}
