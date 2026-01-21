package com.netflix.controller;

import com.netflix.dto.request.*;
import com.netflix.dto.response.EmailValidationResponse;
import com.netflix.dto.response.LoginResponse;
import com.netflix.dto.response.MessageResponse;
import com.netflix.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//Add your annotations here
@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(path = "/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(authService.signup(userRequest));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/validate-email")
    public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.validateEmail(email));
    }

    @GetMapping(path = "/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping(path = "/resend-verification")
    public ResponseEntity<MessageResponse> resendVerification(@Valid @RequestBody EmailRequest emailRequest) {
        return ResponseEntity.ok(authService.resendVerification(emailRequest.getEmail()));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody EmailRequest emailRequest) {
        return ResponseEntity.ok(authService.forgotPassword(emailRequest.getEmail()));
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword()));
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<MessageResponse> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        String email = authentication.getName();

        return ResponseEntity.ok(authService.changePassword(email, changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword()));
    }

    @GetMapping(path = "/current-user")
    public ResponseEntity<LoginResponse> currentUser(Authentication authentication) {
        String email = authentication.getName();

        return ResponseEntity.ok(authService.currentUser(email));
    }


}
