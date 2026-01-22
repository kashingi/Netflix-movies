package com.netflix.controller;

import com.netflix.dto.request.UserRequest;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.UserResponse;
import com.netflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//Add your annotations here
@RestController
@RequestMapping(path = "/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/createUser")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping(path = "/updateUser/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(userService.getAllUsers(page, size, search));
    }

    @DeleteMapping(path = "/deleteUser/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        return ResponseEntity.ok(userService.deleteUser(id, currentUserEmail));
    }

    @PutMapping(path = "/{id}/toggle-status")
    public ResponseEntity<MessageResponse> toggleUserStatus(@PathVariable Long id, Authentication authentication) {
        String currentUserEmail = authentication.getName();

        return ResponseEntity.ok(userService.toggleUserStatus(id, currentUserEmail));
    }

    @PutMapping(path = "/{id}/change-role")
    public ResponseEntity<MessageResponse> changeUserRole(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.changeUserRole(id, userRequest));
    }
}
