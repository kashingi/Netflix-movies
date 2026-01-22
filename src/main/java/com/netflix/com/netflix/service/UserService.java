package com.netflix.service;

import com.netflix.dto.request.UserRequest;
import com.netflix.dto.response.MessageResponse;
import com.netflix.dto.response.PageResponse;
import com.netflix.dto.response.UserResponse;

//Add your annotations here
public interface UserService {
    MessageResponse createUser(UserRequest userRequest);

    MessageResponse updateUser(Long id, UserRequest userRequest);

    PageResponse<UserResponse> getAllUsers(int page, int size, String search);

    MessageResponse deleteUser(Long id, String currentUserEmail);

    MessageResponse toggleUserStatus(Long id, String currentUserEmail);

    MessageResponse changeUserRole(Long id, UserRequest userRequest);
}
