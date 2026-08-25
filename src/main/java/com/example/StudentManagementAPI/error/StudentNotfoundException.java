package com.example.StudentManagementAPI.error;

public class StudentNotfoundException extends RuntimeException {
    public StudentNotfoundException() {
        super();
    }
    public StudentNotfoundException(String message) {
        super(message);
    }
}
