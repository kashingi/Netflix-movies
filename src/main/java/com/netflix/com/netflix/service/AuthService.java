package com.netflix.service;

import com.netflix.dto.request.UserRequest;
import com.netflix.dto.response.EmailValidationResponse;
import com.netflix.dto.response.LoginResponse;
import com.netflix.dto.response.MessageResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MessageResponse signup(@Valid UserRequest userRequest);

    LoginResponse login(String email, String password);

    EmailValidationResponse validateEmail(String email);

    MessageResponse verifyEmail(String token);

    MessageResponse resendVerification(String email);

    MessageResponse forgotPassword(String email);

    MessageResponse resetPassword(String token, String newPassword);

    MessageResponse changePassword(String email, String currentPassword, String newPassword);

    LoginResponse currentUser(String email);
}
