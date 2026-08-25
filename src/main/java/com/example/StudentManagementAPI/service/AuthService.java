package com.example.StudentManagementAPI.service;

import com.example.StudentManagementAPI.Entity.User;

public interface AuthService {
    User registerUser(User user);
    void resendVerification(String userEmail);
    void requestPasswordReset(String userEmail);
    void resetPassword(String token, String newPassword);
    void changePassword(String userName, String oldPassword, String newPassword);

}
