package com.example.StudentManagementAPI.repository;

import com.example.StudentManagementAPI.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByuserName(String userName);
    Optional<User> findByuserEmail(String userEmail);
    boolean existsByuserName(String userName);
    boolean existsByuserEmail(String userEmail);
}
